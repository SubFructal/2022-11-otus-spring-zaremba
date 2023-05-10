package ru.otus.client.services;

import ru.otus.client.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> findAllCommentsForSpecificBook(long bookId);
}
