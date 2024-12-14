CREATE TABLE task (
    id UUID NOT NULL,
    title VARCHAR(255),
    description VARCHAR(255),
    is_completed BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);