package ru.otus.homework.controllers.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("BookPagesController")
@SpringBootTest
@AutoConfigureWebTestClient
class BookPagesControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("должен отображать страницу со списком всех книг")
    @Test
    void shouldDisplayListAllBooksPage() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).contains("Книги в БД:"))
                .value(body -> assertThat(body).contains("Добавить новую книгу:"));
    }

    @DisplayName("должен отображать страницу со списком всех книг одного автора")
    @Test
    void shouldDisplayListBooksByAuthorPage() {
        webTestClient.get().uri("/books-by-author")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).contains("Список всех книг одного автора:"));
    }

    @DisplayName("должен отображать страницу со списком всех книг одного жанра")
    @Test
    void shouldDisplayListBooksByGenrePage() {
        webTestClient.get().uri("/books-by-genre")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).contains("Список всех книг одного жанра:"));
    }

    @DisplayName("должен отображать страницу с просмотра и редактирования информации о книге")
    @Test
    void shouldDisplaySpecificBookPage() {
        webTestClient.get().uri("/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).contains("Информация о книге:"))
                .value(body -> assertThat(body).contains("Изменить книгу:"));
    }
}