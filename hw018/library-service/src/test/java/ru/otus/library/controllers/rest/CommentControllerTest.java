package ru.otus.library.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.controllers.rest.dto.CommentDto;
import ru.otus.library.models.Author;
import ru.otus.library.models.Book;
import ru.otus.library.models.Comment;
import ru.otus.library.models.Genre;
import ru.otus.library.services.CommentService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CommentController")
@WebMvcTest(controllers = {CommentController.class})
class CommentControllerTest {

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("должен возвращать корректный список комментариев для конкретной книги")
    @Test
    void shouldReturnCorrectCommentsListForSpecificBook() throws Exception {
        var expectedBook = new Book(1, "firstBook", new Genre(1, "firstGenre"),
                new Author(1, "firstAuthor"));
        var expectedComments = List.of(
                new Comment(1, "firstComment", expectedBook),
                new Comment(1, "firstComment", expectedBook)
        );

        given(commentService.findAllCommentsForSpecificBook(expectedBook.getId())).willReturn(expectedComments);
        List<CommentDto> expectedResult = expectedComments.stream()
                .map(CommentDto::transformDomainToDto)
                .collect(Collectors.toList());

        mockMvc.perform(get("/api/books/{id}/comments", expectedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(commentService, times(1)).findAllCommentsForSpecificBook(expectedBook.getId());
    }
}