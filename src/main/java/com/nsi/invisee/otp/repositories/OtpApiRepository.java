/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.repositories;

import com.nsi.invisee.otp.domain.OtpApi;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author hatta.palino
 */
public interface OtpApiRepository extends JpaRepository<OtpApi, String> {
    public OtpApi findByCode(String code);
}
