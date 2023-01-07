package ru.otus.homework.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс BookRepositoryJdbc")
@JdbcTest
@Import(value = {BookRepositoryJdbc.class})
class BookRepositoryJdbcTest {

    @Autowired
    private BookRepositoryJdbc bookRepositoryJdbc;

    @DisplayName("должен возвращать ожидаемое количество книг в БД")
    @Test
    void shouldReturnExpectedBooksCount() {
        var actualBooksCount = bookRepositoryJdbc.count();
        assertThat(actualBooksCount).isEqualTo(3);
    }

    @DisplayName("должен добавлять книгу в БД")
    @Test
    void shouldInsertBook() {
        var expectedBook = new Book();
        expectedBook.setTitle("Книга_04");
        expectedBook.setGenre(new Genre(1, "Жанр_01"));
        expectedBook.setAuthor(new Author(1, "Автор_01"));

        bookRepositoryJdbc.insert(expectedBook);
        var actualBook = bookRepositoryJdbc.getById(expectedBook.getId());

        assertThat(actualBook)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен изменять имеющуюся в БД книгу")
    @Test
    void shouldUpdateBook() {
        var expectedBook = new Book();
        expectedBook.setId(1);
        expectedBook.setTitle("Книга_01_updated");
        expectedBook.setGenre(new Genre(2, "Жанр_02"));
        expectedBook.setAuthor(new Author(1, "Автор_01"));

        bookRepositoryJdbc.update(expectedBook);
        var actualBook = bookRepositoryJdbc.getById(expectedBook.getId());

        assertThat(actualBook)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен возвращать ожидаемую книгу по идентификатору")
    @Test
    void shouldReturnExpectedBookById() {
        var expectedBook = new Book();
        expectedBook.setId(1);
        expectedBook.setTitle("Книга_01");
        expectedBook.setGenre(new Genre(1, "Жанр_01"));
        expectedBook.setAuthor(new Author(3, "Автор_03"));

        var actualBook = bookRepositoryJdbc.getById(expectedBook.getId());

        assertThat(actualBook)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен возвращать ожидаемый список книг")
    @Test
    void shouldReturnExpectedBooksList() {
        var expectedBooks = List.of(
                new Book(1, "Книга_01", new Genre(1, "Жанр_01"),
                        new Author(3, "Автор_03")),
                new Book(2, "Книга_02", new Genre(2, "Жанр_02"),
                        new Author(1, "Автор_01")),
                new Book(3, "Книга_03", new Genre(1, "Жанр_01"),
                        new Author(2, "Автор_02"))
        );

        var actualBooks = bookRepositoryJdbc.getAll();

        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedBooks);
    }

    @DisplayName("должен возвращать ожидаемый список книг одного жанра")
    @Test
    void shouldReturnBooksListByExpectedGenre() {
        var expectedGenre = new Genre(1, "Жанр_01");
        var expectedBooksByGenre = List.of(
                new Book(1, "Книга_01", expectedGenre, new Author(3, "Автор_03")),
                new Book(3, "Книга_03", expectedGenre, new Author(2, "Автор_02"))
        );

        var actualBooksByGenre = bookRepositoryJdbc.getAllByGenre(expectedGenre.getGenreName());

        assertThat(actualBooksByGenre)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedBooksByGenre);
    }

    @DisplayName("должен возвращать ожидаемый список книг одного автора")
    @Test
    void shouldReturnBooksListByExpectedAuthor() {
        var expectedAuthor = new Author(1, "Автор_01");
        var expectedBooksByAuthor = List.of(new Book(2, "Книга_02",
                new Genre(2, "Жанр_02"), expectedAuthor));

        var actualBooksByAuthor = bookRepositoryJdbc.getAllByAuthor(expectedAuthor.getName());

        assertThat(actualBooksByAuthor)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedBooksByAuthor);
    }

    @DisplayName("должен удалять заданную книгу по ее идентификатору")
    @Test
    void shouldDeleteBookById() {
        var existingBook = bookRepositoryJdbc.getById(1);
        assertThat(existingBook).isNotEmpty();

        bookRepositoryJdbc.deleteById(1);

        var deletedBook = bookRepositoryJdbc.getById(1);
        assertThat(deletedBook).isEmpty();
    }

    @DisplayName("должен удалять все книги из БД")
    @Test
    void shouldDeleteAllBooks() {
        var actualCountBeforeCleaning = bookRepositoryJdbc.count();
        assertThat(actualCountBeforeCleaning).isEqualTo(3);

        bookRepositoryJdbc.deleteAll();

        var expectedCountAfterCleaning = 0;
        var actualCountAfterCleaning = bookRepositoryJdbc.count();
        assertThat(actualCountAfterCleaning).isEqualTo(expectedCountAfterCleaning);
    }
}