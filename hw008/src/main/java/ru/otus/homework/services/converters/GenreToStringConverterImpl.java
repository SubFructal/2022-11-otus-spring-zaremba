package ru.otus.homework.services.converters;

import org.springframework.stereotype.Service;
import ru.otus.homework.models.Genre;

@Service
public class GenreToStringConverterImpl implements GenreToStringConverter {
    @Override
    public String convertToString(Genre genre) {
        return "Genre(id=" + genre.getId() + ", genreName=" + genre.getGenreName() + ")";
    }
}
