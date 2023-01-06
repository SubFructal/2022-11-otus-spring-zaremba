package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.models.Author;
import ru.otus.homework.repositories.AuthorRepository;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public long getAuthorsCount() {
        return authorRepository.count();
    }

    @Override
    public Author addAuthor(String authorName) {
        var author = new Author();
        author.setName(authorName);
        return authorRepository.insert(author);
    }

    @Override
    public Author changeAuthor(long id, String authorName) {
        var author = findAuthorById(id);
        author.setName(authorName);
        return authorRepository.update(author);
    }

    @Override
    public Author findAuthorById(long id) {
        return authorRepository.getById(id)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден автор с идентификатором %d", id)));
    }

    @Override
    public Author findAuthorByName(String authorName) {
        return authorRepository.getByName(authorName)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден автор с именем %s", authorName)));
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.getAll();
    }

    @Override
    public Author deleteAuthorById(long id) {
        var author = findAuthorById(id);
        authorRepository.deleteById(id);
        return author;
    }

    @Override
    public long deleteAllAuthors() {
        return authorRepository.clean();
    }

}
