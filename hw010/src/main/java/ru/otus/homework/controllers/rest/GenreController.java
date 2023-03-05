package ru.otus.homework.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.controllers.rest.dto.GenreDto;
import ru.otus.homework.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping(value = "/api/genres")
    public List<GenreDto> findAllGenres() {
        return genreService.getAllGenres()
                .stream()
                .map(GenreDto::transformDomainToDto)
                .collect(Collectors.toList());
    }
}
