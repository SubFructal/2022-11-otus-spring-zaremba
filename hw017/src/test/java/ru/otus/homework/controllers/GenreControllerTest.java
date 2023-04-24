package ru.otus.homework.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.models.Genre;
import ru.otus.homework.services.GenreService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("GenreController")
@WebMvcTest(controllers = {GenreController.class})
class GenreControllerTest {

    @MockBean
    private GenreService genreService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("должен отображать страницу со списком всех жанров")
    @WithMockUser(username = "user")
    @Test
    void shouldDisplayListAllGenresPage() throws Exception {
        var expectedGenres = List.of(
                new Genre(1, "firstGenre"),
                new Genre(2, "secondGenre")
        );
        given(genreService.getAllGenres()).willReturn(expectedGenres);

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres"))
                .andExpect(model().attribute("genres", expectedGenres));
    }
}