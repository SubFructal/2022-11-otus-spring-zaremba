package ru.otus.homework.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;
import ru.otus.homework.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@DisplayName("Класс BookServiceImpl")
@SpringBootTest
class BookServiceImplTest {

    private static final long EXPECTED_BOOKS_COUNT = 3;
    private static final long EXPECTED_DELETED_BOOKS_COUNT = 3;
    private static final long EXISTING_BOOK_ID = 1;
    private static final long NOT_EXISTING_BOOK_ID = 100;

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;
    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("должен возвращать ожидаемое количество книг в БД")
    @Test
    void shouldReturnExpectedBooksCount() {
        given(bookRepository.count()).willReturn(EXPECTED_BOOKS_COUNT);
        var actualBooksCount = bookService.getBooksCount();

        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOKS_COUNT);
        verify(bookRepository, times(1)).count();
    }

    @DisplayName("должен добавлять книгу в БД при указании ее названия и идентификаторов жанра и автора")
    @Test
    void shouldInsertBookWithTitleAndIdentifiers() {
        var expectedGenre = new Genre(1, "Жанр_01");
        var expectedAuthor = new Author(1, "Автор_01");
        var expectedBook = new Book(4, "Книга_04", expectedGenre, expectedAuthor);

        given(genreService.findGenreById(expectedGenre.getId())).willReturn(expectedGenre);
        given(authorService.findAuthorById(expectedAuthor.getId())).willReturn(expectedAuthor);
        given(bookRepository.insert(new Book(0, expectedBook.getTitle(), expectedGenre, expectedAuthor)))
                .willReturn(expectedBook);

        var actualBook = bookService.addBook(expectedBook.getTitle(), expectedGenre.getId(), expectedAuthor.getId());

        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
        verify(genreService, times(1))
                .findGenreById(expectedGenre.getId());
        verify(authorService, times(1))
                .findAuthorById(expectedAuthor.getId());
        verify(bookRepository, times(1))
                .insert(new Book(0, expectedBook.getTitle(), expectedGenre, expectedAuthor));
    }

    @DisplayName("должен добавлять книгу в БД при указании ее названия, названия жанра и имени автора")
    @Test
    void shouldInsertBookWithNames() {
        var expectedGenre = new Genre(1, "Жанр_01");
        var expectedAuthor = new Author(1, "Автор_01");
        var expectedBook = new Book(4, "Книга_04", expectedGenre, expectedAuthor);

        given(genreService.findGenreByName(expectedGenre.getGenreName())).willReturn(expectedGenre);
        given(authorService.findAuthorByName(expectedAuthor.getName())).willReturn(expectedAuthor);
        given(bookRepository.insert(new Book(0, expectedBook.getTitle(), expectedGenre, expectedAuthor)))
                .willReturn(expectedBook);

        var actualBook = bookService.addBook(expectedBook.getTitle(), expectedGenre.getGenreName(),
                expectedAuthor.getName());

        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
        verify(genreService, times(1))
                .findGenreByName(expectedGenre.getGenreName());
        verify(authorService, times(1))
                .findAuthorByName(expectedAuthor.getName());
        verify(bookRepository, times(1))
                .insert(new Book(0, expectedBook.getTitle(), expectedGenre, expectedAuthor));
    }

    @DisplayName("должен изменять имеющуюся в БД книгу при указании ее id, названия и идентификаторов жанра и автора")
    @Test
    void shouldChangeBookWithTitleAndIdentifiers() {
        var bookFromDatabase = new Book(EXISTING_BOOK_ID, "Книга_01", new Genre(1, "Жанр_01"),
                new Author(3, "Автор_03"));

        var expectedGenre = new Genre(2, "Жанр_02");
        var expectedAuthor = new Author(1, "Автор_01");
        var expectedChangedBook = new Book(EXISTING_BOOK_ID, "Книга_01_Updated", expectedGenre, expectedAuthor);

        given(genreService.findGenreById(expectedGenre.getId())).willReturn(expectedGenre);
        given(authorService.findAuthorById(expectedAuthor.getId())).willReturn(expectedAuthor);
        given(bookRepository.getById(expectedChangedBook.getId())).willReturn(Optional.of(bookFromDatabase));
        given(bookRepository.update(expectedChangedBook)).willReturn(expectedChangedBook);

        var actualChangedBook = bookService.changeBook(expectedChangedBook.getId(), expectedChangedBook.getTitle(),
                expectedGenre.getId(), expectedAuthor.getId());

        assertThat(actualChangedBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedChangedBook);
        verify(genreService, times(1)).findGenreById(expectedGenre.getId());
        verify(authorService, times(1)).findAuthorById(expectedAuthor.getId());
        verify(bookRepository, times(1)).getById(expectedChangedBook.getId());
        verify(bookRepository, times(1)).update(expectedChangedBook);
    }

    @DisplayName("должен изменять имеющуюся в БД книгу при указании ее id, названия, названия жанра и имени автора")
    @Test
    void shouldChangeBookWithNames() {
        var bookFromDatabase = new Book(EXISTING_BOOK_ID, "Книга_01", new Genre(1, "Жанр_01"),
                new Author(3, "Автор_03"));

        var expectedGenre = new Genre(2, "Жанр_02");
        var expectedAuthor = new Author(1, "Автор_01");
        var expectedChangedBook = new Book(EXISTING_BOOK_ID, "Книга_01_Updated", expectedGenre, expectedAuthor);

        given(genreService.findGenreByName(expectedGenre.getGenreName())).willReturn(expectedGenre);
        given(authorService.findAuthorByName(expectedAuthor.getName())).willReturn(expectedAuthor);
        given(bookRepository.getById(expectedChangedBook.getId())).willReturn(Optional.of(bookFromDatabase));
        given(bookRepository.update(expectedChangedBook)).willReturn(expectedChangedBook);

        var actualChangedBook = bookService.changeBook(expectedChangedBook.getId(), expectedChangedBook.getTitle(),
                expectedGenre.getGenreName(), expectedAuthor.getName());

        assertThat(actualChangedBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedChangedBook);
        verify(genreService, times(1)).findGenreByName(expectedGenre.getGenreName());
        verify(authorService, times(1)).findAuthorByName(expectedAuthor.getName());
        verify(bookRepository, times(1)).getById(expectedChangedBook.getId());
        verify(bookRepository, times(1)).update(expectedChangedBook);
    }

    @DisplayName("должен возвращать ожидаемую книгу по идентификатору")
    @Test
    void shouldReturnExpectedBookById() {
        var expectedBook = new Book();
        expectedBook.setId(EXISTING_BOOK_ID);
        expectedBook.setTitle("Книга_01");
        expectedBook.setGenre(new Genre(1, "Жанр_01"));
        expectedBook.setAuthor(new Author(3, "Автор_03"));

        given(bookRepository.getById(EXISTING_BOOK_ID)).willReturn(Optional.of(expectedBook));
        given(bookRepository.getById(NOT_EXISTING_BOOK_ID)).willReturn(Optional.empty());

        var actualBook = bookService.findBookById(EXISTING_BOOK_ID);

        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
        assertThatThrownBy(() -> bookService.findBookById(NOT_EXISTING_BOOK_ID))
                .isInstanceOf(IllegalArgumentException.class);
        verify(bookRepository, times(1)).getById(EXISTING_BOOK_ID);
        verify(bookRepository, times(1)).getById(NOT_EXISTING_BOOK_ID);
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

        given(bookRepository.getAll()).willReturn(expectedBooks);
        var actualBooksList = bookService.getAllBooks();

        assertThat(actualBooksList)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedBooks);
        verify(bookRepository, times(1)).getAll();
    }

    @DisplayName("должен возвращать ожидаемый список книг одного жанра")
    @Test
    void shouldReturnBooksListByExpectedGenre() {
        var expectedGenre = new Genre(1, "Жанр_01");
        var expectedBooksByGenre = List.of(
                new Book(1, "Книга_01", expectedGenre, new Author(3, "Автор_03")),
                new Book(3, "Книга_03", expectedGenre, new Author(2, "Автор_02"))
        );

        given(bookRepository.getAllByGenre(expectedGenre.getGenreName())).willReturn(expectedBooksByGenre);
        var actualBooksByGenre = bookService.findAllBooksByGenre(expectedGenre.getGenreName());

        assertThat(actualBooksByGenre)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedBooksByGenre);
        verify(bookRepository, times(1)).getAllByGenre(expectedGenre.getGenreName());
    }

    @DisplayName("должен возвращать ожидаемый список книг одного автора")
    @Test
    void shouldReturnBooksListByExpectedAuthor() {
        var expectedAuthor = new Author(1, "Автор_01");
        var expectedBooksByAuthor = List.of(new Book(2, "Книга_02",
                new Genre(2, "Жанр_02"), expectedAuthor));

        given(bookRepository.getAllByAuthor(expectedAuthor.getName())).willReturn(expectedBooksByAuthor);
        var actualBooksByAuthor = bookService.findAllBooksByAuthor(expectedAuthor.getName());

        assertThat(actualBooksByAuthor)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedBooksByAuthor);
        verify(bookRepository, times(1)).getAllByAuthor(expectedAuthor.getName());
    }

    @DisplayName("должен удалять заданную книгу по ее идентификатору")
    @Test
    void shouldDeleteBookById() {
        var expectedBook = new Book();
        expectedBook.setId(EXISTING_BOOK_ID);
        expectedBook.setTitle("Книга_01");
        expectedBook.setGenre(new Genre(1, "Жанр_01"));
        expectedBook.setAuthor(new Author(3, "Автор_03"));

        given(bookRepository.getById(EXISTING_BOOK_ID)).willReturn(Optional.of(expectedBook));

        var actualBook = bookService.deleteBookById(EXISTING_BOOK_ID);

        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
        verify(bookRepository, times(1)).deleteById(EXISTING_BOOK_ID);
    }

    @DisplayName("должен удалять все книги из БД")
    @Test
    void shouldDeleteAllBooks() {
        given(bookRepository.deleteAll()).willReturn(EXPECTED_DELETED_BOOKS_COUNT);
        var actualDeletedBooksCount = bookService.deleteAllBooks();

        assertThat(actualDeletedBooksCount).isEqualTo(EXPECTED_DELETED_BOOKS_COUNT);
        verify(bookRepository, times(1)).deleteAll();
    }
}