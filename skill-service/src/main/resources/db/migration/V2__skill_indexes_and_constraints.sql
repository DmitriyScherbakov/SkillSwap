CREATE INDEX IF NOT EXISTS idx_skill_owner_id ON skill (owner_id);
CREATE INDEX IF NOT EXISTS idx_skill_title ON skill (title);

UPDATE skill SET title = 'Без названия' WHERE title IS NULL;
ALTER TABLE skill ALTER COLUMN title SET NOT NULL;

ALTER TABLE skill ALTER COLUMN description TYPE VARCHAR(500);
