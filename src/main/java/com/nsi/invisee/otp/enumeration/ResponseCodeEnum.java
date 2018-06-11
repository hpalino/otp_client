/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.enumeration;

/**
 *
 * @author hatta.palino
 */
public enum ResponseCodeEnum {

    SUCCESS_SEND(0, "Success"),
    FAILED_SEND(1, "Failed Send"),
    RECIPIENT_NOT_VALID(2, "Recipient Not Valid"),
    TO_MANY_CHARACTER(3, "More Than 160 Character"), 
    INCOMPLETE_DATA(4, "Incomplete Data"),
    INTERNAL_ERROR(5, "Internal Error"),
    ACCESS_DENIED(6, "Access Denied"),
    TYPE_NOT_VALID(7, "Type Sender OTP Not Valid"),
    TYPE_NOT_SUPPORT(8, "Type Sender OTP Not Support"),
    TEMPLATE_NOT_FOUND(9, "Template Not Found"),
    OTP_ALREADY_SEND(10, "OTP Already Send"),
    TOKEN_INVALID(11, "Invalid Token"),
    TOKEN_EXPIRED(12, "Token Expired");    
    
    private final Integer code;
    private final String description;

    private ResponseCodeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
