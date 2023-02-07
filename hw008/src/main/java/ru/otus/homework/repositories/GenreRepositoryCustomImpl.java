package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.homework.models.Genre;

import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override
    public void deleteCascade(Genre genre) {
        var books = bookRepository.findAllByGenre(genre);
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
