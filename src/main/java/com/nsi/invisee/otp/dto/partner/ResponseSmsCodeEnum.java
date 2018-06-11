/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.dto.partner;

/**
 *
 * @author hatta.palino
 */
public enum ResponseSmsCodeEnum {

    SUCCESS_SEND(0, "Success Send SMS"),
    FAILED_SEND(1, "Failed Send SMS"),
    RECIPIENT_NOT_SUPPORT_SMS(2, "Recipient Not Support SMS"),
    TO_MANY_CHARACTER(3, "More Than 160 Character"), 
    INCOMPLETE_DATA(4, "Incomplete Data"),
    TIMEOUT(5, "Timeout"),
    INTERNAL_ERROR(6, "Internal Error"),
    ACCESS_DENIED(7, "Access Denied");
    
    private final Integer code;
    private final String description;

    private ResponseSmsCodeEnum(Integer code, String description) {
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
