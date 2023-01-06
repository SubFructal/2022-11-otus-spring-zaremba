package ru.otus.homework.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс GenreRepositoryJdbc")
@JdbcTest
@Import(value = {GenreRepositoryJdbc.class})
class GenreRepositoryJdbcTest {

    private static final long EXPECTED_GENRES_COUNT = 2;
    private static final long EXPECTED_GENRES_COUNT_AFTER_CLEANING = 0;
    private static final long EXISTING_GENRE_ID = 1;
    private static final String EXISTING_GENRE_NAME = "Жанр_01";
    private static final String NEW_GENRE_NAME = "Жанр_03";
    private static final String UPDATED_GENRE_NAME = "Жанр_01_updated";

    @Autowired
    private GenreRepositoryJdbc genreRepositoryJdbc;

    @DisplayName("должен возвращать ожидаемое количество жанров в БД")
    @Test
    void shouldReturnExpectedGenresCount() {
        var actualGenresCount = genreRepositoryJdbc.count();
        assertThat(actualGenresCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("должен добавлять жанр в БД")
    @Test
    void shouldInsertGenre() {
        var expectedGenre = new Genre();
        expectedGenre.setGenreName(NEW_GENRE_NAME);

        genreRepositoryJdbc.insert(expectedGenre);
        var actualGenre = genreRepositoryJdbc.getById(expectedGenre.getId());

        assertThat(actualGenre)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен изменять имеющийся в БД жанр")
    @Test
    void shouldUpdateGenre() {
        var expectedGenre = new Genre(EXISTING_GENRE_ID, UPDATED_GENRE_NAME);

        genreRepositoryJdbc.update(expectedGenre);
        var actualGenre = genreRepositoryJdbc.getById(expectedGenre.getId());

        assertThat(actualGenre)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен возвращать ожидаемый жанр по идентификатору")
    @Test
    void shouldReturnExpectedGenreById() {
        var expectedGenre = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);

        var actualGenre = genreRepositoryJdbc.getById(expectedGenre.getId());

        assertThat(actualGenre)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен возвращать ожидаемый жанр по имени")
    @Test
    void shouldReturnExpectedGenreByName() {
        var expectedGenre = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);

        var actualGenre = genreRepositoryJdbc.getByName(expectedGenre.getGenreName());

        assertThat(actualGenre)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен возвращать ожидаемый список жанров")
    @Test
    void shouldReturnExpectedGenresList() {
        var expectedGenres = List.of(
                new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME),
                new Genre(EXISTING_GENRE_ID + 1, EXISTING_GENRE_NAME.replace('1', '2'))
        );

        var actualGenres = genreRepositoryJdbc.getAll();

        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedGenres);
    }

    @DisplayName("должен удалять заданный жанр по его идентификатору")
    @Test
    void shouldDeleteGenreById() {
        var existingGenre = genreRepositoryJdbc.getById(EXISTING_GENRE_ID);
        assertThat(existingGenre).isNotEmpty();

        genreRepositoryJdbc.deleteById(EXISTING_GENRE_ID);

        var deletedGenre = genreRepositoryJdbc.getById(EXISTING_GENRE_ID);
        assertThat(deletedGenre).isEmpty();
    }

    @DisplayName("должен удалять все жанры из БД")
    @Test
    void shouldDeleteAllGenres() {
        var actualCountBeforeCleaning = genreRepositoryJdbc.count();
        assertThat(actualCountBeforeCleaning).isEqualTo(EXPECTED_GENRES_COUNT);

        genreRepositoryJdbc.clean();

        var actualCountAfterCleaning = genreRepositoryJdbc.count();
        assertThat(actualCountAfterCleaning).isEqualTo(EXPECTED_GENRES_COUNT_AFTER_CLEANING);
    }
}