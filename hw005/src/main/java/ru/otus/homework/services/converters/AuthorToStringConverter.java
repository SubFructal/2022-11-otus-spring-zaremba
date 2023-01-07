package ru.otus.homework.services.converters;

import ru.otus.homework.models.Author;

public interface AuthorToStringConverter {
    String convertToString(Author author);
}
