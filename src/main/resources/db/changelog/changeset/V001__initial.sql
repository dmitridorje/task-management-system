-- Создание таблицы user
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

CREATE TABLE token (
                       id SERIAL PRIMARY KEY,
                       token TEXT NOT NULL,
                       refresh_token TEXT NOT NULL,
                       expires_at TIMESTAMP NOT NULL,
                       user_id BIGINT NOT NULL,
                       is_active BOOLEAN NOT NULL,
                       CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Создание таблицы task
CREATE TABLE task (
                      id BIGSERIAL PRIMARY KEY,  -- Use BIGSERIAL instead of AUTO_INCREMENT
                      title VARCHAR(255) NOT NULL,
                      description VARCHAR(1000),
                      task_status VARCHAR(255) NOT NULL,
                      task_priority VARCHAR(255) NOT NULL,
                      author_id BIGINT NOT NULL,
                      assignee_id BIGINT,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (author_id) REFERENCES "users" (id) ON DELETE CASCADE,
                      FOREIGN KEY (assignee_id) REFERENCES "users" (id)
);

-- Создание таблицы comment
CREATE TABLE comment (
                         id BIGSERIAL PRIMARY KEY,  -- Use BIGSERIAL instead of AUTO_INCREMENT
                         content VARCHAR(1000) NOT NULL,
                         author_id BIGINT NOT NULL,
                         task_id BIGINT NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (author_id) REFERENCES "users" (id) ON DELETE CASCADE,
                         FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE
);