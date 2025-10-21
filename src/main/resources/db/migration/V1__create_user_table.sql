CREATE TABLE users
(
    id       INT NOT NULL,
    name     varchar(32),
    email    varchar(32) UNIQUE,
    password varchar(32),
    PRIMARY KEY (id)
)
