package ru.otus.homework.repositories;

import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    long count();

    Book save(Book book);

    Optional<Book> getById(long id);

    List<Book> getAll();

    List<Book> getAllForSomeGenre(Genre genre);

    List<Book> getAllForSomeAuthor(Author author);

    void deleteById(long id);

    long deleteAll();
}
