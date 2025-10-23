CREATE TABLE tasks
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(64) NOT NULL,
    description VARCHAR(127)
)