/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.dto.request;

import java.io.Serializable;

/**
 *
 * @author hatta.palino
 */
public class RequestValidOtpDto implements Serializable {
    
    private String channel;
    private String stan;
    private String token;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RequestValidOtpDto{" + "channel=" + channel + ", stan=" + stan + ", token=" + token + '}';
    }
}
