package com.sm.qms.service.impl;

import com.sm.qms.common.QmsException;
import com.sm.qms.model.constants.AppConstants;
import com.sm.qms.model.entity.Notification;
import com.sm.qms.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    public void sendNotification(Notification notification) {

        String toEmail = notification.email;
        String subject = notification.title;
        String message = notification.message;

        if (toEmail == null || toEmail.isEmpty()) {
            throw new RuntimeException(new QmsException("Email should not be empty!"));
        }
        if (subject == null || subject.isEmpty()) {
            throw new RuntimeException(new QmsException("Subject should not be empty!"));
        }
        if (message == null || message.isEmpty()) {
            throw new RuntimeException(new QmsException("Message should not be empty!"));
        }

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

//            String html = templateEngine.process("email/" + templateName, templateContext);
//            String html = templateEngine.process("email/" + templateName, templateContext);

            helper.setTo(toEmail);
            helper.setFrom(AppConstants.EMAIL_FROM);
            helper.setSubject(subject);
            //helper.setText(html, true);
//            helper.setText("Hello from QMS!");
            helper.setText(message);

//            if (attachment != null) {
//                helper.addAttachment("invoice", attachment);
//            }

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            System.out.printf("Error while adding attachment to email, error is {}", e.getLocalizedMessage());
        }
    }
}
