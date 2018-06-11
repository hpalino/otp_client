/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.manager.impl;

import com.nsi.invisee.otp.domain.OtpApi;
import com.nsi.invisee.otp.domain.OtpApiAccess;
import com.nsi.invisee.otp.domain.OtpApiLogTrx;
import com.nsi.invisee.otp.domain.OtpApiToken;
import com.nsi.invisee.otp.dto.response.ResponseDataErrorSendOtpDto;
import com.nsi.invisee.otp.dto.request.RequestSendOtpDto;
import com.nsi.invisee.otp.dto.response.ResponseDto;
import com.nsi.invisee.otp.dto.request.RequestValidOtpDto;
import com.nsi.invisee.otp.dto.response.ResponseDataErrorValidOtpDto;
import com.nsi.invisee.otp.dto.response.ResponseDataSuccessValidOtpDto;
import com.nsi.invisee.otp.enumeration.OtpApiParameterEnum;
import com.nsi.invisee.otp.enumeration.ResponseCodeEnum;
import com.nsi.invisee.otp.enumeration.TypeSendOtpEnum;
import com.nsi.invisee.otp.manager.OtpApiEmailManager;
import com.nsi.invisee.otp.util.DateTimeUtil;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nsi.invisee.otp.manager.OtpApiManager;
import com.nsi.invisee.otp.manager.OtpApiSmsManager;
import com.nsi.invisee.otp.repositories.OtpApiRepository;
import com.nsi.invisee.otp.repositories.OtpApiAccessRepository;
import com.nsi.invisee.otp.repositories.OtpApiLogTrxRepository;
import com.nsi.invisee.otp.repositories.OtpApiParameterRepository;
import com.nsi.invisee.otp.repositories.OtpApiTokenRepository;
import com.nsi.invisee.otp.util.StringUtil;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 *
 * @author hatta.palino
 */
@Service
public class OtpApiManagerImpl implements OtpApiManager {

    @Autowired
    private OtpApiRepository otpApiRepository;

    @Autowired
    private OtpApiAccessRepository otpApiAccessRepository;

    @Autowired
    private OtpApiLogTrxRepository otpApiLogTrxRepository;

    @Autowired
    private OtpApiEmailManager otpApiEmailManager;

    @Autowired
    private OtpApiSmsManager otpApiSmsManager;

    @Autowired
    private OtpApiParameterRepository otpApiParameterRepository;

