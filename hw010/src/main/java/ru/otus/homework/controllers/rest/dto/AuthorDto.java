package ru.otus.homework.controllers.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.homework.models.Author;

@Data
@AllArgsConstructor
public class AuthorDto {
    private long id;
    private String name;

    public static AuthorDto transformDomainToDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }
}
