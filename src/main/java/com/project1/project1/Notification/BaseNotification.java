package com.project1.project1.Notification;

import org.springframework.stereotype.Service;

@Service
public class BaseNotification implements NotificationService {
    @Override
    public void sendNotification(String message) {}
}
