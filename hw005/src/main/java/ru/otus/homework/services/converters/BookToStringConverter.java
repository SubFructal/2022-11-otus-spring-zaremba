package ru.otus.homework.services.converters;

import ru.otus.homework.models.Book;

public interface BookToStringConverter {
    String convertToString(Book book);
}
