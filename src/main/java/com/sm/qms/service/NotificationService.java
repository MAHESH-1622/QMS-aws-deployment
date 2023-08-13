package com.sm.qms.service;

import com.sm.qms.model.entity.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendNotification(Notification notification) {
        String email = notification.email;
        if (email != null && !email.isEmpty()) {
            emailService.sendNotification(notification);
        }
    }
}
