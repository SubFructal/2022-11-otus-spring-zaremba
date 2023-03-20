package ru.otus.homework.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.homework.controllers.rest.dto.AuthorDto;
import ru.otus.homework.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping(value = "/api/authors")
    public Flux<AuthorDto> findAllAuthors() {
        return authorRepository
                .findAll(Sort.by(Sort.Direction.ASC,"id"))
                .map(AuthorDto::transformDomainToDto);
    }
}
