package ru.otus.homework.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;
import ru.otus.homework.security.SecurityConfiguration;
import ru.otus.homework.services.AuthorService;
import ru.otus.homework.services.BookService;
import ru.otus.homework.services.CommentService;
import ru.otus.homework.services.GenreService;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("SecurityTest проверяет, что ")
@Import(SecurityConfiguration.class)
@WebMvcTest(controllers = {AuthorController.class, GenreController.class, BookController.class})
public class SecurityTest {

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

    @DisplayName("для аутентифицированного пользователя возвращается код 200 (OK) при GET запросах по каждому из url-ов")
    @WithMockUser(username = "user")
    @ParameterizedTest
    @CsvSource(value = {
            "/authors, null, null",
            "/genres, null, null",
            "/, null, null",
            "/books-by-author, name, firstAuthor",
            "/books-by-genre, name, firstGenre",
            "/delete-all, null, null",
            "/delete, id, 1",
            "/edit, id, 1"
    })
    void shouldReturnOkStatusOnGetRequestForAuthenticatedUser(String url,
                                                              String idName, String idValue)
            throws Exception {
        var expectedBook = new Book(1, "firstBook", new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));
        given(bookService.findBookById(expectedBook.getId())).willReturn(expectedBook);

        mockMvc.perform(get(url).param(idName, idValue))
                .andExpect(status().isOk());
    }

    @DisplayName("для аутентифицированного пользователя возвращается код 302 (Found) при POST запросах по каждому из url-ов")
    @WithMockUser(username = "user")
    @ParameterizedTest
    @CsvSource(value = {
            "/delete, id, 1, null, null, null, null, null, null",
            "/delete-all, null, null, null, null, null, null, null, null",
            "/edit, id, 1, title, firstBook, genre, firstGenre, author, firstAuthor",
            "/add, null, null, title, firstBook, genre, firstGenre, author, firstAuthor"
    })
    void shouldReturnFoundStatusOnPostRequestForAuthenticatedUser(String url,
                                                                  String idName, String idValue,
                                                                  String bookTitleName, String bookTitleValue,
                                                                  String genreName, String genreValue,
                                                                  String authorName, String authorValue)
            throws Exception {
        mockMvc.perform(post(url).with(csrf())
                        .param(idName, idValue)
                        .param(bookTitleName, bookTitleValue)
                        .param(genreName, genreValue)
                        .param(authorName, authorValue))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @DisplayName("для анонимного пользователя возвращается код 401 (Unauthorized) при GET запросах по каждому из url-ов")
    @WithAnonymousUser
    @ParameterizedTest
    @CsvSource(value = {
            "/authors, null, null",
            "/genres, null, null",
            "/, null, null",
            "/books-by-author, name, firstAuthor",
            "/books-by-genre, name, firstGenre",
            "/delete-all, null, null",
            "/delete, id, 1",
            "/edit, id, 1"
    })
    void shouldReturnUnauthorizedStatusOnGetRequestForAnonymousUser(String url,
                                                                    String paramName,
                                                                    String paramValue)
            throws Exception {
        var expectedBook = new Book(1, "firstBook", new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));
        given(bookService.findBookById(expectedBook.getId())).willReturn(expectedBook);

        mockMvc.perform(get(url).param(paramName, paramValue))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("для анонимного пользователя возвращается код 401 (Unauthorized) при POST запросах по каждому из url-ов")
    @WithAnonymousUser
    @ParameterizedTest
    @CsvSource(value = {
            "/delete, id, 1, null, null, null, null, null, null",
            "/delete-all, null, null, null, null, null, null, null, null",
            "/edit, id, 1, title, firstBook, genre, firstGenre, author, firstAuthor",
            "/add, null, null, title, firstBook, genre, firstGenre, author, firstAuthor"
    })
    void shouldReturnUnauthorizedStatusOnPostRequestForAnonymousUser(String url,
                                                                     String idName, String idValue,
                                                                     String bookTitleName, String bookTitleValue,
                                                                     String genreName, String genreValue,
                                                                     String authorName, String authorValue)
            throws Exception {
        mockMvc.perform(post(url).with(csrf())
                        .param(idName, idValue)
                        .param(bookTitleName, bookTitleValue)
                        .param(genreName, genreValue)
                        .param(authorName, authorValue))
                .andExpect(status().isUnauthorized());
    }
}
