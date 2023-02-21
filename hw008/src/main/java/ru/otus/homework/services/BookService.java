package ru.otus.homework.services;

import ru.otus.homework.models.Book;

import java.util.List;

public interface BookService {
    long getBooksCount();

    Book addBookByIds(String title, String genreId, String authorId);

    Book addBookByNames(String title, String genreName, String authorName);

    Book changeBookByIds(String id, String title, String genreId, String authorId);

    Book changeBookByNames(String id, String title, String genreName, String authorName);

    Book findBookById(String id);

    List<Book> getAllBooks();

    List<Book> findAllBooksByGenre(String genreName);

    List<Book> findAllBooksByAuthor(String authorName);

    Book deleteBookById(String id);

    long deleteAllBooks();

}
