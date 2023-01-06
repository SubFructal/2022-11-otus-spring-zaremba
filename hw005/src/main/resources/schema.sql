drop table if exists authors cascade;
drop table if exists genres cascade;
drop table if exists books cascade;

create table authors
(
    id   bigserial primary key,
    name varchar(255) not null unique
);

create table genres
(
    id    bigserial primary key,
    genre varchar(255) not null unique
);

create table books
(
    id        bigserial primary key,
    genre_id  bigint references genres (id) on delete cascade on update cascade,
    author_id bigint references authors (id) on delete cascade on update cascade,
    title     varchar(255) not null
);