package ru.otus.homework.services.converters;

import ru.otus.homework.models.Genre;

public interface GenreToStringConverter {
    String convertToString(Genre genre);
}
