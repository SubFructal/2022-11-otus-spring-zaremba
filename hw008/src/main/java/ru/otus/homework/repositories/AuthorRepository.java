package ru.otus.homework.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.models.Author;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String>, AuthorRepositoryCustom {
    Optional<Author> findByName(String authorName);
}
