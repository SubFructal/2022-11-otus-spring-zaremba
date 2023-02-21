package ru.otus.homework.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.models.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String>, GenreRepositoryCustom {
    Optional<Genre> findByGenreName(String genreName);
}
