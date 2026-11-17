package com.example.skillswap.Tests;

import com.example.skillswap.DTO.SkillDto;
import com.example.skillswap.Entities.Role;
import com.example.skillswap.Entities.Skill;
import com.example.skillswap.Entities.User;
import com.example.skillswap.Repositories.SkillRepository;
import com.example.skillswap.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SkillAuthorizationIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private SkillRepository skillRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JsonMapper jsonMapper;

    private Long mariaSkillId;
    private Long ivanSkillId;

    @BeforeEach
    void setUp() {
        User maria = userRepository.save(new User(
                "test_maria", "Мария", passwordEncoder.encode("secret"), Role.USER));
        User ivan = userRepository.save(new User(
                "test_ivan", "Иван", passwordEncoder.encode("secret"), Role.USER));

        mariaSkillId = skillRepository.save(new Skill("Linux", "desc", maria)).getId();
        ivanSkillId = skillRepository.save(new Skill("English", "desc", ivan)).getId();
    }

    @Test
    void maria_canUpdateOwnSkill() throws Exception {
        mockMvc.perform(put("/api/skills/" + mariaSkillId)
                        .with(user("test_maria").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                new SkillDto(null, "LinuxAdvanced", "by_maria", null, null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("LinuxAdvanced"));
    }

    @Test
    void maria_cannotUpdateIvanSkill() throws Exception {
        mockMvc.perform(put("/api/skills/" + ivanSkillId)
                        .with(user("test_maria").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                new SkillDto(null, "hacked", "nope", null, null))))
                .andExpect(status().isForbidden());
    }

    @Test
    void admin_canUpdateAnySkill() throws Exception {
        mockMvc.perform(put("/api/skills/" + ivanSkillId)
                        .with(user("test_admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                new SkillDto(null, "EnglishPRO", "by_admin", null, null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("EnglishPRO"))
                .andExpect(jsonPath("$.ownerName").value("Иван"));
    }

    @Test
    void maria_cannotDeleteEvenOwnSkill() throws Exception {
        mockMvc.perform(delete("/api/skills/" + mariaSkillId)
                        .with(user("test_maria").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void admin_canDeleteAnySkill() throws Exception {
        mockMvc.perform(delete("/api/skills/" + mariaSkillId)
                        .with(user("test_admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }
}