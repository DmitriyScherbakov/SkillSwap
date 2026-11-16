package com.example.skillswap.Tests;

import com.example.skillswap.Controllers.SkillController;
import com.example.skillswap.DTO.PagedResponse;
import com.example.skillswap.DTO.SkillDto;
import com.example.skillswap.Repositories.SkillRepository;
import com.example.skillswap.Services.SkillService;
import com.example.skillswap.security.JwtService;
import com.example.skillswap.security.SecurityConfig;
import com.example.skillswap.security.SkillSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(SkillController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private SkillService skillService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private SkillRepository skillRepository;

    @MockitoBean(name = "skillSecurity")
    private SkillSecurity skillSecurity;

    @MockitoBean
    private CacheManager cacheManager;

    @Test
    void getSkills_returnsPage() throws Exception {
        SkillDto dto = new SkillDto(1L, "Гитара", "Аккорды", "Алексей", 1L);
        PagedResponse<SkillDto> response = new PagedResponse<>(
                List.of(dto),
                0,
                20,
                1,
                1
        );

        when(skillService.findAllSkills(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/api/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Гитара"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void postSkill_returns400_whenTitleBlank() throws Exception {
        SkillDto invalid = new SkillDto(null, "", "desc", null, 1L);

        mockMvc.perform(post("/api/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    @WithMockUser(username = "alexey")
    void postSkill_returns200_whenValid() throws Exception {
        SkillDto input = new SkillDto(null, "Йога", "Утро", null, null);
        SkillDto output = new SkillDto(4L, "Йога", "Утро", "Алексей", 1L);
        when(skillService.createSkill(any(SkillDto.class), eq("alexey"))).thenReturn(output);

        mockMvc.perform(post("/api/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.ownerName").value("Алексей"));
    }

    @Test
    @WithMockUser(username = "maria", roles = "USER")
    void updateSkill_returns200_whenUserIsOwner() throws Exception {
        SkillDto dto = new SkillDto(null, "LinuxAdvanced", "by_maria", null, null);
        SkillDto output = new SkillDto(2L, "LinuxAdvanced", "by_maria", "Мария", 2L);
        when(skillSecurity.isOwner(2L, "maria")).thenReturn(true);
        when(skillService.updateSkill(eq(2L), any(SkillDto.class))).thenReturn(output);

        mockMvc.perform(put("/api/skills/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("LinuxAdvanced"));
    }

    @Test
    @WithMockUser(username = "maria", roles = "USER")
    void updateSkill_returns403_whenUserIsNotOwner() throws Exception {
        SkillDto dto = new SkillDto(null, "hacked", "nope", null, null);
        when(skillSecurity.isOwner(3L, "maria")).thenReturn(false);

        mockMvc.perform(put("/api/skills/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

}
