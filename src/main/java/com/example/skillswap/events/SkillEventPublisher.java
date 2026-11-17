package com.example.skillswap.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SkillEventPublisher {

    private static final String TOPIC = "skill-events";
    private final KafkaTemplate<String, SkillCreatedEvent> kafkaTemplate;

    public SkillEventPublisher(KafkaTemplate<String, SkillCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishSkillCreated(SkillCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.skillId().toString(), event);
    }
}