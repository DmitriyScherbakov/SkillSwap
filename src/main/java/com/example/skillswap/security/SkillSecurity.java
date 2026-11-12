package com.example.skillswap.security;

import com.example.skillswap.Entities.Skill;
import com.example.skillswap.Repositories.SkillRepository;
import org.springframework.stereotype.Component;

@Component("skillSecurity")
public class SkillSecurity {

    private final SkillRepository skillRepository;

    public SkillSecurity(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean isOwner(Long skillId, String username){
        return skillRepository.findById(skillId)
                .map(Skill::getOwner)
                .map(owner -> owner.getUsername().equals(username))
                .orElse(false);
    }
}
