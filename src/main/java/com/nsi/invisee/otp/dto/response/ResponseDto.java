/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.dto.response;

import com.nsi.invisee.otp.enumeration.ResponseCodeEnum;
import java.io.Serializable;

/**
 *
 * @author hatta.palino
 */
public class ResponseDto implements Serializable {
    
    private final int code;
    private ResponseDataDto data;
    private String info;

    public ResponseDto(ResponseCodeEnum responseCodeEnum, ResponseDataDto data, String field) {
        this.code = responseCodeEnum.getCode();
        this.data = data;
        this.info = responseCodeEnum.getDescription();
        if(field != null) this.info += " : " + field;
    }

    public int getCode() {
        return code;
    }

    public ResponseDataDto getData() {
        return data;
    }

    public void setData(ResponseDataDto data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "ResponseDto{" + "code=" + code + ", data=" + data + ", info=" + info + '}';
    }
    
}
