package ru.otus.homework.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;
import ru.otus.homework.models.Genre;
import ru.otus.homework.services.BookService;
import ru.otus.homework.services.CommentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("BookController")
@WebMvcTest(controllers = {BookController.class})
class BookControllerTest {

    @MockBean
    private BookService bookService;
    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("должен отображать страницу со списком всех книг и указанием количества книг")
    @Test
    void shouldDisplayListAllBooksPage() throws Exception {
        var expectedBooks = List.of(
                new Book(1, "firstBook", new Genre(1, "firstGenre"),
                        new Author(1, "firstAuthor")),
                new Book(2, "secondBook", new Genre(2, "secondGenre"),
                        new Author(2, "secondAuthor"))
        );
        given(bookService.getAllBooks()).willReturn(expectedBooks);
        given(bookService.getBooksCount()).willReturn(2L);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", expectedBooks))
                .andExpect(model().attribute("booksCount", 2L));
    }

    @DisplayName("должен отображать страницу со списком всех книг одного автора")
    @Test
    void shouldDisplayListBooksByAuthorPage() throws Exception {
        var expectedAuthor = new Author(1, "firstAuthor");
        var expectedBooks = List.of(
                new Book(1, "firstBook", new Genre(1, "firstGenre"), expectedAuthor),
                new Book(2, "secondBook", new Genre(2, "secondGenre"), expectedAuthor)
        );
        given(bookService.findAllBooksByAuthor(expectedAuthor.getName())).willReturn(expectedBooks);

        mockMvc.perform(get("/books-by-author").param("name", expectedAuthor.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("books-by-author"))
                .andExpect(model().attribute("books", expectedBooks));
    }

    @DisplayName("должен отображать страницу со списком всех книг одного жанра")
    @Test
    void shouldDisplayListBooksByGenrePage() throws Exception {
        var expectedGenre = new Genre(1, "firstGenre");
        var expectedBooks = List.of(
                new Book(1, "firstBook", expectedGenre, new Author(1, "firstAuthor")),
                new Book(2, "secondBook", expectedGenre, new Author(2, "secondAuthor"))
        );
        given(bookService.findAllBooksByGenre(expectedGenre.getGenreName())).willReturn(expectedBooks);

        mockMvc.perform(get("/books-by-genre").param("name", expectedGenre.getGenreName()))
                .andExpect(status().isOk())
                .andExpect(view().name("books-by-genre"))
                .andExpect(model().attribute("books", expectedBooks));
    }

    @DisplayName("должен удалить книгу по ее идентификатору и затем отобразить страницу со списком всех книг")
    @Test
    void shouldDeleteBookByIdAndThenShouldDisplayListALLBooksPage() throws Exception {
        var expectedBook = new Book(1, "firstBook", new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));
        mockMvc.perform(get("/delete").param("id", Long.toString(expectedBook.getId())))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).deleteBookById(expectedBook.getId());
    }

    @DisplayName("должен удалить все книги и затем отобразить страницу со списком всех книг")
    @Test
    void shouldDeleteAllBooksAndThenShouldDisplayListAllBooksPage() throws Exception {
        mockMvc.perform(get("/delete-all"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).deleteAllBooks();
    }

    @DisplayName("должен отображать страницу со всей информацией о конкретной книге, включая комментарии")
    @Test
    void shouldDisplaySpecificBookPage() throws Exception {
        var expectedBook = new Book(1, "firstBook", new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));
        var expectedComments = List.of(
                new Comment(1, "firstComment", expectedBook),
                new Comment(1, "firstComment", expectedBook)
        );
        given(bookService.findBookById(expectedBook.getId())).willReturn(expectedBook);
        given(commentService.findAllCommentsForSpecificBook(expectedBook.getId())).willReturn(expectedComments);

        mockMvc.perform(get("/edit").param("id", Long.toString(expectedBook.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("comments", expectedComments));
    }

    @DisplayName("должен изменить существующую книгу и затем отобразить страницу со списком всех книг")
    @Test
    void shouldEditBookAndThenShouldDisplayListAllBooksPage() throws Exception {
        mockMvc.perform(post("/edit")
                        .param("id", "1")
                        .param("title", "firstBook")
                        .param("genre", "firstGenre")
                        .param("author", "firstAuthor"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1))
                .changeBook(anyLong(), anyString(), anyString(), anyString());
    }

    @DisplayName("должен добавить новую книгу в БД и затем отобразить страницу со списком всех книг")
    @Test
    void shouldAddNewBookInDatabaseAndThenShouldDisplayListAllBooksPage() throws Exception {
        mockMvc.perform(post("/add")
                        .param("title", "firstBook")
                        .param("genre", "firstGenre")
                        .param("author", "firstAuthor"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1))
                .addBook(anyString(), anyString(), anyString());
    }
}