# ===== Stage 1: сборка =====
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Сначала копируем только файлы, нужные для загрузки зависимостей
# Это ускоряет повторные сборки: если pom.xml не менялся — зависимости из кэша
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Загружаем зависимости (кэшируется в слое Docker)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Теперь копируем исходники и собираем jar
COPY src src
RUN ./mvnw clean package -DskipTests -B

# ===== Stage 2: runtime =====
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем собранный jar из builder-этапа
COPY --from=builder /app/target/*.jar app.jar

# Открываем порт (информационно, реально публикуется через docker-compose)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]