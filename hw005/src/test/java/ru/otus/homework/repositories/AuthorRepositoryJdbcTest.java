package ru.otus.homework.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс AuthorRepositoryJdbc")
@JdbcTest
@Import(value = {AuthorRepositoryJdbc.class})
class AuthorRepositoryJdbcTest {

    private static final long EXPECTED_AUTHORS_COUNT = 3;
    private static final long EXPECTED_AUTHORS_COUNT_AFTER_CLEANING = 0;
    private static final long EXISTING_AUTHOR_ID = 1;
    private static final String EXISTING_AUTHOR_NAME = "Автор_01";
    private static final String NEW_AUTHOR_NAME = "Автор_04";
    private static final String UPDATED_AUTHOR_NAME = "Автор_01_updated";

    @Autowired
    private AuthorRepositoryJdbc authorRepositoryJdbc;

    @DisplayName("должен возвращать ожидаемое число авторов в БД")
    @Test
    void shouldReturnExpectedAuthorsCount() {
        var actualAuthorsCount = authorRepositoryJdbc.count();
        assertThat(actualAuthorsCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("должен добавлять автора в БД")
    @Test
    void shouldInsertAuthor() {
        var expectedAuthor = new Author();
        expectedAuthor.setName(NEW_AUTHOR_NAME);

        authorRepositoryJdbc.insert(expectedAuthor);
        var actualAuthor = authorRepositoryJdbc.getById(expectedAuthor.getId());

        assertThat(actualAuthor)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен изменять имеющегося в БД автора")
    @Test
    void shouldUpdateAuthor() {
        var expectedAuthor = new Author(EXISTING_AUTHOR_ID, UPDATED_AUTHOR_NAME);

        authorRepositoryJdbc.update(expectedAuthor);
        var actualAuthor = authorRepositoryJdbc.getById(expectedAuthor.getId());

        assertThat(actualAuthor)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен возвращать ожидаемого автора по его идентификатору")
    @Test
    void shouldReturnExpectedAuthorById() {
        var expectedAuthor = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);

        var actualAuthor = authorRepositoryJdbc.getById(expectedAuthor.getId());

        assertThat(actualAuthor)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен возвращать ожидаемого автора по имени")
    @Test
    void shouldReturnExpectedAuthorByName() {
        var expectedAuthor = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);

        var actualAuthor = authorRepositoryJdbc.getByName(expectedAuthor.getName());

        assertThat(actualAuthor)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен возвращать ожидаемый список авторов")
    @Test
    void shouldReturnExpectedAuthorList() {
        var expectedAuthors = List.of(
                new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME),
                new Author(EXISTING_AUTHOR_ID + 1, EXISTING_AUTHOR_NAME.replace('1', '2')),
                new Author(EXISTING_AUTHOR_ID + 2, EXISTING_AUTHOR_NAME.replace('1', '3'))
        );

        var actualAuthors = authorRepositoryJdbc.getAll();

        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedAuthors);
    }

    @DisplayName("должен удалять заданного автора по его идентификатору")
    @Test
    void shouldDeleteAuthorById() {
        var existingAuthor = authorRepositoryJdbc.getById(EXISTING_AUTHOR_ID);
        assertThat(existingAuthor).isNotEmpty();

        authorRepositoryJdbc.deleteById(EXISTING_AUTHOR_ID);

        var deletedAuthor = authorRepositoryJdbc.getById(EXISTING_AUTHOR_ID);
        assertThat(deletedAuthor).isEmpty();
    }

    @DisplayName("должен удалять всех авторов из БД")
    @Test
    void shouldDeleteAllAuthors() {
        var actualCountBeforeCleaning = authorRepositoryJdbc.count();
        assertThat(actualCountBeforeCleaning).isEqualTo(EXPECTED_AUTHORS_COUNT);

        authorRepositoryJdbc.clean();

        var actualCountAfterCleaning = authorRepositoryJdbc.count();
        assertThat(actualCountAfterCleaning).isEqualTo(EXPECTED_AUTHORS_COUNT_AFTER_CLEANING);
    }
}