package ru.otus.homework.controllers.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("GenrePagesController")
@WebMvcTest(controllers = {GenrePagesController.class})
class GenrePagesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("должен отображать страницу со списком всех жанров")
    @Test
    void shouldDisplayListAllGenresPage() throws Exception {
        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres"));
    }
}