package ru.otus.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.exceptions.NotFoundException;
import ru.otus.library.models.Comment;
import ru.otus.library.repositories.CommentRepository;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookService bookService;

    @Transactional
    @Override
    public Comment addComment(long bookId, String commentText) {
        var book = bookService.findBookById(bookId);
        var comment = new Comment();
        comment.setBook(book);
        comment.setCommentText(commentText);
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment changeComment(long id, String commentText) {
        var comment = findCommentById(id);
        comment.setCommentText(commentText);
        return comment;
    }

    @Transactional(readOnly = true)
    @Override
    public Comment findCommentById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("Не найден комментарий с идентификатором %d", id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllCommentsForSpecificBook(long bookId) {
        var book = bookService.findBookById(bookId);
        return commentRepository.findAllByBook(book);
    }

    @Transactional
    @Override
    public Comment deleteCommentById(long id) {
        var comment = findCommentById(id);
        commentRepository.deleteById(id);
        return comment;
    }
}
