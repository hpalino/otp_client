/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.manager;

import com.nsi.invisee.otp.domain.OtpApi;
import com.nsi.invisee.otp.domain.OtpApiLogTrx;
import com.nsi.invisee.otp.domain.OtpApiToken;
import com.nsi.invisee.otp.dto.request.RequestSendOtpDto;
import com.nsi.invisee.otp.dto.response.ResponseDto;

/**
 *
 * @author hatta.palino
 */
public interface OtpApiSmsManager {
    ResponseDto sendOtp(RequestSendOtpDto requestDto, OtpApiToken otpApiToken, OtpApiLogTrx otpApiLogTrx, boolean isCustomTemplate);
}
