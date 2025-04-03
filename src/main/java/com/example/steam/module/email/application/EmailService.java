package com.example.steam.module.email.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEmail(String toEmail, String title, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(content, true);
        mimeMessageHelper.setReplyTo("noreply@steam.com");
        try{
            mailSender.send(mimeMessage);
        }catch (RuntimeException e){
            e.printStackTrace();
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }
}
