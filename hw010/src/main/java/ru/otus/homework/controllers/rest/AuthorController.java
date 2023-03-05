package ru.otus.homework.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.controllers.rest.dto.AuthorDto;
import ru.otus.homework.services.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping(value = "/api/authors")
    public List<AuthorDto> findAllAuthors() {
        return authorService.getAllAuthors()
                .stream()
                .map(AuthorDto::transformDomainToDto)
                .collect(Collectors.toList());
    }
}
