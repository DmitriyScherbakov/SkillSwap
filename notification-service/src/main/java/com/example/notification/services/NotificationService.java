package com.example.notification.services;

import com.example.skillswap.events.SkillCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @KafkaListener(topics = "skill-events", groupId = "skillswap-group")
    public void handleSkillCreated(SkillCreatedEvent event) {
        System.out.println(">>> [NOTIFICATION-SERVICE] Получено событие: " + event);
        System.out.println(">>> [NOTIFICATION-SERVICE] Отправка email на " + event.ownerUsername() + "@skillswap.com");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(">>> [NOTIFICATION-SERVICE] Email отправлен.");
    }
}