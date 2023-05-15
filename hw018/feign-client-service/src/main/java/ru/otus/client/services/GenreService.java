package ru.otus.client.services;

import ru.otus.client.dto.GenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> getAllGenres();
}
