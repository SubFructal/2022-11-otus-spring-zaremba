package ru.otus.homework.controllers.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("AuthorPagesController")
@SpringBootTest
@AutoConfigureWebTestClient
class AuthorPagesControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("должен отображать страницу со списком всех авторов")
    @Test
    void shouldDisplayListAllAuthorsPage() {
        webTestClient.get().uri("/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).value(body -> assertThat(body).contains("Авторы в БД:"));
    }
}