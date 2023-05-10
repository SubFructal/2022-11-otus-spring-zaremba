package ru.otus.library.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.controllers.rest.dto.GenreDto;
import ru.otus.library.models.Genre;
import ru.otus.library.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("GenreController")
@WebMvcTest(controllers = {GenreController.class})
class GenreControllerTest {

    @MockBean
    private GenreService genreService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("должен возвращать корректный список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        var expectedGenres = List.of(
                new Genre(1, "firstGenre"),
                new Genre(2, "secondGenre")
        );
        given(genreService.getAllGenres()).willReturn(expectedGenres);

        List<GenreDto> expectedResult = expectedGenres.stream()
                .map(GenreDto::transformDomainToDto)
                .collect(Collectors.toList());

        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(genreService, times(1)).getAllGenres();
    }
}