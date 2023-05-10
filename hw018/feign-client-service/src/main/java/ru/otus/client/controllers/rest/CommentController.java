package ru.otus.client.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.client.dto.BookDto;
import ru.otus.client.dto.CommentDto;
import ru.otus.client.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/feign/api/books/{id}/comments")
    public List<CommentDto> findAllCommentsForSpecificBook(@PathVariable(value = "id") long id) {
        return commentService.findAllCommentsForSpecificBook(id);
    }
}
