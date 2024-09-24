INSERT INTO users (name, email)
VALUES ('Ivan Ivanov', 'ivanov123@mail.ru');

INSERT INTO categories (name)
VALUES ('Первая');

INSERT INTO events (title, annotation, description, category_id, user_id, created,
                    event_date, published, latitude, longitude, paid, participant_limit, request_moderation, state)
VALUES ('title', 'event annotation for test', 'description annotation for test', 1, 1, '2024-09-19 11:11:11',
        '2025-09-20 11:11:11', '2024-09-20 01:11:11', 55.398595, 37.555139, false, 0, false, 'PUBLISHED');

INSERT INTO comments (text, event_id, author_id, created)
VALUES ('Первый комментарий', 1, 1, '2024-09-20 11:11:11');