    @Autowired
    private OtpApiTokenRepository otpApiTokenRepository;

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ResponseDto sendOtp(RequestSendOtpDto requestDto, String ipAddress) {
        OtpApiLogTrx otpApiLogTrx = new OtpApiLogTrx();
        otpApiLogTrx.setDateTime(new Date());
        otpApiLogTrx.setIpAddress(ipAddress);
        otpApiLogTrx.setRequestBody(requestDto.toString());

        otpApiLogTrx = otpApiLogTrxRepository.save(otpApiLogTrx);

        OtpApi otpApi = otpApiRepository.findByCode(requestDto.getChannel());
        if (otpApi == null) {
            ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
            responseData.setTrx_id(requestDto.getTrx_id());
            responseData.setDescription("channel not found");
            return sendResponse(new ResponseDto(ResponseCodeEnum.ACCESS_DENIED, responseData, "channel"), otpApiLogTrx);
        }

        OtpApiAccess otpApiAccess = otpApiAccessRepository.findByIdCodeAndIdIpAddress(otpApi.getCode(), ipAddress);
        if (otpApiAccess == null) {
            ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
            responseData.setTrx_id(requestDto.getTrx_id());
            responseData.setDescription("ip address not allowed : " + ipAddress);
            return sendResponse(new ResponseDto(ResponseCodeEnum.ACCESS_DENIED, responseData, "channel"), otpApiLogTrx);
        }

        SecureRandom sr = new SecureRandom();
        String token = StringUtil.addLeadingZeroes(sr.nextInt(1000000), 6);
        boolean isCustomTemplate = requestDto.getTemplate().equalsIgnoreCase("CUSTOM");
        Integer session = Integer.valueOf(otpApiParameterRepository.getOne(OtpApiParameterEnum.TIME_SESSION_DEFAULT.toString()).getValue());

        OtpApiToken oat = otpApiTokenRepository.findTop1ByOtpApiAndTrxIdOrderByIdDesc(otpApi, requestDto.getTrx_id());
        if (oat != null) {
            Integer repeat = Integer.valueOf(otpApiParameterRepository.getOne(OtpApiParameterEnum.MINUTES_REPEAT.toString()).getValue());
            Date dateRepeat = DateTimeUtil.getCostumMinuteDate(oat.getDateSend(), repeat);
            if (new Date().before(dateRepeat)) {
                ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
                responseData.setTrx_id(requestDto.getTrx_id());
                responseData.setDescription("minimum date repeat : " + repeat + " minutes");
                return sendResponse(new ResponseDto(ResponseCodeEnum.OTP_ALREADY_SEND, responseData, "send OTP"), otpApiLogTrx);
            }
        }

        if (oat == null) {
            oat = new OtpApiToken();
        }

        oat.setDateSend(new Date());
        oat.setExpired(DateTimeUtil.getCostumMinuteDate(oat.getDateSend(), session));
        oat.setIpAddress(ipAddress);
        oat.setOtpApi(otpApi);
        oat.setSession(session);
        oat.setStan(UUID.randomUUID().toString());
        oat.setStatus(0);
        oat.setToken(token);
        oat.setTrxId(requestDto.getTrx_id());
        oat.setType(requestDto.getType());

        ResponseDto resp;

        if (requestDto.getType().equalsIgnoreCase(TypeSendOtpEnum.EMAIL.toString())) {
            if (!otpApi.isStatusEmail() || otpApi.getCodeEmail() == null) {
                ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
                responseData.setTrx_id(requestDto.getTrx_id());
                responseData.setDescription("type : " + requestDto.getType());
                return sendResponse(new ResponseDto(ResponseCodeEnum.TYPE_NOT_SUPPORT, responseData, "send OTP"), otpApiLogTrx);
            }

            if (!validate("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", requestDto.getRecipient())) {
                ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
                responseData.setTrx_id(requestDto.getTrx_id());
                responseData.setDescription("recipient : " + requestDto.getRecipient());
                return sendResponse(new ResponseDto(ResponseCodeEnum.RECIPIENT_NOT_VALID, responseData, "send OTP"), otpApiLogTrx);
            }

            resp = otpApiEmailManager.sendOtp(requestDto, oat, otpApiLogTrx, isCustomTemplate);
        } else if (requestDto.getType().equalsIgnoreCase(TypeSendOtpEnum.SMS.toString())) {
            if (!otpApi.isStatusSms() || otpApi.getCodeSms() == null) {
                ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
                responseData.setTrx_id(requestDto.getTrx_id());
                responseData.setDescription("type : " + requestDto.getType());
                return sendResponse(new ResponseDto(ResponseCodeEnum.TYPE_NOT_SUPPORT, responseData, "send OTP"), otpApiLogTrx);
            }

            if (!validate("\\d+", requestDto.getRecipient())) {
                ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
                responseData.setTrx_id(requestDto.getTrx_id());
                responseData.setDescription("recipient : " + requestDto.getRecipient());
                return sendResponse(new ResponseDto(ResponseCodeEnum.RECIPIENT_NOT_VALID, responseData, "send OTP"), otpApiLogTrx);
            }

            resp = otpApiSmsManager.sendOtp(requestDto, oat, otpApiLogTrx, isCustomTemplate);
        } else {
            ResponseDataErrorSendOtpDto responseData = new ResponseDataErrorSendOtpDto();
            responseData.setTrx_id(requestDto.getTrx_id());
            responseData.setDescription("type : " + requestDto.getType());
            resp = new ResponseDto(ResponseCodeEnum.TYPE_NOT_VALID, responseData, "send OTP");
        }

        if (resp.getCode() == ResponseCodeEnum.SUCCESS_SEND.getCode()) {
            otpApiTokenRepository.save(oat);
        }

        return sendResponse(resp, otpApiLogTrx);
    }

