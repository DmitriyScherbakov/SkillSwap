package com.example.skillswap.Tests;

import com.example.skillswap.Entities.Skill;
import com.example.skillswap.Entities.User;
import com.example.skillswap.Repositories.SkillRepository;
import com.example.skillswap.security.SkillSecurity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillSecurityTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillSecurity skillSecurity;

    @Test
    void isOwner_returnsTrue_whenUserOwnsSkill() {
        User maria = new User("maria", "Maria", "hash");
        Skill linux = new Skill("Linux", "desc", maria);
        when(skillRepository.findById(2L)).thenReturn(Optional.of(linux));

        boolean result = skillSecurity.isOwner(2L, "maria");

        assertThat(result).isTrue();
    }

    @Test
    void isOwner_returnsFalse_whenUserIsNotOwner() {
        User ivan = new User("ivan", "Ivan", "hash");
        Skill english = new Skill("Английский", "desc", ivan);
        when(skillRepository.findById(3L)).thenReturn(Optional.of(english));

        boolean result = skillSecurity.isOwner(3L, "maria");

        assertThat(result).isFalse();
    }

    @Test
    void isOwner_returnsFalse_whenSkillDoesNotExist() {
        when(skillRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = skillSecurity.isOwner(999L, "maria");

        assertThat(result).isFalse();
    }
}