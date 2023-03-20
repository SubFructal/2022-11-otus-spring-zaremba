package ru.otus.homework.controllers.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.homework.models.Genre;

@Data
@AllArgsConstructor
public class GenreDto {
    private String id;
    private String name;

    public static GenreDto transformDomainToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
