package com.example.skillswap.Tests;

import com.example.skillswap.DTO.SkillDto;
import com.example.skillswap.Entities.Skill;
import com.example.skillswap.Entities.User;
import com.example.skillswap.Repositories.SkillRepository;
import com.example.skillswap.Repositories.UserRepository;
import com.example.skillswap.Services.SkillService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SkillService skillService;

    @Test
    void findById_returnsDto_whenSkillExists() {
        // given
        User owner = new User("alexey", "Алексей", "111");
        Skill skill = new Skill("Гитара", "Аккорды", owner);
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        // when
        SkillDto result = skillService.findSkillById(1L);

        // then
        assertThat(result.title()).isEqualTo("Гитара");
        assertThat(result.ownerName()).isEqualTo("Алексей");
    }

    @Test
    void findById_throws404_whenSkillMissing() {
        when(skillRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> skillService.findSkillById(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("not found");
    }


    @Test
    void create_throws401_whenAuthenticatedUserMissing() {
        SkillDto dto = new SkillDto(null, "Йога", "Утро", null, null);
        when(userRepository.findUserByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> skillService.createSkill(dto, "ghost"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("ghost");
    }

    @Test
    void create_savesSkill_withOwnerFromToken() {
        User owner = new User("alexey", "Алексей", "hashed");
        SkillDto dto = new SkillDto(null, "Йога", "Утро", null, null);
        when(userRepository.findUserByUsername("alexey")).thenReturn(Optional.of(owner));
        when(skillRepository.save(any(Skill.class))).thenAnswer(inv -> inv.getArgument(0));

        SkillDto result = skillService.createSkill(dto, "alexey");

        assertThat(result.title()).isEqualTo("Йога");
        assertThat(result.ownerName()).isEqualTo("Алексей");
    }
}
