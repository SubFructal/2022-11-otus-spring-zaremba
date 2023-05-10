package ru.otus.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.otus.client.dto.BookDto;

import java.util.List;

@FeignClient(name = "libraryServiceBookFeignClient", url = "http://localhost:9001")
public interface LibraryServiceBookFeignClient {

    @GetMapping(value = "/api/books")
    List<BookDto> findAllBooks();

    @GetMapping(value = "/api/books", params = "authorName")
    List<BookDto> findAllBooksByAuthor(@RequestParam(value = "authorName") String authorName);

    @GetMapping(value = "/api/books", params = "genreName")
    List<BookDto> findAllBooksByGenre(@RequestParam(value = "genreName") String genreName);

    @GetMapping(value = "/api/books/{id}")
    BookDto findBookById(@PathVariable(value = "id") long bookId);

    @PostMapping(value = "/api/books")
    BookDto createNewBook(@RequestBody BookDto bookDto);

    @PutMapping(value = "/api/books/{id}")
    BookDto editBook(@PathVariable(value = "id") long id, @RequestBody BookDto bookDto);

    @DeleteMapping(value = "/api/books/{id}")
    BookDto deleteBook(@PathVariable(value = "id") long id);

    @DeleteMapping(value = "/api/books")
    long deleteAllBooks();
}
