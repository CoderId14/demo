package com.example.demo.Service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.example.demo.Utils.AppConstants.EMAIL_SEND_FROM;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements IEmailSender {

    private final JavaMailSender mailSender;

    @Override
    @Async("taskExecutor")
    public void send(String to, String email) {
        try{
            log.info("Execute method with configured executor" +
                    Thread.currentThread().getName());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom(EMAIL_SEND_FROM);
            mailSender.send(message);
        }catch (MessagingException e){
            log.error("Fail to send email", e);
            throw new IllegalStateException("Fail to send email");
        }
    }

    @Override
    public void receive(String from, Object object) {

    }
}
