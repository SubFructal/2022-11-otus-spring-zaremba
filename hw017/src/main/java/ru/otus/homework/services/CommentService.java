package ru.otus.homework.services;

import ru.otus.homework.models.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(long bookId, String commentText);

    Comment changeComment(long id, String commentText);

    Comment findCommentById(long id);

    List<Comment> findAllCommentsForSpecificBook(long bookId);

    Comment deleteCommentById(long id);
}
