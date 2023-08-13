package com.sm.qms.service;

import com.sm.qms.model.entity.Notification;

public interface EmailService {

    void sendNotification(Notification notification);
}
