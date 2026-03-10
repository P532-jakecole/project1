package com.project1.project1.Notification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationChain {

    @Bean
    public NotificationService notificationService() {
        NotificationService service = new BaseNotification();

        service = new ConsoleNotify(service);

        return service;
    }
}
