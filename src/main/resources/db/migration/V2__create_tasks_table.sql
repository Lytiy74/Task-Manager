CREATE TABLE tasks
(
    id          SERIAL PRIMARY KEY,
    title VARCHAR(64) NOT NULL,
    description VARCHAR(127)
)