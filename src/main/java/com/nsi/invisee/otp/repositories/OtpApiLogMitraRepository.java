/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.repositories;

import com.nsi.invisee.otp.domain.OtpApi;
import com.nsi.invisee.otp.domain.OtpApiLogMitra;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author hatta.palino
 */
public interface OtpApiLogMitraRepository extends JpaRepository<OtpApiLogMitra, Long> {
    
}
