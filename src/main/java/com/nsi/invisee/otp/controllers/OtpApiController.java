/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.controllers;

import com.nsi.invisee.otp.dto.request.RequestSendOtpDto;
import com.nsi.invisee.otp.dto.request.RequestValidOtpDto;
import com.nsi.invisee.otp.dto.response.ResponseDto;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.nsi.invisee.otp.manager.OtpApiManager;

/**
 *
 * @author hatta.palino
 */
@Controller
public class OtpApiController {
    
    @Autowired
    private OtpApiManager otpApiManager;
    
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto> send(HttpServletRequest request, @RequestBody RequestSendOtpDto requestDto) {
        ResponseDto responseDto = otpApiManager.sendOtp(requestDto, request.getHeader("X-FORWARDED-FOR") == null ? request.getRemoteAddr() : request.getHeader("X-FORWARDED-FOR"));
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/valid", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto> send(HttpServletRequest request, @RequestBody RequestValidOtpDto requestDto) {
        ResponseDto responseDto = otpApiManager.validOtp(requestDto, request.getHeader("X-FORWARDED-FOR") == null ? request.getRemoteAddr() : request.getHeader("X-FORWARDED-FOR"));
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }    
    
}