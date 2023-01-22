package ru.otus.homework.repositories;

import ru.otus.homework.models.Comment;

import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    Optional<Comment> getById(long id);

    void deleteById(long id);
}
