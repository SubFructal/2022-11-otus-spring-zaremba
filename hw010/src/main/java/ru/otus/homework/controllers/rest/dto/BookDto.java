package ru.otus.homework.controllers.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.homework.models.Book;

@Data
@AllArgsConstructor
public class BookDto {
    private long id;
    private String title;
    private String author;
    private String genre;

    public static BookDto transformDomainToDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName(),
                book.getGenre().getGenreName());
    }
}
