package ru.otus.homework.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.models.Author;
import ru.otus.homework.services.AuthorService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("AuthorController")
@WebMvcTest(controllers = {AuthorController.class})
class AuthorControllerTest {

    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("должен отображать страницу со списком всех авторов")
    @Test
    void shouldDisplayListAllAuthorsPage() throws Exception {
        var expectedAuthors = List.of(
                new Author(1, "firstAuthor"),
                new Author(2, "secondAuthor")
        );
        given(authorService.getAllAuthors()).willReturn(expectedAuthors);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors"))
                .andExpect(model().attribute("authors", expectedAuthors));
    }
}