package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.homework.models.Author;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override
    public void deleteCascade(Author author) {
        var books = bookRepository.findAllByAuthor(author);
        var commentsLists = books.stream()
                .map(commentRepository::findAllByBook)
                .collect(Collectors.toList());

        for (var commentsList : commentsLists) {
            for (var comment : commentsList) {
                commentRepository.deleteById(comment.getId());
            }
        }
        books.forEach(book -> bookRepository.deleteById(book.getId()));
    }

    @Override
    public void deleteAllCascade() {
        commentRepository.deleteAll();
        bookRepository.deleteAll();
    }
}
