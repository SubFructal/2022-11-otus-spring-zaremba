package ru.otus.library.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.library.controllers.rest.dto.CommentDto;
import ru.otus.library.services.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/api/books/{id}/comments")
    public List<CommentDto> findAllCommentsForSpecificBook(@PathVariable(value = "id") long id) {
        return commentService.findAllCommentsForSpecificBook(id)
                .stream()
                .map(CommentDto::transformDomainToDto)
                .collect(Collectors.toList());
    }
}
