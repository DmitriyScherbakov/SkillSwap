package com.example.skillswap;

import com.example.skillswap.Entities.Role;
import com.example.skillswap.Entities.Skill;
import com.example.skillswap.Entities.User;
import com.example.skillswap.Repositories.SkillRepository;
import com.example.skillswap.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner seedData(SkillRepository skillRepo, UserRepository userRepo) {
        return args -> {
            if (userRepo.count() == 0) {
                User alexey = userRepo.save(new User("alexey", "Alexey",
                        passwordEncoder.encode("secret"), Role.ADMIN));
                User maria = userRepo.save(new User("maria", "Maria",
                        passwordEncoder.encode("secret"), Role.USER));
                User ivan = userRepo.save(new User("ivan", "Ivan",
                        passwordEncoder.encode("secret"), Role.USER ));

                skillRepo.save(new Skill("Guitar", "I’ll teach you the basic chords.", alexey));
                skillRepo.save(new Skill("Linux", "I'll help you set up your environment.", maria));
                skillRepo.save(new Skill("English teaching", "Conversation club", ivan));
            }
        };
    }
}