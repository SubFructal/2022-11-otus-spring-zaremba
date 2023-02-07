package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
        return authorRepository.save(author);
    }

    @Override
    public Author changeAuthor(String id, String authorName) {
        var author = findAuthorById(id);
        author.setName(authorName);
        return authorRepository.save(author);
    }

    @Override
    public Author findAuthorById(String id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден автор с идентификатором %s", id)));
    }

    @Override
    public Author findAuthorByName(String authorName) {
        return authorRepository.findByName(authorName)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден автор с именем %s", authorName)));
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll(Sort.by(Sort.Direction.ASC,"id"));
    }

    @Override
    public Author deleteAuthorById(String id) {
        var author = findAuthorById(id);
        authorRepository.deleteCascade(author);
        authorRepository.deleteById(id);
        return author;
    }

    @Override
    public long deleteAllAuthors() {
        var authorsCount = getAuthorsCount();
        authorRepository.deleteAllCascade();
        authorRepository.deleteAll();
        return authorsCount;
    }
}
