package ru.otus.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.homework.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long>, GenreRepositoryCustom {
    Optional<Genre> findByGenreName(String genreName);

    @Override
    @Query("select g from Genre g order by g.id")
    List<Genre> findAll();
}
