package ru.otus.homework.services.converters;

import org.springframework.stereotype.Service;
import ru.otus.homework.models.Author;

@Service
public class AuthorToStringConverterImpl implements AuthorToStringConverter {
    @Override
    public String convertToString(Author author) {
        return "Author(id=" + author.getId() + ", name=" + author.getName() + ")";
    }
}
