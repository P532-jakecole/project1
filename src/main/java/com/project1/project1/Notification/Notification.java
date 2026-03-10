package com.project1.project1.Notification;

public abstract class Notification implements NotificationService{
    private NotificationService notificationService;

    public Notification(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void sendNotification(String message) {
        notificationService.sendNotification(message);
    }
}
