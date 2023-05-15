package ru.otus.client.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.client.dto.GenreDto;
import ru.otus.client.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping(value = "/feign/api/genres")
    public List<GenreDto> findAllBooks() {
        return genreService.getAllGenres();
    }
}
