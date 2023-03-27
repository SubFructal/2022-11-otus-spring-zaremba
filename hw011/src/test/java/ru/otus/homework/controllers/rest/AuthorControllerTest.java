package ru.otus.homework.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import ru.otus.homework.controllers.rest.dto.AuthorDto;
import ru.otus.homework.models.Author;
import ru.otus.homework.repositories.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("AuthorController")
@SpringBootTest
@AutoConfigureWebTestClient
class AuthorControllerTest {

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("должен возвращать корректный список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() throws JsonProcessingException {
        var expectedAuthors = List.of(
                new Author("1", "firstAuthor"),
                new Author("2", "secondAuthor")
        );
        given(authorRepository.findAll(Sort.by(Sort.Direction.ASC, "id")))
                .willReturn(Flux.fromIterable(expectedAuthors));

        List<AuthorDto> expectedResult = expectedAuthors.stream()
                .map(AuthorDto::transformDomainToDto)
                .collect(Collectors.toList());

        webTestClient.get().uri("/api/authors")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(objectMapper.writeValueAsString(expectedResult));
        verify(authorRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}