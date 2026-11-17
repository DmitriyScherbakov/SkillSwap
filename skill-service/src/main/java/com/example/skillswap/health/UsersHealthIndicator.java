package com.example.skillswap.health;

import com.example.skillswap.Repositories.UserRepository;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class UsersHealthIndicator implements HealthIndicator {

    private final UserRepository userRepository;

    public UsersHealthIndicator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Health health() {
        long count = userRepository.count();
        if (count == 0) {
            return Health.down()
                    .withDetail("reason", "No users in the system")
                    .build();
        }
        return Health.up()
                .withDetail("users", count)
                .build();
    }
}