package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.service.OtpGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OtpGeneratorServiceImplTest {

    private OtpGeneratorService otpGeneratorService;

    @BeforeEach
    void setUp() {
        otpGeneratorService = new OtpGeneratorServiceImpl();
    }
    @Test
    public void test_generate_6_digit_otp() {
        String otp = otpGeneratorService.generateOtp();
        assertEquals(6, otp.length());
    }
}