package ru.otus.homework.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.controllers.rest.dto.BookDto;
import ru.otus.homework.exceptions.NotFoundException;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;
import ru.otus.homework.repositories.AuthorRepository;
import ru.otus.homework.repositories.BookRepository;
import ru.otus.homework.repositories.CommentRepository;
import ru.otus.homework.repositories.GenreRepository;

import static java.lang.String.format;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final CommentRepository commentRepository;

    @GetMapping(value = "/api/books")
    public Flux<BookDto> findAllBooks() {
        return bookRepository
                .findAll(Sort.by(Sort.Direction.ASC, "id"))
                .map(BookDto::transformDomainToDto);
    }

    @GetMapping(value = "/api/books", params = "authorName")
    public Flux<BookDto> findAllBooksByAuthor(@RequestParam(value = "authorName") String authorName) {
        return Flux.from(getAuthorFromDatabaseByName(authorName)).flatMap(author ->
                        bookRepository.findAllByAuthor(author, Sort.by(Sort.Direction.ASC, "id")))
                .map(BookDto::transformDomainToDto);
    }

    @GetMapping(value = "/api/books", params = "genreName")
    public Flux<BookDto> findAllBooksByGenre(@RequestParam(value = "genreName") String genreName) {
        return Flux.from(getGenreFromDatabaseByName(genreName)).flatMap(genre ->
                        bookRepository.findAllByGenre(genre, Sort.by(Sort.Direction.ASC, "id")))
                .map(BookDto::transformDomainToDto);
    }

    @GetMapping(value = "/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> findBookById(@PathVariable(value = "id") String id) {
        return getBookFromDatabaseById(id)
                .map(BookDto::transformDomainToDto)
                .map(ResponseEntity::ok);
    }

    @PostMapping(value = "/api/books")
    public Mono<ResponseEntity<BookDto>> createNewBook(@RequestBody BookDto bookDto) {
        var authorMono = getAuthorFromDatabaseByName(bookDto.getAuthor());
        var genreMono = getGenreFromDatabaseByName(bookDto.getGenre());
        return Mono.zip(authorMono, genreMono).flatMap(tuple -> {
            var book = new Book();
            book.setTitle(bookDto.getTitle());
            book.setAuthor(tuple.getT1());
            book.setGenre(tuple.getT2());
            return bookRepository.save(book);
        }).map(BookDto::transformDomainToDto).map(ResponseEntity::ok);
    }

    @PutMapping(value = "/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> editBook(@PathVariable(value = "id") String id,
                                                  @RequestBody BookDto bookDto) {
        var authorMono = getAuthorFromDatabaseByName(bookDto.getAuthor());
        var genreMono = getGenreFromDatabaseByName(bookDto.getGenre());
        var bookMono = getBookFromDatabaseById(id);
        return Mono.zip(bookMono, authorMono, genreMono).flatMap(tuple -> {
            var book = tuple.getT1();
            book.setTitle(bookDto.getTitle());
            book.setAuthor(tuple.getT2());
            book.setGenre(tuple.getT3());
            return bookRepository.save(book);
        }).map(BookDto::transformDomainToDto).map(ResponseEntity::ok);

    }

    @DeleteMapping(value = "/api/books/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable(value = "id") String id) {
        return bookRepository.deleteById(id)
                .and(commentRepository.deleteAllByBookId(id))
                .map(ResponseEntity::ok);
    }

    @DeleteMapping(value = "/api/books")
    public Mono<ResponseEntity<Void>> deleteAllBooks() {
        return bookRepository.deleteAll()
                .and(commentRepository.deleteAll())
                .map(ResponseEntity::ok);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<String> handleNotFound(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    private Mono<Author> getAuthorFromDatabaseByName(String authorName) {
        return authorRepository.findByName(authorName).switchIfEmpty(Mono
                .error(new NotFoundException(format("Не найден автор с именем %s", authorName))));
    }

    private Mono<Genre> getGenreFromDatabaseByName(String genreName) {
        return genreRepository.findByName(genreName).switchIfEmpty(Mono
                .error(new NotFoundException(format("Не найден жанр с названием %s", genreName))));
    }

    private Mono<Book> getBookFromDatabaseById(String bookId) {
        return bookRepository.findById(bookId).switchIfEmpty(Mono
                .error(new NotFoundException(format("Не найдена книга с идентификатором %s", bookId))));
    }
}
