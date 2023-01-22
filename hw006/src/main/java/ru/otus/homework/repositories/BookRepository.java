package ru.otus.homework.repositories;

import ru.otus.homework.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    long count();

    Book save(Book book);

    Optional<Book> getById(long id);

    List<Book> getAll();

    void deleteById(long id);

    long deleteAll();
}
