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
public class ResponseDataErrorValidOtpDto extends ResponseDataSuccessValidOtpDto {
    
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ResponseDataErrorValidOtpDto{" + "channel=" + getChannel() + ", stan=" + getStan() + ", token=" + getToken()  + ", description=" + description + '}';
    }
    
    
}
