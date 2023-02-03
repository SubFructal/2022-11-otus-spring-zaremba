package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.models.Genre;
import ru.otus.homework.repositories.GenreRepository;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    @Override
    public long getGenresCount() {
        return genreRepository.count();
    }

    @Transactional
    @Override
    public Genre addGenre(String genreName) {
        var genre = new Genre();
        genre.setGenreName(genreName);
        return genreRepository.save(genre);
    }

    @Transactional
    @Override
    public Genre changeGenre(long id, String genreName) {
        var genre = findGenreById(id);
        genre.setGenreName(genreName);
        return genre;
    }

    @Transactional(readOnly = true)
    @Override
    public Genre findGenreById(long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден жанр с идентификатором %d", id)));
    }

    @Transactional(readOnly = true)
    @Override
    public Genre findGenreByName(String genreName) {
        return genreRepository.findByGenreName(genreName)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден жанр с названием %s", genreName)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional
    @Override
    public Genre deleteGenreById(long id) {
        var genre = findGenreById(id);
        genreRepository.deleteById(id);
        return genre;
    }

    @Transactional
    @Override
    public long deleteAllGenres() {
        return genreRepository.deleteAllCustom();
    }
}
