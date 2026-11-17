package com.example.skillswap.Tests;

import com.example.skillswap.security.JwtService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET =
            "test-secret-key-that-is-at-least-32-bytes-long-for-hs256!!";
    private static final long EXPIRATION_MS = 3600_000L;

    private final JwtService jwtService = new JwtService(SECRET, EXPIRATION_MS);

    @Test
    void generateToken_thenExtractUsername_returnsSameUsername() {
        String token = jwtService.generateToken("alexey");

        assertThat(token).isNotBlank();
        assertThat(jwtService.extractUsername(token)).isEqualTo("alexey");
    }

    @Test
    void isValid_returnsTrue_forFreshToken() {
        String token = jwtService.generateToken("alexey");
        assertThat(jwtService.isValid(token)).isTrue();
    }

    @Test
    void isValid_returnsFalse_forGarbage() {
        assertThat(jwtService.isValid("garbage")).isFalse();
    }

    @Test
    void isValid_returnsFalse_forExpiredToken() {
        JwtService shortLived = new JwtService(SECRET, -1000L); // уже истёк
        String token = shortLived.generateToken("alexey");
        assertThat(shortLived.isValid(token)).isFalse();
    }

    @Test
    void isValid_returnsFalse_whenSignedWithDifferentKey() {
        JwtService another = new JwtService(
                "another-secret-key-that-is-at-least-32-bytes-long-!!!!", EXPIRATION_MS);
        String token = another.generateToken("alexey");
        assertThat(jwtService.isValid(token)).isFalse();
    }
}