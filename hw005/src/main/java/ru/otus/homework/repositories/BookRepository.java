package ru.otus.homework.repositories;

import ru.otus.homework.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    long count();

    Book insert(Book book);

    Book update(Book book);

    Optional<Book> getById(long id);

    List<Book> getAll();

    List<Book> getAllByGenre(String genreName);

    List<Book> getAllByAuthor(String authorName);

    void deleteById(long id);

    long deleteAll();
}
