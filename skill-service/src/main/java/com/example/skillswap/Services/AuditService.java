package com.example.skillswap.Services;

import com.example.skillswap.Entities.SkillAudit;
import com.example.skillswap.Repositories.SkillAuditRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final SkillAuditRepository auditRepository;

    public AuditService(SkillAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Transactional
    public void logDelete(Long skillId, String username) {
        auditRepository.save(new SkillAudit(skillId, username));
    }
}
