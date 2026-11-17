package com.example.skillswap.Repositories;

import com.example.skillswap.Entities.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    @EntityGraph(attributePaths = "owner")
    Page<Skill> findAll(Pageable pageable);
}