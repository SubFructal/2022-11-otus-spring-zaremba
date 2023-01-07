insert into authors(name)
values ('Автор_01'),
       ('Автор_02'),
       ('Автор_03');

insert into genres(genre)
values ('Жанр_01'),
       ('Жанр_02');

insert into books(genre_id, author_id, title)
values (1, 3, 'Книга_01'),
       (2, 1, 'Книга_02'),
       (1, 2, 'Книга_03');