    @Override
    public ResponseDto validOtp(RequestValidOtpDto requestDto, String ipAddress) {
        OtpApiLogTrx otpApiLogTrx = new OtpApiLogTrx();
        otpApiLogTrx.setDateTime(new Date());
        otpApiLogTrx.setIpAddress(ipAddress);
        otpApiLogTrx.setRequestBody(requestDto.toString());

        otpApiLogTrx = otpApiLogTrxRepository.save(otpApiLogTrx);

        OtpApi otpApi = otpApiRepository.findByCode(requestDto.getChannel());
        if (otpApi == null) {
            ResponseDataErrorValidOtpDto responseData = new ResponseDataErrorValidOtpDto();
            responseData.setChannel(requestDto.getChannel());
            responseData.setStan(requestDto.getStan());
            responseData.setToken(requestDto.getToken());
            responseData.setDescription("channel not found");
            return sendResponse(new ResponseDto(ResponseCodeEnum.ACCESS_DENIED, responseData, "channel"), otpApiLogTrx);
        }

        OtpApiAccess otpApiAccess = otpApiAccessRepository.findByIdCodeAndIdIpAddress(otpApi.getCode(), ipAddress);
        if (otpApiAccess == null) {
            ResponseDataErrorValidOtpDto responseData = new ResponseDataErrorValidOtpDto();
            responseData.setChannel(requestDto.getChannel());
            responseData.setStan(requestDto.getStan());
            responseData.setToken(requestDto.getToken());
            responseData.setDescription("ip address not allowed : " + ipAddress);
            return sendResponse(new ResponseDto(ResponseCodeEnum.ACCESS_DENIED, responseData, "channel"), otpApiLogTrx);
        }

        OtpApiToken oat = otpApiTokenRepository.findByStanAndToken(requestDto.getStan(), requestDto.getToken());
        if (oat == null || oat.getStatus() != 0) {
            ResponseDataErrorValidOtpDto responseData = new ResponseDataErrorValidOtpDto();
            responseData.setChannel(requestDto.getChannel());
            responseData.setStan(requestDto.getStan());
            responseData.setToken(requestDto.getToken());
            responseData.setDescription("token invalid : " + requestDto.getToken());
            return sendResponse(new ResponseDto(ResponseCodeEnum.TOKEN_INVALID, responseData, "token"), otpApiLogTrx);
        }

        if (new Date().after(oat.getExpired())) {
            oat.setStatus(3);
            otpApiTokenRepository.save(oat);

            ResponseDataErrorValidOtpDto responseData = new ResponseDataErrorValidOtpDto();
            responseData.setChannel(requestDto.getChannel());
            responseData.setStan(requestDto.getStan());
            responseData.setToken(requestDto.getToken());
            responseData.setDescription("token expired : " + requestDto.getToken());
            return sendResponse(new ResponseDto(ResponseCodeEnum.TOKEN_EXPIRED, responseData, "token"), otpApiLogTrx);
        }

        oat.setStatus(1);
        oat.setDateUsed(new Date());
        otpApiTokenRepository.save(oat);

        ResponseDataSuccessValidOtpDto responseData = new ResponseDataSuccessValidOtpDto();
        responseData.setChannel(requestDto.getChannel());
        responseData.setStan(requestDto.getStan());
        responseData.setToken(requestDto.getToken());
        return sendResponse(new ResponseDto(ResponseCodeEnum.SUCCESS_SEND, responseData, "token"), otpApiLogTrx);
    }

    private ResponseDto sendResponse(ResponseDto responseDto, OtpApiLogTrx otpApiLogTrx) {
        otpApiLogTrx.setResponseBody(responseDto.toString());
        otpApiLogTrxRepository.save(otpApiLogTrx);

        return responseDto;
    }

    private boolean validate(String regex, String value) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(value).find();
    }

}
