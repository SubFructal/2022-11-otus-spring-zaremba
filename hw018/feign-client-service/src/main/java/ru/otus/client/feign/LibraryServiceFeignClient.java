package ru.otus.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.otus.client.dto.AuthorDto;
import ru.otus.client.dto.BookDto;
import ru.otus.client.dto.CommentDto;
import ru.otus.client.dto.GenreDto;

import java.util.List;

@FeignClient(name = "library-service")
public interface LibraryServiceFeignClient {

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

    @GetMapping(value = "/api/books/{id}/comments")
    List<CommentDto> findAllCommentsForSpecificBook(@PathVariable(value = "id") long id);

    @GetMapping(value = "/api/authors")
    List<AuthorDto> findAllAuthors();

    @GetMapping(value = "/api/genres")
    List<GenreDto> findAllGenres();
}
