package ru.otus.library.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.controllers.rest.dto.BookDto;
import ru.otus.library.models.Author;
import ru.otus.library.models.Book;
import ru.otus.library.models.Genre;
import ru.otus.library.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BookController")
@WebMvcTest(controllers = {BookController.class})
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("должен возвращать корректный список всех книг")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        var expectedFirstAuthor = new Author(1, "firstAuthor");
        var expectedSecondAuthor = new Author(1, "secondAuthor");
        var expectedFirstGenre = new Genre(1, "firstGenre");
        var expectedSecondGenre = new Genre(1, "secondGenre");

        var expectedBooks = List.of(
                new Book(1, "firstBook", expectedFirstGenre, expectedFirstAuthor),
                new Book(2, "secondBook", expectedSecondGenre, expectedSecondAuthor)
        );

        given(bookService.getAllBooks()).willReturn(expectedBooks);
        List<BookDto> expectedResult = expectedBooks.stream()
                .map(BookDto::transformDomainToDto)
                .collect(Collectors.toList());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(bookService, times(1)).getAllBooks();
    }

    @DisplayName("должен возвращать корректный список всех книг конкретного автора")
    @Test
    void shouldReturnCorrectBooksListForSpecificAuthor() throws Exception {
        var expectedAuthor = new Author(1, "firstAuthor");
        var expectedFirstGenre = new Genre(1, "firstGenre");
        var expectedSecondGenre = new Genre(1, "secondGenre");
        var expectedBooks = List.of(
                new Book(1, "firstBook", expectedFirstGenre, expectedAuthor),
                new Book(2, "secondBook", expectedSecondGenre, expectedAuthor)
        );

        given(bookService.findAllBooksByAuthor(expectedAuthor.getName())).willReturn(expectedBooks);
        List<BookDto> expectedResult = expectedBooks.stream()
                .map(BookDto::transformDomainToDto)
                .collect(Collectors.toList());

        mockMvc.perform(get("/api/books").param("authorName", expectedAuthor.getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(bookService, times(1)).findAllBooksByAuthor(expectedAuthor.getName());
    }

    @DisplayName("должен возвращать корректный список всех книг конкретного жанра")
    @Test
    void shouldReturnCorrectBooksListForSpecificGenre() throws Exception {
        var expectedGenre = new Genre(1, "firstGenre");
        var expectedFirstAuthor = new Author(1, "firstAuthor");
        var expectedSecondAuthor = new Author(2, "secondAuthor");
        var expectedBooks = List.of(
                new Book(1, "firstBook", expectedGenre, expectedFirstAuthor),
                new Book(2, "secondBook", expectedGenre, expectedSecondAuthor)
        );

        given(bookService.findAllBooksByGenre(expectedGenre.getGenreName())).willReturn(expectedBooks);
        List<BookDto> expectedResult = expectedBooks.stream()
                .map(BookDto::transformDomainToDto)
                .collect(Collectors.toList());

        mockMvc.perform(get("/api/books").param("genreName", expectedGenre.getGenreName()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(bookService, times(1)).findAllBooksByGenre(expectedGenre.getGenreName());
    }

    @DisplayName("должен возвращать книгу по идентификатору")
    @Test
    void shouldReturnBookById() throws Exception {
        var expectedBook = new Book(1, "firstBook",
                new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));

        given(bookService.findBookById(expectedBook.getId())).willReturn(expectedBook);
        var expectedResult = BookDto.transformDomainToDto(expectedBook);

        mockMvc.perform(get("/api/books/{id}", expectedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(bookService, times(1)).findBookById(expectedBook.getId());
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldCorrectSaveNewBook() throws Exception {
        var expectedBook = new Book(1, "firstBook",
                new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));

        given(bookService.addBook(anyString(), anyString(), anyString())).willReturn(expectedBook);
        var expectedResult = objectMapper.writeValueAsString(BookDto.transformDomainToDto(expectedBook));

        mockMvc.perform(post("/api/books")
                        .contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
        verify(bookService, times(1)).addBook(anyString(), anyString(), anyString());
    }

    @DisplayName("должен обновлять книгу по идентификатору")
    @Test
    void shouldUpdateBookById() throws Exception {
        var expectedBook = new Book(1, "firstBook",
                new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));
        given(bookService.changeBook(anyLong(), anyString(), anyString(), anyString())).willReturn(expectedBook);
        var expectedResult = objectMapper.writeValueAsString(BookDto.transformDomainToDto(expectedBook));

        mockMvc.perform(put("/api/books/{id}", expectedBook.getId())
                        .contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
        verify(bookService, times(1)).changeBook(anyLong(), anyString(), anyString(), anyString());
    }

    @DisplayName("должен удалять книгу по идентификатору")
    @Test
    void shouldDeleteBookById() throws Exception {
        var expectedBook = new Book(1, "firstBook",
                new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));

        given(bookService.deleteBookById(expectedBook.getId())).willReturn(expectedBook);
        var expectedResult = BookDto.transformDomainToDto(expectedBook);

        mockMvc.perform(delete("/api/books/{id}", expectedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(bookService, times(1)).deleteBookById(expectedBook.getId());
    }

    @DisplayName("должен удалять все книги")
    @Test
    void shouldDeleteAllBooks() throws Exception {
        var expectedFirstAuthor = new Author(1, "firstAuthor");
        var expectedSecondAuthor = new Author(1, "secondAuthor");
        var expectedFirstGenre = new Genre(1, "firstGenre");
        var expectedSecondGenre = new Genre(1, "secondGenre");

        var expectedBooks = List.of(
                new Book(1, "firstBook", expectedFirstGenre, expectedFirstAuthor),
                new Book(2, "secondBook", expectedSecondGenre, expectedSecondAuthor)
        );

        var expectedResult = (long) expectedBooks.size();
        given(bookService.deleteAllBooks()).willReturn(expectedResult);

        mockMvc.perform(delete("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(bookService, times(1)).deleteAllBooks();
    }
}