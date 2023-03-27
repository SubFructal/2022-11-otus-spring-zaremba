package ru.otus.homework.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.homework.controllers.rest.dto.GenreDto;
import ru.otus.homework.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreRepository genreRepository;

    @GetMapping(value = "/api/genres")
    public Flux<GenreDto> findAllGenres() {
        return genreRepository
                .findAll(Sort.by(Sort.Direction.ASC, "id"))
                .map(GenreDto::transformDomainToDto);
    }
}
