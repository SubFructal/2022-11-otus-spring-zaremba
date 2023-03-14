package ru.otus.homework.controllers.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Comment;

@Data
@AllArgsConstructor
public class CommentDto {
    private long id;
    private String commentText;

    public static CommentDto transformDomainToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getCommentText());
    }
}
