package ru.otus.library.controllers.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.library.models.Author;

@Data
@AllArgsConstructor
public class AuthorDto {
    private long id;
    private String name;

    public static AuthorDto transformDomainToDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }
}
