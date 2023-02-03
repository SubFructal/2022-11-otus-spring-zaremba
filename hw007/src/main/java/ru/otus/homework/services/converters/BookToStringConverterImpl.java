package ru.otus.homework.services.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.models.Book;

@Service
@RequiredArgsConstructor
public class BookToStringConverterImpl implements BookToStringConverter {

    private final GenreToStringConverter genreConverter;
    private final AuthorToStringConverter authorConverter;

    @Override
    public String convertToString(Book book) {
        return "Book(id=" + book.getId() + ", title=" + book.getTitle() +
                ", genre=" + genreConverter.convertToString(book.getGenre()) +
                ", author=" + authorConverter.convertToString(book.getAuthor()) + ")";
    }
}
