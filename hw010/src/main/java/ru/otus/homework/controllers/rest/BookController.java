package ru.otus.homework.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.controllers.rest.dto.BookDto;
import ru.otus.homework.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping(value = "/api/books")
    public List<BookDto> findAllBooks() {
        return bookService.getAllBooks()
                .stream()
                .map(BookDto::transformDomainToDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/api/books", params = "authorName")
    public List<BookDto> findAllBooksByAuthor(@RequestParam(value = "authorName") String authorName) {
        return bookService.findAllBooksByAuthor(authorName)
                .stream()
                .map(BookDto::transformDomainToDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/api/books", params = "genreName")
    public List<BookDto> findAllBooksByGenre(@RequestParam(value = "genreName") String genreName) {
        return bookService.findAllBooksByGenre(genreName)
                .stream()
                .map(BookDto::transformDomainToDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/api/books/{id}")
    public BookDto findBookById(@PathVariable(value = "id") long id) {
        var book = bookService.findBookById(id);
        return BookDto.transformDomainToDto(book);
    }

    @PostMapping(value = "/api/books")
    public BookDto createNewBook(@RequestBody BookDto bookDto) {
        var book = bookService.addBook(bookDto.getTitle(), bookDto.getGenre(), bookDto.getAuthor());
        return BookDto.transformDomainToDto(book);
    }

    @PutMapping(value = "/api/books/{id}")
    public BookDto editBook(@PathVariable(value = "id") long id, @RequestBody BookDto bookDto) {
        var book = bookService.changeBook(id, bookDto.getTitle(), bookDto.getGenre(), bookDto.getAuthor());
        return BookDto.transformDomainToDto(book);
    }

    @DeleteMapping(value = "/api/books/{id}")
    public BookDto deleteBook(@PathVariable(value = "id") long id) {
        var book = bookService.deleteBookById(id);
        return BookDto.transformDomainToDto(book);
    }

    @DeleteMapping(value = "/api/books")
    public long deleteAllBooks() {
        return bookService.deleteAllBooks();
    }
}
