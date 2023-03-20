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
import ru.otus.homework.controllers.rest.dto.GenreDto;
import ru.otus.homework.models.Genre;
import ru.otus.homework.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("GenreController")
@SpringBootTest
@AutoConfigureWebTestClient
class GenreControllerTest {

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("должен возвращать корректный список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        var expectedGenres = List.of(
                new Genre("1", "firstGenre"),
                new Genre("2", "secondGenre")
        );
        given(genreRepository.findAll(Sort.by(Sort.Direction.ASC, "id")))
                .willReturn(Flux.fromIterable(expectedGenres));

        List<GenreDto> expectedResult = expectedGenres.stream()
                .map(GenreDto::transformDomainToDto)
                .collect(Collectors.toList());

        webTestClient.get().uri("/api/genres")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(objectMapper.writeValueAsString(expectedResult));
        verify(genreRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}