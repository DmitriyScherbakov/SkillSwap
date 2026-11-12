# SkillSwap

REST API for skill sharing within the community. Users offer skills (guitar, Linux, English) and request others.

**Стек:** Java 21 · Spring Boot 4.1 · Spring Security (JWT) · Spring Data JPA · PostgreSQL · H2 · Docker · JUnit 5

---

## Launch

**Requirements:** JDK 21, Docker.

**Dev (H2):**
.\mvnw.cmd spring-boot:run

- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- H2: http://localhost:8080/h2-console (JDBC `jdbc:h2:mem:skillswap`, user `sa`)

**Prod (PostgreSQL):**
docker compose up -d
$env:SPRING_PROFILES_ACTIVE="prod"; .\mvnw.cmd spring-boot:run

PostgreSQL: `localhost:5433`. Данные сохраняются между перезапусками.

**Тесты:**
.\mvnw.cmd clean test

---

## Test users

| Username | Password | Role |
|----------|----------|------|
| alexey | secret | ADMIN |
| maria | secret | USER |
| ivan | secret | USER |

---

## API

**Auth:**
- `POST /api/auth/register` — registration
- `POST /api/auth/login` — login, return JWT

**Skills (require JWT):**
- `GET /api/skills?page=0&size=20&sort=title,asc` — list with pagination
- `POST /api/skills` — create
- `PUT /api/skills/{id}` — update (owner or Admin)
- `DELETE /api/skills/{id}` — delete (only ADMIN)

Cods: `200` success · `201` created · `204` witout body · `400` invalid data · `401` witout token · `403` no rights · `404` didnt found.

---

## Implemented

- JWT authentication, roles `USER`/`ADMIN`
- Resource-level authorization (`@PreAuthorize` + `SkillSecurity`)
- Pagination and sorting, solved N+1 via `@EntityGraph`
- Global error handling, validation
- Audit log via `@Transactional(REQUIRES_NEW)`
- Actuator: health, liveness/readiness
- Swagger UI, `dev`/`prod` profiles, Docker Compose
