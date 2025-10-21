CREATE TABLE tasks
(
    id          INT         NOT NULL,
    name        VARCHAR(64) NOT NULL,
    description VARCHAR(127),
    PRIMARY KEY (id)
)