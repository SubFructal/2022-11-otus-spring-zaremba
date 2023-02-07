package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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

    @Override
    public long getBooksCount() {
        return bookRepository.count();
    }

    @Override
    public Book addBookByIds(String title, String genreId, String authorId) {
        var genre = genreService.findGenreById(genreId);
        var author = authorService.findAuthorById(authorId);
        var book = new Book();
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @Override
    public Book addBookByNames(String title, String genreName, String authorName) {
        var genre = genreService.findGenreByName(genreName);
        var author = authorService.findAuthorByName(authorName);
        var book = new Book();
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @Override
    public Book changeBookByIds(String id, String title, String genreId, String authorId) {
        var genre = genreService.findGenreById(genreId);
        var author = authorService.findAuthorById(authorId);
        var book = findBookById(id);
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @Override
    public Book changeBookByNames(String id, String title, String genreName, String authorName) {
        var genre = genreService.findGenreByName(genreName);
        var author = authorService.findAuthorByName(authorName);
        var book = findBookById(id);
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @Override
    public Book findBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найдена книга с идентификатором %s", id)));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<Book> findAllBooksByGenre(String genreName) {
        var genre = genreService.findGenreByName(genreName);
        return bookRepository.findAllByGenre(genre);
    }

    @Override
    public List<Book> findAllBooksByAuthor(String authorName) {
        var author = authorService.findAuthorByName(authorName);
        return bookRepository.findAllByAuthor(author);
    }

    @Override
    public Book deleteBookById(String id) {
        var book = findBookById(id);
        bookRepository.deleteCascade(book);
        bookRepository.deleteById(id);
        return book;
    }

    @Override
    public long deleteAllBooks() {
        var booksCount = getBooksCount();
        bookRepository.deleteAllCascade();
        bookRepository.deleteAll();
        return booksCount;
    }
}
