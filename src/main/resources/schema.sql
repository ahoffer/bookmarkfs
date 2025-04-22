DROP TABLE IF EXISTS user_drive CASCADE;

CREATE TABLE user_drive
(
    user_id      TEXT PRIMARY KEY,
    data         TEXT       NOT NULL,
    current_hash TEXT        NOT NULL,
    updated      TIMESTAMPTZ NOT NULL
);
