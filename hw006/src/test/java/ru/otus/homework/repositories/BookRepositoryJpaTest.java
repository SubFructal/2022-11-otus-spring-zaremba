package ru.otus.homework.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс BookRepositoryJpa")
@DataJpaTest
@Import(value = {BookRepositoryJpa.class})
class BookRepositoryJpaTest {

    private static final int EXPECTED_BOOKS_COUNT = 3;
    private static final long EXPECTED_BOOKS_COUNT_AFTER_CLEANING = 0;
    private static final String NEW_BOOK_TITLE = "Книга_04";
    private static final long TEST_GENRE_ID = 1;
    private static final long TEST_AUTHOR_ID = 1;
    private static final long EXISTING_BOOK_ID = 1;
    private static final String UPDATED_BOOK_TITLE = "Книга_04_updated";

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private BookRepositoryJpa bookRepositoryJpa;

    @DisplayName("должен возвращать ожидаемое количество книг в БД")
    @Test
    void shouldReturnExpectedBooksCount() {
        var actualBooksCount = bookRepositoryJpa.count();
        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("должен добавлять книгу в БД")
    @Test
    void shouldInsertBook() {
        var expectedBook = new Book();
        expectedBook.setTitle(NEW_BOOK_TITLE);
        var testGenre = testEntityManager.find(Genre.class, TEST_GENRE_ID);
        expectedBook.setGenre(testGenre);
        var testAuthor = testEntityManager.find(Author.class, TEST_AUTHOR_ID);
        expectedBook.setAuthor(testAuthor);

        bookRepositoryJpa.save(expectedBook);
        assertThat(expectedBook.getId()).isGreaterThan(0);

        var actualBook = testEntityManager.find(Book.class, expectedBook.getId());

        assertThat(actualBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);

        assertThat(actualBook)
                .matches(book -> book.getId() == 4)
                .matches(book -> book.getTitle().equals(NEW_BOOK_TITLE))
                .matches(book -> book.getGenre() != null
                        && book.getGenre().getId() == TEST_GENRE_ID)
                .matches(book -> book.getAuthor() != null
                        && book.getAuthor().getId() == TEST_AUTHOR_ID);
    }

    @DisplayName("должен изменять имеющуюся в БД книгу без отключения объекта книги от контекста")
    @Test
    void shouldUpdateBook() {
        var book = testEntityManager.find(Book.class, EXISTING_BOOK_ID);
        book.setTitle(UPDATED_BOOK_TITLE);
        testEntityManager.flush();
        var updatedBook = testEntityManager.find(Book.class, EXISTING_BOOK_ID);

        assertThat(updatedBook.getTitle()).isEqualTo(UPDATED_BOOK_TITLE);
    }

    @DisplayName("должен изменять имеющуюся в БД книгу при отключении объекта книги от контекста")
    @Test
    void shouldUpdateBookWithDetaching() {
        var book = testEntityManager.find(Book.class, EXISTING_BOOK_ID);
        testEntityManager.detach(book);
        book.setTitle(UPDATED_BOOK_TITLE);
        bookRepositoryJpa.save(book);
        var updatedBook = testEntityManager.find(Book.class, EXISTING_BOOK_ID);

        assertThat(updatedBook.getTitle()).isEqualTo(UPDATED_BOOK_TITLE);
    }

    @DisplayName("должен возвращать ожидаемую книгу по идентификатору")
    @Test
    void shouldReturnExpectedBookById() {
        var optionalActualBook = bookRepositoryJpa.getById(EXISTING_BOOK_ID);
        var expectedBook = testEntityManager.find(Book.class, EXISTING_BOOK_ID);

        assertThat(optionalActualBook)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен возвращать ожидаемый список книг")
    @Test
    void shouldReturnExpectedBooksList() {
        var books = bookRepositoryJpa.getAll();

        assertThat(books).isNotNull().hasSize(EXPECTED_BOOKS_COUNT)
                .allMatch(book -> !book.getTitle().equals(""))
                .allMatch(book -> book.getGenre() != null)
                .allMatch(book -> book.getAuthor() != null)
                .containsOnlyOnce(testEntityManager.find(Book.class, EXISTING_BOOK_ID));
    }

    @DisplayName("должен удалять заданную книгу по ее идентификатору")
    @Test
    void shouldDeleteBookById() {
        var existingBook = testEntityManager.find(Book.class, EXISTING_BOOK_ID);
        assertThat(existingBook).isNotNull();

        bookRepositoryJpa.deleteById(EXISTING_BOOK_ID);

        var deletedBook = testEntityManager.find(Book.class, EXISTING_BOOK_ID);
        assertThat(deletedBook).isNull();
    }

    @DisplayName("должен удалять все книги из БД")
    @Test
    void shouldDeleteAllBooks() {
        var actualCountBeforeCleaning = bookRepositoryJpa.count();
        assertThat(actualCountBeforeCleaning).isEqualTo(EXPECTED_BOOKS_COUNT);

        bookRepositoryJpa.deleteAll();

        var actualCountAfterCleaning = bookRepositoryJpa.count();
        assertThat(actualCountAfterCleaning).isEqualTo(EXPECTED_BOOKS_COUNT_AFTER_CLEANING);
    }
}