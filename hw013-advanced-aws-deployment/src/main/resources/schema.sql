drop table if exists authors cascade;
drop table if exists genres cascade;
drop table if exists books cascade;
drop table if exists comments cascade;
drop table if exists users cascade;

create table authors
(
    id   bigint primary key AUTO_INCREMENT,
    name varchar(255) not null unique
);

create table genres
(
    id    bigint primary key AUTO_INCREMENT,
    genre varchar(255) not null unique
);

create table books
(
    id        bigint primary key AUTO_INCREMENT,
    genre_id  bigint references genres (id) on delete cascade on update cascade,
    author_id bigint references authors (id) on delete cascade on update cascade,
    title     varchar(255) not null
);

create table comments
(
    id      bigint primary key AUTO_INCREMENT,
    book_id bigint references books (id) on delete cascade on update cascade,
    comment varchar(1000) not null
);

create table users
(
    id   bigint primary key AUTO_INCREMENT,
    username varchar(255) not null unique,
    password varchar(1000) not null,
    authority varchar(255)
);