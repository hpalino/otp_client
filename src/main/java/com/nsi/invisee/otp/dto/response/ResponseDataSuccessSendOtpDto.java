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
public class ResponseDataSuccessSendOtpDto implements ResponseDataDto {
    
    private String channel;
    private String recipient;
    private String trx_id;
    private String type;
    private String template;
    private Integer time_session;
    private String date_sender;
    private String stan;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getTime_session() {
        return time_session;
    }

    public void setTime_session(Integer time_session) {
        this.time_session = time_session;
    }

    public String getDate_sender() {
        return date_sender;
    }

    public void setDate_sender(String date_sender) {
        this.date_sender = date_sender;
    }

    @Override
    public String toString() {
        return "ResponseDataSuccessSendOtpDto{" + "channel=" + channel + ", recipient=" + recipient + ", trx_id=" + trx_id + ", type=" + type + ", template=" + template + ", time_session=" + time_session + ", date_sender=" + date_sender + ", stan=" + stan + '}';
    }
}
