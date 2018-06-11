/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.dto.response;

/**
 *
 * @author hatta.palino
 */
public class ResponseDataErrorSendOtpDto implements ResponseDataDto {
    
    private String trx_id;
    private String description;

    public String getTrx_id() {
        return trx_id;
    }

    public void setTrx_id(String trx_id) {
        this.trx_id = trx_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ResponseDataErrorSendOtpDto{" + "trx_id=" + trx_id + ", description=" + description + '}';
    }
}
