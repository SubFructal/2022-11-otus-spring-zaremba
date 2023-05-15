package ru.otus.library.services;

import ru.otus.library.models.Book;

import java.util.List;

public interface BookService {
    long getBooksCount();

    Book addBook(String title, long genreId, long authorId);

    Book addBook(String title, String genreName, String authorName);

    Book changeBook(long id, String title, long genreId, long authorId);

    Book changeBook(long id, String title, String genreName, String authorName);

    Book findBookById(long id);

    List<Book> getAllBooks();

    List<Book> findAllBooksByGenre(String genreName);

    List<Book> findAllBooksByAuthor(String authorName);

    Book deleteBookById(long id);

    long deleteAllBooks();

}
