package ru.otus.homework.repositories;

import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    Optional<Comment> getById(long id);

    List<Comment> getAllForSomeBook(Book book);

    void deleteById(long id);
}
