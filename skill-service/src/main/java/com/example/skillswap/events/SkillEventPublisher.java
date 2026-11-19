package com.example.skillswap.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class SkillEventPublisher {

    private static final String TOPIC = "skill-events";

    private final KafkaTemplate<String, SkillCreatedEvent> kafkaTemplate;

    public SkillEventPublisher(KafkaTemplate<String, SkillCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishSkillCreated(SkillCreatedEvent event) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    send(event);
                }
            });
        } else {
            send(event);
        }
    }

    private void send(SkillCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.skillId().toString(), event);
    }
}
