-- V2: индексы и ограничения целостности для таблицы skill.
--
-- 1. PostgreSQL не создаёт индекс на внешний ключ автоматически.
--    Без индекса по owner_id любой запрос "навыки пользователя" читает всю
--    таблицу целиком (Seq Scan), а каскадная проверка FK при удалении
--    пользователя сканирует skill полностью на каждую строку.
CREATE INDEX IF NOT EXISTS idx_skill_owner_id ON skill (owner_id);

-- 2. Пагинация по умолчанию идёт с сортировкой по id (см. @PageableDefault),
--    но клиент может запросить sort=title. Индекс по title делает такую
--    сортировку индексным scan вместо сортировки всей выборки.
CREATE INDEX IF NOT EXISTS idx_skill_title ON skill (title);

-- 3. title обязателен по контракту API (@NotBlank в SkillDto), но колонка
--    допускала NULL: такие строки неотличимы от "пустого" навыка и требуют
--    ручной чистки. Закрываем на уровне БД.
UPDATE skill SET title = 'Без названия' WHERE title IS NULL;
ALTER TABLE skill ALTER COLUMN title SET NOT NULL;

-- 4. SkillDto разрешает description до 500 символов, а колонка была
--    VARCHAR(255): валидный по API запрос падал на уровне БД.
ALTER TABLE skill ALTER COLUMN description TYPE VARCHAR(500);
