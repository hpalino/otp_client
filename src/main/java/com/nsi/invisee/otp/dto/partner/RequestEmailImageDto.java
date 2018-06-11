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
public class RequestEmailImageDto implements Serializable {
    private String name;
    private String file;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "RequestImageDto{" + "name=" + name + ", file=" + file + '}';
    }
}
