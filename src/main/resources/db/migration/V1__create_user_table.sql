CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     varchar(32),
    email    varchar(32) UNIQUE,
    password varchar(32)
)
