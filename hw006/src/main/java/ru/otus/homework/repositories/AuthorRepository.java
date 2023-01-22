package ru.otus.homework.repositories;

import ru.otus.homework.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    long count();

    Author save(Author author);

    Optional<Author> getById(long id);

    Optional<Author> getByName(String authorName);

    List<Author> getAll();

    void deleteById(long id);

    long deleteAll();
}
