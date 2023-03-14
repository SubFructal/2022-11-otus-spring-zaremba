package ru.otus.homework.controllers.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("AuthorPagesController")
@WebMvcTest(controllers = {AuthorPagesController.class})
class AuthorPagesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("должен отображать страницу со списком всех авторов")
    @Test
    void shouldDisplayListAllAuthorsPage() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors"));
    }
}