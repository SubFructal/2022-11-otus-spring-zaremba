package ru.otus.homework.controllers.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("GenrePagesController")
@SpringBootTest
@AutoConfigureWebTestClient
class GenrePagesControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("должен отображать страницу со списком всех жанров")
    @Test
    void shouldDisplayListAllGenresPage() {
        webTestClient.get().uri("/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).value(body -> assertThat(body).contains("Жанры в БД:"));
    }
}