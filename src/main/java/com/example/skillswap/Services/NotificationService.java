package com.example.skillswap.Services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Async
    public void sendEmailAsync(String to, String subject) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(">>> [ASYNC END] Email отправлен на " + to + " в потоке: " + Thread.currentThread().getName());
    }
}