/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsi.invisee.otp.domain.OtpApiLogMitra;
import com.nsi.invisee.otp.domain.OtpApiLogTrx;
import com.nsi.invisee.otp.domain.OtpApiParameter;
import com.nsi.invisee.otp.domain.OtpApiTemplateSms;
import com.nsi.invisee.otp.domain.OtpApiToken;
import com.nsi.invisee.otp.dto.response.ResponseDataErrorSendOtpDto;
import com.nsi.invisee.otp.dto.request.RequestSendOtpDto;
import com.nsi.invisee.otp.dto.response.ResponseDto;
import com.nsi.invisee.otp.dto.partner.RequestSmsDto;
import com.nsi.invisee.otp.dto.partner.ResponseSmsCodeEnum;
import com.nsi.invisee.otp.dto.request.RequestSendOtpConvertDto;
import com.nsi.invisee.otp.dto.response.ResponseDataSuccessSendOtpDto;
import com.nsi.invisee.otp.enumeration.OtpApiParameterEnum;
import com.nsi.invisee.otp.enumeration.ResponseCodeEnum;
import com.nsi.invisee.otp.util.DateTimeUtil;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nsi.invisee.otp.manager.OtpApiSmsManager;
import com.nsi.invisee.otp.repositories.OtpApiLogMitraRepository;
import com.nsi.invisee.otp.repositories.OtpApiParameterRepository;
import com.nsi.invisee.otp.repositories.OtpApiTemplateSmsRepository;
import com.nsi.invisee.otp.util.HttpUtil;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author hatta.palino
 */
@Service
public class OtpApiSmsManagerImpl implements OtpApiSmsManager {

    @Autowired
    private OtpApiParameterRepository otpApiParameterRepository;

    @Autowired
    private OtpApiTemplateSmsRepository otpApiTemplateSmsRepository;

    @Autowired
    private OtpApiLogMitraRepository smsApiLogMitraRepository;

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ResponseDto sendOtp(RequestSendOtpDto requestDto, OtpApiToken otpApiToken, OtpApiLogTrx otpApiLogTrx, boolean isCustomTemplate) {
        ResponseDto responseDto;
        try {
            String text;
            OtpApiParameter urlParam = otpApiParameterRepository.getOne(OtpApiParameterEnum.URL_SMS_SEND.toString());

            if (urlParam == null) {
                logger.error("OTP Parameter '" + OtpApiParameterEnum.URL_SMS_SEND.toString() + "' is null");

                ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
                responseData.setTrx_id(requestDto.getTrx_id());
                responseData.setDescription("internal error");
                return new ResponseDto(ResponseCodeEnum.INTERNAL_ERROR, responseData, "send OTP");
            }

            if (isCustomTemplate) {
                if (requestDto.getCustom() == null || requestDto.getCustom().getContent() == null) {
                    ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
                    responseData.setTrx_id(requestDto.getTrx_id());
                    responseData.setDescription("parameter custom invalid value");
                    return new ResponseDto(ResponseCodeEnum.INCOMPLETE_DATA, responseData, "send OTP");
                } else {
                    text = requestDto.getCustom().getContent();
                }
            } else {
                OtpApiTemplateSms template = otpApiTemplateSmsRepository.findByIdCodeAndIdTemplate(requestDto.getChannel(), requestDto.getTemplate());
                if (template == null) {
                    ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
                    responseData.setTrx_id(requestDto.getTrx_id());
                    responseData.setDescription("template " + requestDto.getTemplate() + " not found");
                    return new ResponseDto(ResponseCodeEnum.TEMPLATE_NOT_FOUND, responseData, "send OTP");
                } else {
                    text = template.getContent();
                }
            }

            if (requestDto.getConvert() != null && !requestDto.getConvert().isEmpty()) {
                for (RequestSendOtpConvertDto convert : requestDto.getConvert()) {
                    text = text.replaceAll("#" + convert.getKey() + "#", convert.getValue());
                }
            }

            text = text.replaceAll("#TOKEN#", otpApiToken.getToken());
            text = text.replaceAll("#SESSION#", String.valueOf(otpApiToken.getSession()));

            if (text.length() >= 160) {
                ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
                responseData.setTrx_id(requestDto.getTrx_id());
                responseData.setDescription("length text is " + text.length());
                return new ResponseDto(ResponseCodeEnum.TO_MANY_CHARACTER, responseData, "send OTP");
            }

            RequestSmsDto requestSmsDto = new RequestSmsDto();
            requestSmsDto.setChannel(otpApiToken.getOtpApi().getCodeSms());
            requestSmsDto.setContent(text);
            requestSmsDto.setRecipient(requestDto.getRecipient());
            requestSmsDto.setTrx_id(requestDto.getTrx_id());

            String jsonReq = new ObjectMapper().writeValueAsString(requestSmsDto);

            OtpApiLogMitra log = new OtpApiLogMitra();
            log.setDateTime(new Date());
            log.setRequest(jsonReq);
            log.setOtpApiLogTrx(otpApiLogTrx);
            log.setUrl(urlParam.getValue());

            log = smsApiLogMitraRepository.save(log);

            HttpUtil httpUtil = new HttpUtil();
            String response = httpUtil.body(urlParam.getValue(), jsonReq);

            log.setResponse(response);

            smsApiLogMitraRepository.save(log);

            if (response == null) {
                log.setResponse("### NO RESPONSE ###");

                ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
                responseData.setTrx_id(requestDto.getTrx_id());
                responseData.setDescription("internal error");
                return new ResponseDto(ResponseCodeEnum.INTERNAL_ERROR, responseData, "send OTP");
            }

            Map map = new ObjectMapper().readValue(response, Map.class);

            Integer code = (Integer) map.get("code");
            if (code.equals(ResponseSmsCodeEnum.SUCCESS_SEND.getCode())) {
                ResponseDataSuccessSendOtpDto responseData = new ResponseDataSuccessSendOtpDto();
                responseData.setChannel(requestDto.getChannel());
                responseData.setDate_sender(DateTimeUtil.convertDateToStringCustomized(new Date(), DateTimeUtil.REPORTDATE2));
                responseData.setRecipient(requestDto.getRecipient());
                responseData.setStan(otpApiToken.getStan());
                responseData.setTemplate(requestDto.getTemplate());
                responseData.setTime_session(otpApiToken.getSession() * 60);
                responseData.setTrx_id(requestDto.getTrx_id());
                responseData.setType(requestDto.getType());
                responseDto = new ResponseDto(ResponseCodeEnum.SUCCESS_SEND, responseData, "send OTP");
            } else {
                ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
                responseData.setTrx_id(requestDto.getTrx_id());
                responseData.setDescription("internal error");
                responseDto = new ResponseDto(ResponseCodeEnum.INTERNAL_ERROR, responseData, "send OTP");
            }

        } catch (IOException e) {
            logger.error(e);

            ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
            responseData.setTrx_id(requestDto.getTrx_id());
            responseData.setDescription("internal error");
            responseDto = new ResponseDto(ResponseCodeEnum.INTERNAL_ERROR, responseData, "send OTP");
        }

        return responseDto;
    }

}
