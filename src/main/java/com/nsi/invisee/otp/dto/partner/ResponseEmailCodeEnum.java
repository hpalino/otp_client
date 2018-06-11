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
public enum ResponseEmailCodeEnum {

    SUCCESS_SEND(0, "Success Send Email"),
    FAILED_SEND(1, "Failed Send Email"),
    ACCESS_DENIED(7, "Access Denied");
    
    private final Integer code;
    private final String description;

    private ResponseEmailCodeEnum(Integer code, String description) {
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
