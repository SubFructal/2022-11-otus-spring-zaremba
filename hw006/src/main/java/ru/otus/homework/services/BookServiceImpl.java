package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.models.Book;
import ru.otus.homework.repositories.BookRepository;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Transactional(readOnly = true)
    @Override
    public long getBooksCount() {
        return bookRepository.count();
    }

    @Transactional
    @Override
    public Book addBook(String title, long genreId, long authorId) {
        var genre = genreService.findGenreById(genreId);
        var author = authorService.findAuthorById(authorId);
        var book = new Book();
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book addBook(String title, String genreName, String authorName) {
        var genre = genreService.findGenreByName(genreName);
        var author = authorService.findAuthorByName(authorName);
        var book = new Book();
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book changeBook(long id, String title, long genreId, long authorId) {
        var genre = genreService.findGenreById(genreId);
        var author = authorService.findAuthorById(authorId);
        var book = findBookById(id);
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return book;
    }

    @Transactional
    @Override
    public Book changeBook(long id, String title, String genreName, String authorName) {
        var genre = genreService.findGenreByName(genreName);
        var author = authorService.findAuthorByName(authorName);
        var book = findBookById(id);
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return book;
    }

    @Transactional(readOnly = true)
    @Override
    public Book findBookById(long id) {
        return bookRepository.getById(id)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найдена книга с идентификатором %d", id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.getAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAllBooksByGenre(String genreName) {
        var genre = genreService.findGenreByName(genreName);
        return genre.getBooks();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAllBooksByAuthor(String authorName) {
        var author = authorService.findAuthorByName(authorName);
        return author.getBooks();
    }

    @Transactional
    @Override
    public Book deleteBookById(long id) {
        var book = findBookById(id);
        bookRepository.deleteById(id);
        return book;
    }

    @Transactional
    @Override
    public long deleteAllBooks() {
        return bookRepository.deleteAll();
    }
}
