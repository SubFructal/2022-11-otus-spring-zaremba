package ru.otus.library.services;

import ru.otus.library.models.Genre;

import java.util.List;

public interface GenreService {
    long getGenresCount();

    Genre addGenre(String genreName);

    Genre changeGenre(long id, String genreName);

    Genre findGenreById(long id);

    Genre findGenreByName(String genreName);

    List<Genre> getAllGenres();

    Genre deleteGenreById(long id);

    long deleteAllGenres();
}
