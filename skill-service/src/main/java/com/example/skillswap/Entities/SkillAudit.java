package com.example.skillswap.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "skill_audit")
public class SkillAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long skillId;

    @Column(nullable = false)
    private String deletedBy;

    @Column(nullable = false)
    private Instant deletedAt;

    public SkillAudit() {}

    public SkillAudit(Long skillId, String deletedBy) {
        this.skillId = skillId;
        this.deletedBy = deletedBy;
        this.deletedAt = Instant.now();
    }
}