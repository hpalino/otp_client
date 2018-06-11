/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.dto.partner;

import java.io.Serializable;

/**
 *
 * @author hatta.palino
 */
public class RequestEmailRecipientDto implements Serializable {
    private String type;
    private String email;

    public RequestEmailRecipientDto() {
    }

    public RequestEmailRecipientDto(String type, String email) {
        this.type = type;
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
