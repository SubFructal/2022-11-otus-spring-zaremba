package ru.otus.homework.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findAllByBook(Book book);
}
