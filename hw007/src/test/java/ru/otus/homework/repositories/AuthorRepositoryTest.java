package ru.otus.homework.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.homework.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuthorRepository")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AuthorRepositoryTest {

    private static final int EXPECTED_AUTHORS_COUNT = 3;
    private static final long EXPECTED_AUTHORS_COUNT_AFTER_CLEANING = 0;
    private static final String NEW_AUTHOR_NAME = "Автор_04";
    private static final long EXISTING_AUTHOR_ID = 1;
    private static final String EXISTING_AUTHOR_NAME = "Автор_01";
    private static final String UPDATED_AUTHOR_NAME = "Автор_01_updated";

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("должен возвращать ожидаемое количество авторов в БД")
    @Test
    void shouldReturnExpectedAuthorsCount() {
        var actualAuthorsCount = authorRepository.count();
        assertThat(actualAuthorsCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("должен добавлять автора в БД")
    @Test
    void shouldInsertAuthor() {
        var expectedAuthor = new Author();
        expectedAuthor.setName(NEW_AUTHOR_NAME);

        authorRepository.save(expectedAuthor);
        assertThat(expectedAuthor.getId()).isGreaterThan(0);

        var actualAuthor = testEntityManager.find(Author.class, expectedAuthor.getId());

        assertThat(actualAuthor)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);

        assertThat(actualAuthor)
                .matches(author -> author.getId() == 4)
                .matches(author -> author.getName().equals(NEW_AUTHOR_NAME));
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
        authorRepository.save(author);
        var updatedAuthor = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);

        assertThat(updatedAuthor.getName()).isEqualTo(UPDATED_AUTHOR_NAME);
    }

    @DisplayName("должен возвращать ожидаемого автора по идентификатору")
    @Test
    void shouldReturnExpectedAuthorById() {
        var expectedAuthor = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);
        var optionalActualAuthor = authorRepository.findById(EXISTING_AUTHOR_ID);

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
        var optionalActualAuthor = authorRepository.findByName(EXISTING_AUTHOR_NAME);

        assertThat(optionalActualAuthor)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен возвращать ожидаемый список всех авторов, отсортированный по id в возрастающем порядке")
    @Test
    void shouldReturnExpectedAuthorsList() {
        var expectedAuthors = List.of(
                testEntityManager.find(Author.class, 1L),
                testEntityManager.find(Author.class, 2L),
                testEntityManager.find(Author.class, 3L)
        );
        var actualAuthors = authorRepository.findAll();

        assertThat(actualAuthors).isNotNull().hasSize(EXPECTED_AUTHORS_COUNT)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedAuthors);
    }

    @DisplayName("должен удалять заданного автора по его идентификатору")
    @Test
    void shouldDeleteAuthorById() {
        var existingAuthor = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);
        assertThat(existingAuthor).isNotNull();

        authorRepository.deleteById(EXISTING_AUTHOR_ID);

        var deletedAuthor = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);
        assertThat(deletedAuthor).isNull();
    }

    @DisplayName("должен удалять всех авторов из БД и возвращать количество удаленных авторов")
    @Test
    void shouldDeleteAllAuthors() {
        var actualCountBeforeCleaning = authorRepository.count();
        assertThat(actualCountBeforeCleaning).isEqualTo(EXPECTED_AUTHORS_COUNT);

        var deletedAuthorsCount = authorRepository.deleteAllCustom();

        assertThat(deletedAuthorsCount).isEqualTo(EXPECTED_AUTHORS_COUNT);

        var actualCountAfterCleaning = authorRepository.count();
        assertThat(actualCountAfterCleaning).isEqualTo(EXPECTED_AUTHORS_COUNT_AFTER_CLEANING);
    }
}