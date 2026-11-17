package com.example.skillswap.Repositories;

import com.example.skillswap.Entities.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional <User> findUserByUsername(@NonNull String username);
}
