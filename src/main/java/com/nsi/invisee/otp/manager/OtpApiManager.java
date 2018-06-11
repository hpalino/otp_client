/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.manager;

import com.nsi.invisee.otp.dto.request.RequestSendOtpDto;
import com.nsi.invisee.otp.dto.request.RequestValidOtpDto;
import com.nsi.invisee.otp.dto.response.ResponseDto;

/**
 *
 * @author hatta.palino
 */
public interface OtpApiManager {
    ResponseDto sendOtp(RequestSendOtpDto requestDto, String ipAddress);
    ResponseDto validOtp(RequestValidOtpDto requestDto, String ipAddress);
}
