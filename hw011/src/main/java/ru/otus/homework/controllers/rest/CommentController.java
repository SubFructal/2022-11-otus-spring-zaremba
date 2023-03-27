package ru.otus.homework.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.homework.controllers.rest.dto.CommentDto;
import ru.otus.homework.repositories.CommentRepository;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    @GetMapping(value = "/api/books/{id}/comments")
    public Flux<CommentDto> findAllCommentsForSpecificBook(@PathVariable(value = "id") String id) {
        return commentRepository
                .findAllByBookId(id, Sort.by(Sort.Direction.ASC, "id"))
                .map(CommentDto::transformDomainToDto);
    }
}
