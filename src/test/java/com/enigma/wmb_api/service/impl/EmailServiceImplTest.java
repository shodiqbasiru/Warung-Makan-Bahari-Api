package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.EmailRequest;
import com.enigma.wmb_api.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    @Mock
    private JavaMailSender javaMailSender;

    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailServiceImpl(javaMailSender);
    }
    @Test
    public void test_sendEmail_validRequest() throws MessagingException {
        //given
        EmailRequest request = new EmailRequest();
        request.setTo("to");
        request.setSubject("subject");
        request.setBody("body");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessageHelper messageHelper = mock(MimeMessageHelper.class);
        messageHelper.setTo(request.getTo());
        messageHelper.setSubject(request.getSubject());
        messageHelper.setText(request.getBody(), true);

        //when
        emailService.sendEmail(request);

        //then
        verify(javaMailSender).send(mimeMessage);
    }
}