package ru.otus.homework.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс GenreRepositoryJpa")
@DataJpaTest
@Import(value = {GenreRepositoryJpa.class})
class GenreRepositoryJpaTest {

    private static final int EXPECTED_GENRES_COUNT = 2;
    private static final long EXPECTED_GENRES_COUNT_AFTER_CLEANING = 0;
    private static final String NEW_GENRE_NAME = "Жанр_03";
    private static final long EXISTING_GENRE_ID = 1;
    private static final String EXISTING_GENRE_NAME = "Жанр_01";
    private static final String UPDATED_GENRE_NAME = "Жанр_01_updated";

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private GenreRepositoryJpa genreRepositoryJpa;

    @DisplayName("должен возвращать ожидаемое количество жанров в БД")
    @Test
    void shouldReturnExpectedGenresCount() {
        var actualGenresCount = genreRepositoryJpa.count();
        assertThat(actualGenresCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("должен добавлять жанр в БД")
    @Test
    void shouldInsertGenre() {
        var expectedGenre = new Genre();
        expectedGenre.setGenreName(NEW_GENRE_NAME);

        genreRepositoryJpa.save(expectedGenre);
        assertThat(expectedGenre.getId()).isGreaterThan(0);

        var actualGenre = testEntityManager.find(Genre.class, expectedGenre.getId());

        assertThat(actualGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);

        assertThat(actualGenre)
                .matches(genre -> genre.getId() == 3)
                .matches(genre -> genre.getGenreName().equals(NEW_GENRE_NAME));
    }

    @DisplayName("должен изменять имеющийся в БД жанр без отключения объекта жанра от контекста")
    @Test
    void shouldUpdateGenre() {
        var genre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        genre.setGenreName(UPDATED_GENRE_NAME);
        testEntityManager.flush();
        var updatedGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);

        assertThat(updatedGenre.getGenreName()).isEqualTo(UPDATED_GENRE_NAME);
    }

    @DisplayName("должен изменять имеющийся в БД жанр при отключении объекта жанра от контекста")
    @Test
    void shouldUpdateGenreWithDetaching() {
        var genre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        testEntityManager.detach(genre);
        genre.setGenreName(UPDATED_GENRE_NAME);
        genreRepositoryJpa.save(genre);
        var updatedGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);

        assertThat(updatedGenre.getGenreName()).isEqualTo(UPDATED_GENRE_NAME);
    }

    @DisplayName("должен возвращать ожидаемый жанр по идентификатору")
    @Test
    void shouldReturnExpectedGenreById() {
        var expectedGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        var optionalActualGenre = genreRepositoryJpa.getById(EXISTING_GENRE_ID);

        assertThat(optionalActualGenre)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен возвращать ожидаемый жанр по имени")
    @Test
    void shouldReturnExpectedGenreByName() {
        var expectedGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        var optionalActualGenre = genreRepositoryJpa.getByName(EXISTING_GENRE_NAME);

        assertThat(optionalActualGenre)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен возвращать ожидаемый список жанров")
    @Test
    void shouldReturnExpectedGenresList() {
        var genres = genreRepositoryJpa.getAll();

        assertThat(genres).isNotNull().hasSize(EXPECTED_GENRES_COUNT)
                .allMatch(genre -> !genre.getGenreName().equals(""))
                .containsOnlyOnce(testEntityManager.find(Genre.class, EXISTING_GENRE_ID));
    }

    @DisplayName("должен удалять заданный жанр по его идентификатору")
    @Test
    void shouldDeleteGenreById() {
        var existingGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        assertThat(existingGenre).isNotNull();

        genreRepositoryJpa.deleteById(EXISTING_GENRE_ID);

        var deletedGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        assertThat(deletedGenre).isNull();
    }

    @DisplayName("должен удалять все жанры из БД")
    @Test
    void shouldDeleteAllGenres() {
        var actualCountBeforeCleaning = genreRepositoryJpa.count();
        assertThat(actualCountBeforeCleaning).isEqualTo(EXPECTED_GENRES_COUNT);

        genreRepositoryJpa.deleteAll();

        var actualCountAfterCleaning = genreRepositoryJpa.count();
        assertThat(actualCountAfterCleaning).isEqualTo(EXPECTED_GENRES_COUNT_AFTER_CLEANING);
    }
}