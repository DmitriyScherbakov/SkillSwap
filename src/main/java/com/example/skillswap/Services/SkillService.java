package com.example.skillswap.Services;

import com.example.skillswap.DTO.SkillDto;
import com.example.skillswap.Entities.Skill;
import com.example.skillswap.Entities.User;
import com.example.skillswap.Repositories.SkillRepository;
import com.example.skillswap.Repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public SkillService(SkillRepository skillRepository, UserRepository userRepository, AuditService auditService){
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<SkillDto> findAllSkills(Pageable pageable) {
        return skillRepository.findAll(pageable).map(Skill::toDto);
    }

    @Transactional(readOnly = true)
    public SkillDto findSkillById(Long id){
        return skillRepository.findById(id).map(Skill::toDto).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Skill with id " + id + " not found"
        ));
    }


    public SkillDto createSkill(SkillDto dto, String username){
        User owner = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found: " + username
                ));

        Skill saved = skillRepository.save(Skill.fromDto(dto, owner));
        return saved.toDto();
    }

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
