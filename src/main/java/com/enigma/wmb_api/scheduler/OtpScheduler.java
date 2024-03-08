package com.enigma.wmb_api.scheduler;

import com.enigma.wmb_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class OtpScheduler {
    private final AuthService authService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Scheduled(fixedRate = 50000)
    public void checkOtpIsVerified(){
        LocalTime time = LocalTime.now();

        log.info("START checkOtpIsVerified : {}", formatter.format(time));
        authService.checkAccountIsVerified();
        log.info("END checkOtpIsVerified : {}", formatter.format(time));
    }
}
