package com.example.skillswap.Controllers;

import com.example.skillswap.DTO.PagedResponse;
import com.example.skillswap.DTO.SkillDto;
import com.example.skillswap.Services.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@Tag(name = "Skills", description = "Управление навыками")
public class SkillController {

    private final SkillService service;

    public SkillController(SkillService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Список навыков", description = "Возвращает постраничный список всех навыков")
    public PagedResponse<SkillDto> list(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAllSkills(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Навык", description = "Возвращает навык найденный по ID")
    public SkillDto getById(@PathVariable Long id){
        return service.findSkillById(id);
    }

    @PostMapping
    @Operation(summary = "Создать навык", description = "Создаёт новый навык для текущего пользователя")
    public SkillDto create(@Valid @RequestBody SkillDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        return service.createSkill(dto, auth.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить навык", description = "Удаляет навык по ID (только ADMIN)")
    public void delete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        service.deleteSkill(id, auth.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("@skillSecurity.isOwner(#id, authentication.name) or hasRole('ADMIN')")
    @Operation(summary = "Поменять навык", description = "Обновляет навык (только владелец или ADMIN)")
    public SkillDto update(@PathVariable Long id, @Valid @RequestBody SkillDto dto) {
        return service.updateSkill(id, dto);
    }
}