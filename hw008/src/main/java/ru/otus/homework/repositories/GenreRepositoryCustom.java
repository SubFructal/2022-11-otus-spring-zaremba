package ru.otus.homework.repositories;

import ru.otus.homework.models.Genre;

public interface GenreRepositoryCustom {
    void deleteCascade(Genre genre);

    void deleteAllCascade();
}
