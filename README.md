# SkillSwap

REST API для обмена навыками внутри сообщества. Пользователи предлагают умения (гитара, Linux, английский) и запрашивают чужие.

**Стек:** Java 21 · Spring Boot 4.1 · Spring Security (JWT) · Spring Data JPA · PostgreSQL · H2 · Docker · JUnit 5

---

## Запуск

**Требования:** JDK 21, Docker.

**Dev (H2):**
.\mvnw.cmd spring-boot:run

text
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- H2: http://localhost:8080/h2-console (JDBC `jdbc:h2:mem:skillswap`, user `sa`)

**Prod (PostgreSQL):**
docker compose up -d
$env:SPRING_PROFILES_ACTIVE="prod"; .\mvnw.cmd spring-boot:run

text
PostgreSQL: `localhost:5433`. Данные сохраняются между перезапусками.

**Тесты:**
.\mvnw.cmd clean test

text

---

## Тестовые пользователи

| Username | Password | Role |
|----------|----------|------|
| alexey | secret | ADMIN |
| maria | secret | USER |
| ivan | secret | USER |

---

## API

**Auth:**
- `POST /api/auth/register` — регистрация
- `POST /api/auth/login` — логин, возвращает JWT

**Skills (требуют JWT):**
- `GET /api/skills?page=0&size=20&sort=title,asc` — список с пагинацией
- `POST /api/skills` — создать
- `PUT /api/skills/{id}` — обновить (владелец или ADMIN)
- `DELETE /api/skills/{id}` — удалить (только ADMIN)

Коды: `200` успех · `201` создано · `204` без тела · `400` невалидные данные · `401` без токена · `403` нет прав · `404` не найдено.

---

## Реализовано

- JWT-аутентификация, роли `USER`/`ADMIN`
- Resource-level авторизация (`@PreAuthorize` + `SkillSecurity`)
- Пагинация и сортировка, решён N+1 через `@EntityGraph`
- Глобальная обработка ошибок, валидация
- Audit log через `@Transactional(REQUIRES_NEW)`
- Actuator: health, liveness/readiness
- Swagger UI, профили `dev`/`prod`, Docker Compose
