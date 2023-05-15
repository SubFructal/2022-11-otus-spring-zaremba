package ru.otus.client.services;

import ru.otus.client.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> getAllAuthors();
}
