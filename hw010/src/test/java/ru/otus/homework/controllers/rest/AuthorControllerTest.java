package ru.otus.homework.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.controllers.rest.dto.AuthorDto;
import ru.otus.homework.models.Author;
import ru.otus.homework.services.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AuthorController")
@WebMvcTest(controllers = {AuthorController.class})
class AuthorControllerTest {

    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("должен возвращать корректный список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() throws Exception {
        var expectedAuthors = List.of(
                new Author(1, "firstAuthor"),
                new Author(2, "secondAuthor")
        );
        given(authorService.getAllAuthors()).willReturn(expectedAuthors);

        List<AuthorDto> expectedResult = expectedAuthors.stream()
                .map(AuthorDto::transformDomainToDto)
                .collect(Collectors.toList());

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(authorService, times(1)).getAllAuthors();
    }
}