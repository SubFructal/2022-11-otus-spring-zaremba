package ru.otus.homework.repositories;

import ru.otus.homework.models.Author;

public interface AuthorRepositoryCustom {
    void deleteCascade(Author author);

    void deleteAllCascade();
}
