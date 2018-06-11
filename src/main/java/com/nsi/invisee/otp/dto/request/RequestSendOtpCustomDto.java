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
public class RequestSendOtpCustomDto implements Serializable {
    
    private String content;
    private String header;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "RequestSendOtpCustomDto{" + "content=" + content + ", header=" + header + '}';
    }
}
