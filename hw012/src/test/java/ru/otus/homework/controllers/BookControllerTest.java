package ru.otus.homework.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;
import ru.otus.homework.models.Genre;
import ru.otus.homework.services.AuthorService;
import ru.otus.homework.services.BookService;
import ru.otus.homework.services.CommentService;
import ru.otus.homework.services.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("должен отображать страницу со списком всех книг и указанием количества книг")
    @WithMockUser(username = "user")
    @Test
    void shouldDisplayListAllBooksPage() throws Exception {
        var expectedFirstAuthor = new Author(1, "firstAuthor");
        var expectedSecondAuthor = new Author(1, "secondAuthor");
        var expectedFirstGenre = new Genre(1, "firstGenre");
        var expectedSecondGenre = new Genre(1, "secondGenre");

        var expectedBooks = List.of(
                new Book(1, "firstBook", expectedFirstGenre, expectedFirstAuthor),
                new Book(2, "secondBook", expectedSecondGenre, expectedSecondAuthor)
        );
        given(bookService.getAllBooks()).willReturn(expectedBooks);
        given(bookService.getBooksCount()).willReturn(2L);
        given(genreService.getAllGenres()).willReturn(List.of(expectedFirstGenre, expectedSecondGenre));
        given(authorService.getAllAuthors()).willReturn(List.of(expectedFirstAuthor, expectedSecondAuthor));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", expectedBooks))
                .andExpect(model().attribute("authors", List.of(expectedFirstAuthor, expectedSecondAuthor)))
                .andExpect(model().attribute("genres", List.of(expectedFirstGenre, expectedSecondGenre)))
                .andExpect(model().attribute("booksCount", 2L));
        verify(bookService, times(1)).getAllBooks();
        verify(bookService, times(1)).getBooksCount();
        verify(genreService, times(1)).getAllGenres();
        verify(authorService, times(1)).getAllAuthors();
    }

    @DisplayName("должен отображать страницу со списком всех книг одного автора")
    @WithMockUser(username = "user")
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
        verify(bookService, times(1)).findAllBooksByAuthor(expectedAuthor.getName());
    }

    @DisplayName("должен отображать страницу со списком всех книг одного жанра")
    @WithMockUser(username = "user")
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
        verify(bookService, times(1)).findAllBooksByGenre(expectedGenre.getGenreName());
    }

    @DisplayName("должен отображать страницу подтверждения удаления книги")
    @WithMockUser(username = "user")
    @Test
    void shouldDisplayConfirmDeleteBookPage() throws Exception {
        var expectedBook = new Book(1, "firstBook", new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));
        given(bookService.findBookById(expectedBook.getId())).willReturn(expectedBook);

        mockMvc.perform(get("/delete").param("id", Long.toString(expectedBook.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("confirm-delete-book"))
                .andExpect(model().attribute("book", expectedBook));
        verify(bookService, times(1)).findBookById(expectedBook.getId());
    }

    @DisplayName("должен удалить книгу по ее идентификатору и затем отобразить страницу со списком всех книг")
    @WithMockUser(username = "user")
    @Test
    void shouldDeleteBookByIdAndThenShouldDisplayListALLBooksPage() throws Exception {
        var expectedBook = new Book(1, "firstBook", new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));
        mockMvc.perform(post("/delete")
                        .with(csrf())
                        .param("id", Long.toString(expectedBook.getId())))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).deleteBookById(expectedBook.getId());
    }

    @DisplayName("должен отображать страницу подтверждения удаления всех книг")
    @WithMockUser(username = "user")
    @Test
    void shouldDisplayConfirmDeleteAllBooksPage() throws Exception {
        mockMvc.perform(get("/delete-all"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirm-delete-all-books"));
    }

    @DisplayName("должен удалить все книги и затем отобразить страницу со списком всех книг")
    @WithMockUser(username = "user")
    @Test
    void shouldDeleteAllBooksAndThenShouldDisplayListAllBooksPage() throws Exception {
        mockMvc.perform(post("/delete-all").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).deleteAllBooks();
    }

    @DisplayName("должен отображать страницу со всей информацией о конкретной книге, включая комментарии")
    @WithMockUser(username = "user")
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
        verify(bookService, times(1)).findBookById(expectedBook.getId());
        verify(commentService, times(1)).findAllCommentsForSpecificBook(expectedBook.getId());
    }

    @DisplayName("должен изменить существующую книгу и затем отобразить страницу со списком всех книг")
    @WithMockUser(username = "user")
    @Test
    void shouldEditBookAndThenShouldDisplayListAllBooksPage() throws Exception {
        mockMvc.perform(post("/edit")
                        .with(csrf())
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
    @WithMockUser(username = "user")
    @Test
    void shouldAddNewBookInDatabaseAndThenShouldDisplayListAllBooksPage() throws Exception {
        mockMvc.perform(post("/add")
                        .with(csrf())
                        .param("title", "firstBook")
                        .param("genre", "firstGenre")
                        .param("author", "firstAuthor"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1))
                .addBook(anyString(), anyString(), anyString());
    }
}