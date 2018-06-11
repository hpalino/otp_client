/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.repositories;

import com.nsi.invisee.otp.domain.OtpApiAccess;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author hatta.palino
 */
public interface OtpApiAccessRepository extends JpaRepository<OtpApiAccess, String> {
    public OtpApiAccess findByIdCodeAndIdIpAddress(String code, String ip);
}
