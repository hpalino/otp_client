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
public class RequestSmsDto implements Serializable {
    
    private String channel;
    private String recipient;
    private String trx_id;
    private String content;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTrx_id() {
        return trx_id;
    }

    public void setTrx_id(String trx_id) {
        this.trx_id = trx_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RequestDto{" + "channel=" + channel + ", recipient=" + recipient + ", trx_id=" + trx_id + ", content=" + content + '}';
    }
    
}
