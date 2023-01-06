package ru.otus.homework.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private long id;
    private String title;
    private Genre genre;
    private Author author;

    public long getAuthorId() {
        return this.author.getId();
    }

    public long getGenreId() {
        return this.genre.getId();
    }
}
