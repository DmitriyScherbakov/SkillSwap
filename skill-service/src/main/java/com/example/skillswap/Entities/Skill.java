package com.example.skillswap.Entities;

import com.example.skillswap.DTO.SkillDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public Skill() {
    }

    public Skill(String title, String description, User owner) {
        this.title = title;
        this.description = description;
        this.owner = owner;
    }


    public SkillDto toDto() {
        return new SkillDto(id, title, description,
                owner != null ? owner.getDisplayName() : null,
                owner != null ? owner.getId() : null);
    }

    public static Skill fromDto(SkillDto dto, User owner) {
        Skill skill = new Skill();
        skill.title = dto.title();
        skill.description = dto.description();
        skill.owner = owner;
        return skill;
    }
}
