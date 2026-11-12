# SkillSwap

Платформа для обмена навыками: пользователи предлагают свои умения (гитара, Linux, английский) и запрашивают чужие.

## Стек
- **Backend:** Java 21, Spring Boot 4.1, Spring Security (JWT), Spring Data JPA
- **БД:** PostgreSQL 16 (prod), H2 (dev/test)
- **Тесты:** JUnit 5, Mockito, Spring Boot Test, Testcontainers
- **Инфраструктура:** Docker, Docker Compose

## Запуск

### Требования
- JDK 21
- Docker Desktop

### Dev (H2 in-memory)
```bash
.\mvnw.cmd spring-boot:run
