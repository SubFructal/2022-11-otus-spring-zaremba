package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.models.Comment;
import ru.otus.homework.repositories.CommentRepository;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookService bookService;

    @Override
    public Comment addComment(String bookId, String commentText) {
        var book = bookService.findBookById(bookId);
        var comment = new Comment();
        comment.setBook(book);
        comment.setCommentText(commentText);
        return commentRepository.save(comment);
    }

    @Override
    public Comment changeComment(String id, String commentText) {
        var comment = findCommentById(id);
        comment.setCommentText(commentText);
        return commentRepository.save(comment);
    }

    @Override
    public Comment findCommentById(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден комментарий с идентификатором %s", id)));
    }

    @Override
    public List<Comment> findAllCommentsForSpecificBook(String bookId) {
        var book = bookService.findBookById(bookId);
        return commentRepository.findAllByBook(book);
    }

    @Override
    public Comment deleteCommentById(String id) {
        var comment = findCommentById(id);
        commentRepository.deleteById(id);
        return comment;
    }
}
