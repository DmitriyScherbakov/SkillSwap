package com.example.skillswap.Services;

import com.example.skillswap.Entities.SkillAudit;
import com.example.skillswap.Repositories.SkillAuditRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Записывает аудит удаления навыка.
 *
 * <p>Работает в транзакции вызывающей стороны (пропагация по умолчанию
 * {@code REQUIRED}). Раньше здесь стоял {@code REQUIRES_NEW}, и аудит
 * коммитился в отдельной транзакции: если удаление откатывалось, запись
 * «навык удалён пользователем X» всё равно оставалась в базе и врала.
 *
 * <p>Отдельная транзакция была бы оправдана, если бы требовалось сохранить
 * аудит неудачных попыток удаления. Сейчас такого требования нет, поэтому
 * аудит должен разделять судьбу самой операции.
 */
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
