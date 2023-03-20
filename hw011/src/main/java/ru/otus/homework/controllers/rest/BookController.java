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
import ru.otus.homework.models.Book;
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
        return Flux.from(authorRepository.findByName(authorName).switchIfEmpty(Mono
                        .error(new NotFoundException(format("Не найден автор с именем %s", authorName)))))
                .flatMap(author ->
                        bookRepository.findAllByAuthor(Mono.just(author), Sort.by(Sort.Direction.ASC, "id")))
                .map(BookDto::transformDomainToDto);
    }

    @GetMapping(value = "/api/books", params = "genreName")
    public Flux<BookDto> findAllBooksByGenre(@RequestParam(value = "genreName") String genreName) {
        return Flux.from(genreRepository.findByName(genreName).switchIfEmpty(Mono
                        .error(new NotFoundException(format("Не найден жанр с названием %s", genreName)))))
                .flatMap(genre ->
                        bookRepository.findAllByGenre(Mono.just(genre), Sort.by(Sort.Direction.ASC, "id")))
                .map(BookDto::transformDomainToDto);
    }

    @GetMapping(value = "/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> findBookById(@PathVariable(value = "id") String id) {
        return bookRepository.findById(id).map(BookDto::transformDomainToDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new NotFoundException(format("Не найдена книга с идентификатором %s", id))));
    }

    @PostMapping(value = "/api/books")
    public Mono<ResponseEntity<BookDto>> createNewBook(@RequestBody BookDto bookDto) {
        return Mono.just(new Book())
                .flatMap(book -> authorRepository.findByName(bookDto.getAuthor())
                        .switchIfEmpty(Mono.error(
                                new NotFoundException(format("Не найден автор с именем %s", bookDto.getAuthor()))))
                        .map(author -> {
                            book.setTitle(bookDto.getTitle());
                            book.setAuthor(author);
                            return book;
                        }))
                .flatMap(book -> genreRepository.findByName(bookDto.getGenre())
                        .switchIfEmpty(Mono.error(
                                new NotFoundException(format("Не найден жанр с названием %s", bookDto.getGenre()))))
                        .map(genre -> {
                            book.setGenre(genre);
                            return book;
                        }))
                .flatMap(bookRepository::save).map(BookDto::transformDomainToDto).map(ResponseEntity::ok);
    }

    @PutMapping(value = "/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> editBook(@PathVariable(value = "id") String id,
                                                  @RequestBody BookDto bookDto) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(format("Не найдена книга с идентификатором %s", id))))
                .flatMap(book -> authorRepository.findByName(bookDto.getAuthor())
                        .switchIfEmpty(Mono.error(
                                new NotFoundException(format("Не найден автор с именем %s", bookDto.getAuthor()))))
                        .map(author -> {
                            book.setTitle(bookDto.getTitle());
                            book.setAuthor(author);
                            return book;
                        }))
                .flatMap(book -> genreRepository.findByName(bookDto.getGenre())
                        .switchIfEmpty(Mono.error(
                                new NotFoundException(format("Не найден жанр с названием %s", bookDto.getGenre()))))
                        .map(genre -> {
                            book.setGenre(genre);
                            return book;
                        }))
                .flatMap(bookRepository::save).map(BookDto::transformDomainToDto).map(ResponseEntity::ok);
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
}
