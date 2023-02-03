package ru.otus.homework.services.converters;

import ru.otus.homework.models.Comment;

public interface CommentToStringConverter {
    String convertToString(Comment comment);
}
