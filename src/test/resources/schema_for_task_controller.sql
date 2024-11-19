CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(255)        NOT NULL,
    last_name  VARCHAR(255)        NOT NULL,
    username   VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    role       VARCHAR(50)         NOT NULL
);

CREATE TABLE token
(
    id            SERIAL PRIMARY KEY,
    token         TEXT      NOT NULL,
    refresh_token TEXT      NOT NULL,
    expires_at    TIMESTAMP NOT NULL,
    user_id       BIGINT    NOT NULL,
    is_active     BOOLEAN   NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Создание таблицы task
CREATE TABLE task
(
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    description   VARCHAR(1000),
    task_status   VARCHAR(255) NOT NULL,
    task_priority VARCHAR(255) NOT NULL,
    author_id     BIGINT       NOT NULL,
    assignee_id   BIGINT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES "users" (id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES "users" (id)
);

-- Создание таблицы comment
CREATE TABLE comment
(
    id         BIGSERIAL PRIMARY KEY,
    content    VARCHAR(1000) NOT NULL,
    author_id  BIGINT        NOT NULL,
    task_id    BIGINT        NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES "users" (id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES task (id) ON DELETE CASCADE
);

INSERT INTO users (first_name, last_name, username, password, role)
VALUES ('admin', 'adminych', 'admin@example.com', '$2a$10$zox3PUE57yMowF7WnSBjK.lgfqWEskUNIvRjSs35h60i1BRyG/X0O',
        'ADMIN'),
       ('user', 'userovich', 'user@example.com', '$2a$10$/DZ2UQRk.sJeEtTkETyq8.WkGqhuJVykS9NUiKN2iB3/Jllpk1.ki',
        'USER'),
       ('user2', 'userovich2', 'user2@example.com', '$2a$10$/DZ2UQRk.sJeEtTkETyq8.WkGqhuJVykS9NUiKN2iB3/Jllpk1.ki',
        'USER');


INSERT INTO task (title, description, task_status, task_priority, author_id, assignee_id, created_at, updated_at)
VALUES ('Task 1', 'Description for Task 1', 'PENDING', 'HIGH', 1, 2, NOW(), NOW()),
       ('Task 2', 'Description for Task 2', 'IN_PROGRESS', 'MEDIUM', 1, 3, NOW(), NOW()),
       ('Task 3', 'Description for Task 3', 'PENDING', 'HIGH', 1, 2, NOW(), NOW()),
       ('Task 4', 'Description for Task 4', 'COMPLETED', 'LOW', 1, 3, NOW(), NOW());