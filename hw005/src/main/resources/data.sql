insert into authors(name)
values ('Лев Толстой'),
       ('Стивен Кинг'),
       ('Сергей Лукьяненко'),
       ('Вячеслав Шишков'),
       ('Ник Перумов'),
       ('Вадим Панов'),
       ('Андрей Белянин');

insert into genres(genre)
values ('Русская классика'),
       ('Фантастика'),
       ('Фэнтези'),
       ('Ужасы'),
       ('Приключения');


insert into books(genre_id, author_id, title)
values (1, 1, 'Война и мир'),
       (4, 2, 'Кладбище домашних животных'),
       (1, 4, 'Угрюм-река'),
       (2, 3, 'Лабиринт отражений'),
       (4, 2, 'Кристина'),
       (3, 5, 'Гибель богов');