
DROP TABLE IF EXISTS user_bookmark_tree CASCADE;

CREATE TABLE user_bookmark_tree (
    user_id TEXT PRIMARY KEY,
    data JSONB NOT NULL,
    current_hash TEXT NOT NULL,
    updated TIMESTAMPTZ NOT NULL
);
