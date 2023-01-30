package ru.otus.homework.services.converters;

import org.springframework.stereotype.Service;
import ru.otus.homework.models.Comment;

@Service
public class CommentToStringConverterImpl implements CommentToStringConverter {
    @Override
    public String convertToString(Comment comment) {
        return "Comment(id=" + comment.getId() + ", commentText=" + comment.getCommentText() + ")";
    }
}
