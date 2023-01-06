package ru.otus.homework.repositories;

import ru.otus.homework.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    long count();

    Genre insert(Genre genre);

    Genre update(Genre genre);

    Optional<Genre> getById(long id);

    Optional<Genre> getByName(String genreName);

    List<Genre> getAll();

    void deleteById(long id);

    long clean();
}
