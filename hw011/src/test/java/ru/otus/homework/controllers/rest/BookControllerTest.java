package ru.otus.homework.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.controllers.rest.dto.BookDto;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;
import ru.otus.homework.repositories.AuthorRepository;
import ru.otus.homework.repositories.BookRepository;
import ru.otus.homework.repositories.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@DisplayName("BookController")
@SpringBootTest
@AutoConfigureWebTestClient
class BookControllerTest {

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("должен возвращать корректный список всех книг")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        var expectedFirstAuthor = new Author("1", "firstAuthor");
        var expectedSecondAuthor = new Author("1", "secondAuthor");
        var expectedFirstGenre = new Genre("1", "firstGenre");
        var expectedSecondGenre = new Genre("1", "secondGenre");

        var expectedBooks = List.of(
                new Book("1", "firstBook", expectedFirstGenre, expectedFirstAuthor),
                new Book("2", "secondBook", expectedSecondGenre, expectedSecondAuthor)
        );
        given(bookRepository.findAll(Sort.by(Sort.Direction.ASC, "id")))
                .willReturn(Flux.fromIterable(expectedBooks));

        List<BookDto> expectedResult = expectedBooks.stream()
                .map(BookDto::transformDomainToDto)
                .collect(Collectors.toList());

        webTestClient.get().uri("/api/books")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(objectMapper.writeValueAsString(expectedResult));
        verify(bookRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @DisplayName("должен возвращать книгу по идентификатору")
    @Test
    void shouldReturnBookById() throws Exception {
        var expectedBook = new Book("1", "firstBook",
                new Genre("1", "firstGenre"),
                new Author("1", "firstAuthor"));

        given(bookRepository.findById(expectedBook.getId())).willReturn(Mono.just(expectedBook));
        var expectedResult = BookDto.transformDomainToDto(expectedBook);

        webTestClient.get().uri("/api/books/{id}", expectedBook.getId())
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(objectMapper.writeValueAsString(expectedResult));
        verify(bookRepository, times(1)).findById(expectedBook.getId());
    }

    @DisplayName("должен удалять книгу по идентификатору")
    @Test
    void shouldDeleteBookById() {
        var bookId = "1";
        given(bookRepository.deleteById(eq(bookId))).willReturn(Mono.empty());
        given(commentRepository.deleteAllByBookId(eq(bookId))).willReturn(Mono.empty());

        webTestClient.delete().uri("/api/books/{id}", bookId)
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk();
        verify(bookRepository, times(1)).deleteById(eq(bookId));
        verify(commentRepository, times(1)).deleteAllByBookId(eq(bookId));
    }

    @DisplayName("должен удалять все книги")
    @Test
    void shouldDeleteAllBooks() {
        given(bookRepository.deleteAll()).willReturn(Mono.empty());
        given(commentRepository.deleteAll()).willReturn(Mono.empty());

        webTestClient.delete().uri("/api/books")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk();
        verify(bookRepository, times(1)).deleteAll();
        verify(commentRepository, times(1)).deleteAll();
    }

    @DisplayName("должен выбрасывать исключение при поиске несуществующей книги")
    @Test
    void shouldReturnExpectedErrorWhenEntityNotFound() {
        given(bookRepository.findById("1")).willReturn(Mono.empty());

        webTestClient.get().uri("/api/books/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).isEqualTo("Не найдена книга с идентификатором 1");
    }
}