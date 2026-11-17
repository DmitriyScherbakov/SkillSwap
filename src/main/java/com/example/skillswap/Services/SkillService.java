package com.example.skillswap.Services;

import com.example.skillswap.DTO.PagedResponse;
import com.example.skillswap.DTO.SkillDto;
import com.example.skillswap.Entities.Skill;
import com.example.skillswap.Entities.User;
import com.example.skillswap.Repositories.SkillRepository;
import com.example.skillswap.Repositories.UserRepository;
import com.example.skillswap.events.SkillCreatedEvent;
import com.example.skillswap.events.SkillEventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@AllArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final SkillEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    @Cacheable(value = "skills", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public PagedResponse<SkillDto> findAllSkills(Pageable pageable) {
        return PagedResponse.from(skillRepository.findAll(pageable).map(Skill::toDto));
    }

    @Transactional(readOnly = true)
    @CacheEvict(value = "skills", allEntries = true)
    public SkillDto findSkillById(Long id){
        return skillRepository.findById(id).map(Skill::toDto).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Skill with id " + id + " not found"
        ));
    }

    @CacheEvict(value = "skills", allEntries = true)
    public SkillDto createSkill(SkillDto dto, String username){
        User owner = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found: " + username
                ));

        Skill saved = skillRepository.save(Skill.fromDto(dto, owner));

        eventPublisher.publishSkillCreated(
                new SkillCreatedEvent(saved.getId(), saved.getTitle(), owner.getUsername())
        );
        return saved.toDto();
    }

    @CacheEvict(value = "skills", allEntries = true)
    public void deleteSkill(Long id, String username) {
        if (!skillRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Skill with id " + id + " not found"
            );
        }
        skillRepository.deleteById(id);
        auditService.logDelete(id, username);
    }

    @CacheEvict(value = "skills", allEntries = true)
    public SkillDto updateSkill (Long id, SkillDto dto){
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Skill with id " + id + " not found"
                ));

        skill.setTitle(dto.title());
        skill.setDescription(dto.description());

        return skill.toDto();
    }

}
