package ru.otus.homework.services;

import ru.otus.homework.models.Author;

import java.util.List;

public interface AuthorService {
    long getAuthorsCount();

    Author addAuthor(String authorName);

    Author changeAuthor(String id, String authorName);

    Author findAuthorById(String id);

    Author findAuthorByName(String authorName);

    List<Author> getAllAuthors();

    Author deleteAuthorById(String id);

    long deleteAllAuthors();
}
