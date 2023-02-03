package ru.otus.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.models.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorRepositoryCustom {
    Optional<Author> findByName(String authorName);
}
