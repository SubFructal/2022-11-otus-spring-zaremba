package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.models.Book;
import ru.otus.homework.repositories.BookRepository;

import java.util.List;

import static java.lang.String.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    public long getBooksCount() {
        return bookRepository.count();
    }

    @Override
    public Book addBook(String title, long genreId, long authorId) {
        var genre = genreService.findGenreById(genreId);
        var author = authorService.findAuthorById(authorId);
        var book = new Book();
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return bookRepository.insert(book);
    }

    @Override
    public Book addBook(String title, String genreName, String authorName) {
        var genre = genreService.findGenreByName(genreName);
        var author = authorService.findAuthorByName(authorName);
        var book = new Book();
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return bookRepository.insert(book);
    }

    @Override
    public Book changeBook(long id, String title, long genreId, long authorId) {
        var genre = genreService.findGenreById(genreId);
        var author = authorService.findAuthorById(authorId);
        var book = findBookById(id);
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return bookRepository.update(book);
    }

    @Override
    public Book changeBook(long id, String title, String genreName, String authorName) {
        var genre = genreService.findGenreByName(genreName);
        var author = authorService.findAuthorByName(authorName);
        var book = findBookById(id);
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return bookRepository.update(book);
    }

    @Override
    public Book findBookById(long id) {
        return bookRepository.getById(id)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найдена книга с идентификатором %d", id)));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.getAll();
    }

    @Override
    public List<Book> findAllBooksByGenre(String genreName) {
        return bookRepository.getAllByGenre(genreName);
    }

    @Override
    public List<Book> findAllBooksByAuthor(String authorName) {
        return bookRepository.getAllByAuthor(authorName);
    }

    @Override
    public Book deleteBookById(long id) {
        var book = findBookById(id);
        bookRepository.deleteById(id);
        return book;
    }

    @Override
    public long deleteAllBooks() {
        return bookRepository.clean();
    }
}
