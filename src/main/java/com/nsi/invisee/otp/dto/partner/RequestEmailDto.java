/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.dto.partner;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author hatta.palino
 */
public class RequestEmailDto implements Serializable {
    
    private String channel;
    private List<RequestEmailRecipientDto> recipient;
    private RequestEmailContentDto content;
    private List<RequestEmailImageDto> embledded_image;
    private List<RequestEmailImageDto> attachment;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<RequestEmailRecipientDto> getRecipient() {
        return recipient;
    }

    public void setRecipient(List<RequestEmailRecipientDto> recipient) {
        this.recipient = recipient;
    }

    public RequestEmailContentDto getContent() {
        return content;
    }

    public void setContent(RequestEmailContentDto content) {
        this.content = content;
    }

    public List<RequestEmailImageDto> getEmbledded_image() {
        return embledded_image;
    }

    public void setEmbledded_image(List<RequestEmailImageDto> embledded_image) {
        this.embledded_image = embledded_image;
    }

    public List<RequestEmailImageDto> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<RequestEmailImageDto> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "RequestDto{" + "recipient=" + recipient + ", content=" + content + ", embledded_image=" + embledded_image + ", attachment=" + attachment + '}';
    }
    
}
