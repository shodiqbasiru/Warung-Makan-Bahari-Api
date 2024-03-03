package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.service.OtpGenerationService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpGenerationServiceImpl implements OtpGenerationService {
    @Override
    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String output = Integer.toString(randomNumber);
        while (output.length() < 6){
            output = "0" + output;
        }
        return output;
    }
}
