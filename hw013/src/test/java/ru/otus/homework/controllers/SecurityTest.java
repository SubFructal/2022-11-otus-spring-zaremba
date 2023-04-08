package ru.otus.homework.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;
import ru.otus.homework.security.SecurityConfiguration;
import ru.otus.homework.services.AuthorService;
import ru.otus.homework.services.BookService;
import ru.otus.homework.services.CommentService;
import ru.otus.homework.services.GenreService;

import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SecurityTest проверяет ")
@WebMvcTest(controllers = {AuthorController.class, GenreController.class, BookController.class})
@Import(value = {SecurityConfiguration.class})
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

    @DisplayName("авторизацию по url-ам для пользователя с ролью ADMIN")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @ParameterizedTest
    @MethodSource("generateDataForAdminSecurityTests")
    void checkUrlAccessForAdminRole(MockHttpServletRequestBuilder method,
                                    ResultMatcher status, String idName, String idValue,
                                    String bookTitleName, String bookTitleValue,
                                    String genreName, String genreValue,
                                    String authorName, String authorValue) throws Exception {
        var expectedBook = new Book(1, "firstBook", new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));
        given(bookService.findBookById(expectedBook.getId())).willReturn(expectedBook);
        mockMvc.perform(method.with(csrf())
                        .param(idName, idValue)
                        .param(bookTitleName, bookTitleValue)
                        .param(genreName, genreValue)
                        .param(authorName, authorValue))
                .andExpect(status);
    }

    @DisplayName("авторизацию по url-ам для пользователя с ролью USER")
    @WithMockUser(username = "user", roles = {"USER"})
    @ParameterizedTest
    @MethodSource("generateDataForUserSecurityTests")
    void checkUrlAccessForUserRole(MockHttpServletRequestBuilder method,
                                   ResultMatcher status, String idName, String idValue,
                                   String bookTitleName, String bookTitleValue,
                                   String genreName, String genreValue,
                                   String authorName, String authorValue) throws Exception {
        var expectedBook = new Book(1, "firstBook", new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));
        given(bookService.findBookById(expectedBook.getId())).willReturn(expectedBook);
        mockMvc.perform(method.with(csrf())
                        .param(idName, idValue)
                        .param(bookTitleName, bookTitleValue)
                        .param(genreName, genreValue)
                        .param(authorName, authorValue))
                .andExpect(status);
    }

    @DisplayName("авторизацию по url-ам для анонимного пользователя")
    @WithAnonymousUser
    @ParameterizedTest
    @MethodSource("generateDataForAnonymousUserSecurityTests")
    void checkUrlAccessForAnonymousUser(MockHttpServletRequestBuilder method,
                                   ResultMatcher status, String idName, String idValue,
                                   String bookTitleName, String bookTitleValue,
                                   String genreName, String genreValue,
                                   String authorName, String authorValue) throws Exception {
        var expectedBook = new Book(1, "firstBook", new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));
        given(bookService.findBookById(expectedBook.getId())).willReturn(expectedBook);
        mockMvc.perform(method.with(csrf())
                        .param(idName, idValue)
                        .param(bookTitleName, bookTitleValue)
                        .param(genreName, genreValue)
                        .param(authorName, authorValue))
                .andExpect(status);
    }

    private static Stream<Arguments> generateDataForAdminSecurityTests() {
        return Stream.of(
                Arguments.of(get("/login"), status().isOk(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/authors"), status().isOk(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/genres"), status().isOk(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/"), status().isOk(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/books-by-author"), status().isOk(), "name", "firstAuthor",
                        "null", "null", "null", "null", "null", "null"),
                Arguments.of(get("/books-by-genre"), status().isOk(), "name", "firstGenre",
                        "null", "null", "null", "null", "null", "null"),
                Arguments.of(get("/delete-all"), status().isOk(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/delete"), status().isOk(), "id", "1", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/edit"), status().isOk(), "id", "1", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(post("/delete"), status().isFound(), "id", "1", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(post("/delete-all"), status().isFound(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(post("/edit"), status().isFound(), "id", "1", "title", "firstBook",
                        "genre", "firstGenre", "author", "firstAuthor"),
                Arguments.of(post("/add"), status().isFound(), "null", "null", "title", "firstBook",
                        "genre", "firstGenre", "author", "firstAuthor")
        );
    }

    private static Stream<Arguments> generateDataForUserSecurityTests() {
        return Stream.of(
                Arguments.of(get("/login"), status().isOk(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/authors"), status().isOk(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/genres"), status().isOk(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/"), status().isOk(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/books-by-author"), status().isOk(), "name", "firstAuthor",
                        "null", "null", "null", "null", "null", "null"),
                Arguments.of(get("/books-by-genre"), status().isOk(), "name", "firstGenre",
                        "null", "null", "null", "null", "null", "null"),
                Arguments.of(get("/delete-all"), status().isForbidden(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/delete"), status().isForbidden(), "id", "1", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/edit"), status().isForbidden(), "id", "1", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(post("/delete"), status().isForbidden(), "id", "1", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(post("/delete-all"), status().isForbidden(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(post("/edit"), status().isForbidden(), "id", "1", "title", "firstBook",
                        "genre", "firstGenre", "author", "firstAuthor"),
                Arguments.of(post("/add"), status().isForbidden(), "null", "null", "title", "firstBook",
                        "genre", "firstGenre", "author", "firstAuthor")
        );
    }

    private static Stream<Arguments> generateDataForAnonymousUserSecurityTests() {
        return Stream.of(
                Arguments.of(get("/login"), status().isOk(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/authors"), status().isFound(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/genres"), status().isFound(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/"), status().isFound(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/books-by-author"), status().isFound(), "name", "firstAuthor",
                        "null", "null", "null", "null", "null", "null"),
                Arguments.of(get("/books-by-genre"), status().isFound(), "name", "firstGenre",
                        "null", "null", "null", "null", "null", "null"),
                Arguments.of(get("/delete-all"), status().isFound(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/delete"), status().isFound(), "id", "1", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(get("/edit"), status().isFound(), "id", "1", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(post("/delete"), status().isFound(), "id", "1", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(post("/delete-all"), status().isFound(), "null", "null", "null", "null",
                        "null", "null", "null", "null"),
                Arguments.of(post("/edit"), status().isFound(), "id", "1", "title", "firstBook",
                        "genre", "firstGenre", "author", "firstAuthor"),
                Arguments.of(post("/add"), status().isFound(), "null", "null", "title", "firstBook",
                        "genre", "firstGenre", "author", "firstAuthor")
        );
    }
}
