package ru.otus.homework.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс AuthorRepositoryJpa")
@DataJpaTest
@Import(value = {AuthorRepositoryJpa.class})
class AuthorRepositoryJpaTest {

    private static final int EXPECTED_AUTHORS_COUNT = 3;
    private static final long EXPECTED_AUTHORS_COUNT_AFTER_CLEANING = 0;
    private static final String NEW_AUTHOR_NAME = "Автор_04";
    private static final long EXISTING_AUTHOR_ID = 1;
    private static final String EXISTING_AUTHOR_NAME = "Автор_01";
    private static final String UPDATED_AUTHOR_NAME = "Автор_01_updated";

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    @DisplayName("должен возвращать ожидаемое количество авторов в БД")
    @Test
    void shouldReturnExpectedAuthorsCount() {
        var actualAuthorsCount = authorRepositoryJpa.count();
        assertThat(actualAuthorsCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("должен добавлять автора в БД")
    @Test
    void shouldInsertAuthor() {
        var expectedAuthor = new Author();
        expectedAuthor.setName(NEW_AUTHOR_NAME);

        authorRepositoryJpa.save(expectedAuthor);
        assertThat(expectedAuthor.getId()).isGreaterThan(0);

        var actualAuthor = testEntityManager.find(Author.class, expectedAuthor.getId());

        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);

        assertThat(actualAuthor)
                .matches(author -> author.getId() == 4)
                .matches(author -> author.getName().equals(NEW_AUTHOR_NAME))
                .matches(author -> author.getBooks() == null);
    }

    @DisplayName("должен изменять имеющегося в БД автора без отключения объекта автора от контекста")
    @Test
    void shouldUpdateAuthor() {
        var author = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);
        author.setName(UPDATED_AUTHOR_NAME);
        testEntityManager.flush();
        var updatedAuthor = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);

        assertThat(updatedAuthor.getName()).isEqualTo(UPDATED_AUTHOR_NAME);
    }

    @DisplayName("должен изменять имеющегося в БД автора при отключении объекта автора от контекста")
    @Test
    void shouldUpdateAuthorWithDetaching() {
        var author = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);
        testEntityManager.detach(author);
        author.setName(UPDATED_AUTHOR_NAME);
        authorRepositoryJpa.save(author);
        var updatedAuthor = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);

        assertThat(updatedAuthor.getName()).isEqualTo(UPDATED_AUTHOR_NAME);
    }

    @DisplayName("должен возвращать ожидаемого автора по идентификатору")
    @Test
    void shouldReturnExpectedAuthorById() {
        var expectedAuthor = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);
        var optionalActualAuthor = authorRepositoryJpa.getById(EXISTING_AUTHOR_ID);

        assertThat(optionalActualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен возвращать ожидаемого автора по имени")
    @Test
    void shouldReturnExpectedAuthorByName() {
        var expectedAuthor = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);
        var optionalActualAuthor = authorRepositoryJpa.getByName(EXISTING_AUTHOR_NAME);

        assertThat(optionalActualAuthor)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен возвращать ожидаемый список авторов")
    @Test
    void shouldReturnExpectedAuthorsList() {
        var authors = authorRepositoryJpa.getAll();

        assertThat(authors).isNotNull().hasSize(EXPECTED_AUTHORS_COUNT)
                .allMatch(author -> !author.getName().equals(""))
                .allMatch(author -> author.getBooks() != null)
                .containsOnlyOnce(testEntityManager.find(Author.class, EXISTING_AUTHOR_ID));
    }

    @DisplayName("должен удалять заданного автора по его идентификатору")
    @Test
    void shouldDeleteAuthorById() {
        var existingAuthor = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);
        assertThat(existingAuthor).isNotNull();

        authorRepositoryJpa.deleteById(EXISTING_AUTHOR_ID);

        var deletedAuthor = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);
        assertThat(deletedAuthor).isNull();
    }

    @DisplayName("должен удалять всех авторов из БД")
    @Test
    void shouldDeleteAllAuthors() {
        var actualCountBeforeCleaning = authorRepositoryJpa.count();
        assertThat(actualCountBeforeCleaning).isEqualTo(EXPECTED_AUTHORS_COUNT);

        authorRepositoryJpa.deleteAll();

        var actualCountAfterCleaning = authorRepositoryJpa.count();
        assertThat(actualCountAfterCleaning).isEqualTo(EXPECTED_AUTHORS_COUNT_AFTER_CLEANING);
    }
}