package ru.otus.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.exceptions.NotFoundException;
import ru.otus.library.models.Author;
import ru.otus.library.repositories.AuthorRepository;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public long getAuthorsCount() {
        return authorRepository.count();
    }

    @Transactional
    @Override
    public Author addAuthor(String authorName) {
        var author = new Author();
        author.setName(authorName);
        return authorRepository.save(author);
    }

    @Transactional
    @Override
    public Author changeAuthor(long id, String authorName) {
        var author = findAuthorById(id);
        author.setName(authorName);
        return author;
    }

    @Transactional(readOnly = true)
    @Override
    public Author findAuthorById(long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("Не найден автор с идентификатором %d", id)));
    }

    @Transactional(readOnly = true)
    @Override
    public Author findAuthorByName(String authorName) {
        return authorRepository.findByName(authorName)
                .orElseThrow(() -> new NotFoundException(format("Не найден автор с именем %s", authorName)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll(Sort.by(Sort.Direction.ASC,"id"));
    }

    @Transactional
    @Override
    public Author deleteAuthorById(long id) {
        var author = findAuthorById(id);
        authorRepository.deleteById(id);
        return author;
    }

    @Transactional
    @Override
    public long deleteAllAuthors() {
        var authorsCount = getAuthorsCount();
        authorRepository.deleteAll();
        return authorsCount;
    }

}
