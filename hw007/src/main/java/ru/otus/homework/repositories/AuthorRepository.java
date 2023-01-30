package ru.otus.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.homework.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorRepositoryCustom {
    Optional<Author> findByName(String authorName);

    @Override
    @Query("select a from Author a order by a.id")
    List<Author> findAll();
}
