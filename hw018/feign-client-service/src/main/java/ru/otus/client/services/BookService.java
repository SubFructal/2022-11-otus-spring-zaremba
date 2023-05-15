package ru.otus.client.services;

import ru.otus.client.dto.BookDto;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();

    List<BookDto> findAllBooksByAuthor(String authorName);

    List<BookDto> findAllBooksByGenre(String genreName);

    BookDto findBookById(long id);

    BookDto deleteBookById(long id);

    String deleteAllBooks();

    BookDto addBook(BookDto bookDto);

    BookDto changeBook(long id, BookDto bookDto);

}
