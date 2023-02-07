package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.homework.models.Book;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final CommentRepository commentRepository;

    @Override
    public void deleteCascade(Book book) {
        commentRepository.findAllByBook(book)
                .forEach(comment -> commentRepository.deleteById(comment.getId()));
    }

    @Override
    public void deleteAllCascade() {
        commentRepository.deleteAll();
    }
}
