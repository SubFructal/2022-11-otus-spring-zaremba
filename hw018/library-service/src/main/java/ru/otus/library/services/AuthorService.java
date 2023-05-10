package ru.otus.library.services;

import ru.otus.library.models.Author;

import java.util.List;

public interface AuthorService {
    long getAuthorsCount();

    Author addAuthor(String authorName);

    Author changeAuthor(long id, String authorName);

    Author findAuthorById(long id);

    Author findAuthorByName(String authorName);

    List<Author> getAllAuthors();

    Author deleteAuthorById(long id);

    long deleteAllAuthors();
}
