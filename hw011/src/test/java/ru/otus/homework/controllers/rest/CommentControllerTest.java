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
import ru.otus.homework.controllers.rest.dto.CommentDto;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;
import ru.otus.homework.models.Genre;
import ru.otus.homework.repositories.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@DisplayName("CommentController")
@SpringBootTest
@AutoConfigureWebTestClient
class CommentControllerTest {

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("должен возвращать корректный список комментариев для конкретной книги")
    @Test
    void shouldReturnCorrectCommentsListForSpecificBook() throws Exception {
        var expectedBook = new Book("1", "firstBook", new Genre("1", "firstGenre"),
                new Author("1", "firstAuthor"));
        var expectedComments = List.of(
                new Comment("1", "firstComment", expectedBook),
                new Comment("1", "firstComment", expectedBook)
        );
        given(commentRepository.findAllByBookId(expectedBook.getId(), Sort.by(Sort.Direction.ASC, "id")))
                .willReturn(Flux.fromIterable(expectedComments));

        List<CommentDto> expectedResult = expectedComments.stream()
                .map(CommentDto::transformDomainToDto)
                .collect(Collectors.toList());

        webTestClient.get().uri("/api/books/{id}/comments", expectedBook.getId())
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(objectMapper.writeValueAsString(expectedResult));
        verify(commentRepository, times(1))
                .findAllByBookId(expectedBook.getId(), Sort.by(Sort.Direction.ASC, "id"));
    }
}