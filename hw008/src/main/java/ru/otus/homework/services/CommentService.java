package ru.otus.homework.services;

import ru.otus.homework.models.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(String bookId, String commentText);

    Comment changeComment(String id, String commentText);

    Comment findCommentById(String id);

    List<Comment> findAllCommentsForSpecificBook(String bookId);

    Comment deleteCommentById(String id);
}
