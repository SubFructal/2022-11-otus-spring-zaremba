package ru.otus.client.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.client.dto.AuthorDto;
import ru.otus.client.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping(value = "/feign/api/authors")
    public List<AuthorDto> findAllBooks() {
        return authorService.getAllAuthors();
    }
}
