package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.homework.models.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long count() {
        return entityManager.createQuery(
                "select count(g) from Genre g",
                Long.class).getSingleResult();
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == 0) {
            entityManager.persist(genre);
            return genre;
        }
        return entityManager.merge(genre);
    }

    @Override
    public Optional<Genre> getById(long id) {
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }

    @Override
    public Optional<Genre> getByName(String genreName) {
        var query = entityManager.createQuery(
                "select g from Genre g where g.genreName = :name", Genre.class);
        query.setParameter("name", genreName);
        return query.getResultStream().findFirst();
    }

    @Override
    public List<Genre> getAll() {
        return entityManager.createQuery(
                "select g from Genre g order by g.id",
                Genre.class).getResultList();
    }

    @Override
    public void deleteById(long id) {
        entityManager.remove(entityManager.find(Genre.class, id));
    }

    @Override
    public long deleteAll() {
        return entityManager.createQuery(
                "delete from Genre g").executeUpdate();
    }
}
