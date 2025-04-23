-- only drop table if schema has changed
-- DROP TABLE IF EXISTS user_drive CASCADE;

CREATE TABLE IF NOT EXISTS drives
(
    user_id      TEXT        PRIMARY KEY,
    data         jsonb       NOT NULL,
    hash         TEXT        NOT NULL,
    updated      TIMESTAMPTZ NOT NULL
);
