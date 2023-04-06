package com.uhyeah.choolcheck.global;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(String receive, String subject, String text) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        try {
            simpleMailMessage.setTo(receive);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(text);

            javaMailSender.send(simpleMailMessage);

        } catch (Exception e) {
            log.error("[MailException]" + e.toString());
        }
    }



}
