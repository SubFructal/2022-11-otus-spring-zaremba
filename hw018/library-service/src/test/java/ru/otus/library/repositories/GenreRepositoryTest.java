package ru.otus.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import ru.otus.library.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GenreRepository")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class GenreRepositoryTest {

    private static final int EXPECTED_GENRES_COUNT = 2;
    private static final long EXPECTED_GENRES_COUNT_AFTER_CLEANING = 0;
    private static final String NEW_GENRE_NAME = "Жанр_03";
    private static final long EXISTING_GENRE_ID = 1;
    private static final String EXISTING_GENRE_NAME = "Жанр_01";
    private static final String UPDATED_GENRE_NAME = "Жанр_01_updated";

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("должен возвращать ожидаемое количество жанров в БД")
    @Test
    void shouldReturnExpectedGenresCount() {
        var actualGenresCount = genreRepository.count();
        assertThat(actualGenresCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("должен добавлять жанр в БД")
    @Test
    void shouldInsertGenre() {
        var expectedGenre = new Genre();
        expectedGenre.setGenreName(NEW_GENRE_NAME);

        genreRepository.save(expectedGenre);
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
        genreRepository.save(genre);
        var updatedGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);

        assertThat(updatedGenre.getGenreName()).isEqualTo(UPDATED_GENRE_NAME);
    }

    @DisplayName("должен возвращать ожидаемый жанр по идентификатору")
    @Test
    void shouldReturnExpectedGenreById() {
        var expectedGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        var optionalActualGenre = genreRepository.findById(EXISTING_GENRE_ID);

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
        var optionalActualGenre = genreRepository.findByGenreName(EXISTING_GENRE_NAME);

        assertThat(optionalActualGenre)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен возвращать ожидаемый список всех жанров, отсортированный по id в возрастающем порядке")
    @Test
    void shouldReturnExpectedGenresList() {
        var expectedGenres = List.of(
                testEntityManager.find(Genre.class, 1L),
                testEntityManager.find(Genre.class, 2L)
        );
        var actualGenres = genreRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        assertThat(actualGenres).isNotNull().hasSize(EXPECTED_GENRES_COUNT)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен удалять заданный жанр по его идентификатору")
    @Test
    void shouldDeleteGenreById() {
        var existingGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        assertThat(existingGenre).isNotNull();

        genreRepository.deleteById(EXISTING_GENRE_ID);

        var deletedGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        assertThat(deletedGenre).isNull();
    }

    @DisplayName("должен удалять все жанры из БД")
    @Test
    void shouldDeleteAllGenres() {
        var actualCountBeforeCleaning = genreRepository.count();
        assertThat(actualCountBeforeCleaning).isEqualTo(EXPECTED_GENRES_COUNT);

        genreRepository.deleteAll();

        var actualCountAfterCleaning = genreRepository.count();
        assertThat(actualCountAfterCleaning).isEqualTo(EXPECTED_GENRES_COUNT_AFTER_CLEANING);
    }
}