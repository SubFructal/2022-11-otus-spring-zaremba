package ru.otus.homework.repositories;

import ru.otus.homework.models.Book;

public interface BookRepositoryCustom {
    void deleteCascade(Book book);

    void deleteAllCascade();
}
