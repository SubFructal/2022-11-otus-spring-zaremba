package ru.otus.client.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.client.dto.BookDto;
import ru.otus.client.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping(value = "/feign/api/books")
    public List<BookDto> findAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping(value = "/feign/api/books", params = "authorName")
    public List<BookDto> findAllBooksByAuthor(@RequestParam(value = "authorName") String authorName) {
        return bookService.findAllBooksByAuthor(authorName);
    }

    @GetMapping(value = "/feign/api/books", params = "genreName")
    public List<BookDto> findAllBooksByGenre(@RequestParam(value = "genreName") String genreName) {
        return bookService.findAllBooksByGenre(genreName);
    }

    @GetMapping(value = "/feign/api/books/{id}")
    public BookDto findBookById(@PathVariable(value = "id") long id) {
        return bookService.findBookById(id);
    }

    @PostMapping(value = "/feign/api/books")
    public BookDto createNewBook(@RequestBody BookDto bookDto) {
        return bookService.addBook(bookDto);
    }

    @PutMapping(value = "/feign/api/books/{id}")
    public BookDto editBook(@PathVariable(value = "id") long id, @RequestBody BookDto bookDto) {
        return bookService.changeBook(id, bookDto);
    }

    @DeleteMapping(value = "/feign/api/books/{id}")
    public BookDto deleteBook(@PathVariable(value = "id") long id) {
        return bookService.deleteBookById(id);
    }

    @DeleteMapping(value = "/feign/api/books")
    public String deleteAllBooks() {
        return bookService.deleteAllBooks();
    }
}
