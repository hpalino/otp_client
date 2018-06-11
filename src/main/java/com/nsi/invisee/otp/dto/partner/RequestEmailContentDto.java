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
public class RequestEmailContentDto implements Serializable {
    private String type;
    private String text;
    private String subject;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "RequestContentDto{" + "type=" + type + ", text=" + text + ", subject=" + subject + '}';
    }
}
