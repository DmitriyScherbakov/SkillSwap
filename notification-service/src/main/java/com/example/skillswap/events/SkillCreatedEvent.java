package com.example.skillswap.events;

import java.time.Instant;

public record SkillCreatedEvent(
        Long skillId,
        String title,
        String ownerUsername,
        Instant createdAt
) {
    public SkillCreatedEvent(Long skillId, String title, String ownerUsername) {
        this(skillId, title, ownerUsername, Instant.now());
    }
}