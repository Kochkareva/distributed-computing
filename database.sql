-- Создание таблицы тренировок
CREATE TABLE t_training (
                     id SERIAL PRIMARY KEY,
                     name VARCHAR(255) NOT NULL,
                     description VARCHAR(255) NOT NULL
);
-- Создание таблицы упражнений
CREATE TABLE t_exercise (
                     id SERIAL PRIMARY KEY,
                     name VARCHAR(255) NOT NULL,
                     description VARCHAR(255) NOT NULL,
                     id_training INTEGER,
                     FOREIGN KEY (id_training) REFERENCES t_training(id)
);
