package ru.otus.homework.services;

import ru.otus.homework.models.Genre;

import java.util.List;

public interface GenreService {
    long getGenresCount();

    Genre addGenre(String genreName);

    Genre changeGenre(String id, String genreName);

    Genre findGenreById(String id);

    Genre findGenreByName(String genreName);

    List<Genre> getAllGenres();

    Genre deleteGenreById(String id);

    long deleteAllGenres();
}
