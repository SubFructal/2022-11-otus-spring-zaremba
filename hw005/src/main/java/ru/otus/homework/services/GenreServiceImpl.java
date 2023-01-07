package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.models.Genre;
import ru.otus.homework.repositories.GenreRepository;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public long getGenresCount() {
        return genreRepository.count();
    }

    @Override
    public Genre addGenre(String genreName) {
        var genre = new Genre();
        genre.setGenreName(genreName);
        return genreRepository.insert(genre);
    }

    @Override
    public Genre changeGenre(long id, String genreName) {
        var genre = findGenreById(id);
        genre.setGenreName(genreName);
        return genreRepository.update(genre);
    }

    @Override
    public Genre findGenreById(long id) {
        return genreRepository.getById(id)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден жанр с идентификатором %d", id)));
    }

    @Override
    public Genre findGenreByName(String genreName) {
        return genreRepository.getByName(genreName)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден жанр с названием %s", genreName)));
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.getAll();
    }

    @Override
    public Genre deleteGenreById(long id) {
        var genre = findGenreById(id);
        genreRepository.deleteById(id);
        return genre;
    }

    @Override
    public long deleteAllGenres() {
        return genreRepository.deleteAll();
    }
}
