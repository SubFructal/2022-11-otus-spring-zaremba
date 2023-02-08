package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
        return genreRepository.save(genre);
    }

    @Override
    public Genre changeGenre(String id, String genreName) {
        var genre = findGenreById(id);
        genre.setGenreName(genreName);
        return genreRepository.save(genre);
    }

    @Override
    public Genre findGenreById(String id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден жанр с идентификатором %s", id)));
    }

    @Override
    public Genre findGenreByName(String genreName) {
        return genreRepository.findByGenreName(genreName)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден жанр с названием %s", genreName)));
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Genre deleteGenreById(String id) {
        var genre = findGenreById(id);
        genreRepository.deleteByIdCustom(id);
        return genre;
    }

    @Override
    public long deleteAllGenres() {
        var genresCount = getGenresCount();
        genreRepository.deleteAllCustom();
        return genresCount;
    }
}
