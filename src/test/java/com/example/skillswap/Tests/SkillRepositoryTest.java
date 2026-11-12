package com.example.skillswap.Tests;

import com.example.skillswap.Entities.Skill;
import com.example.skillswap.Repositories.SkillRepository;
import com.example.skillswap.Repositories.UserRepository;
import com.example.skillswap.Entities.User  ;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class SkillRepositoryTest {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllWithOwner_loadsOwnerInOneQuery() {
        User owner = userRepository.save(new User("alexey", "Алексей", "irrelevant"));
        skillRepository.save(new Skill("Гитара", "Аккорды", owner));
        skillRepository.save(new Skill("Linux", "Настройка", owner));

        List<Skill> skills = skillRepository.findAll();

        assertThat(skills).hasSize(2);
        // Если N+1 вернётся, обращение к owner вызовет доп. запрос —
        // но с JOIN FETCH owner уже инициализирован
        assertThat(skills.getFirst().getOwner().getDisplayName()).isEqualTo("Алексей");
    }
}
