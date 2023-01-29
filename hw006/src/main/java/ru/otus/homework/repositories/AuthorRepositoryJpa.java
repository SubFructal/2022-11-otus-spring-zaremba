package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.homework.models.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long count() {
        return entityManager.createQuery(
                "select count(a) from Author a",
                Long.class).getSingleResult();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == 0) {
            entityManager.persist(author);
            return author;
        }
        return entityManager.merge(author);
    }

    @Override
    public Optional<Author> getById(long id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }

    @Override
    public Optional<Author> getByName(String authorName) {
        var query = entityManager.createQuery(
                "select a from Author a where a.name = :name", Author.class);
        query.setParameter("name", authorName);
        return query.getResultStream().findFirst();
    }

    @Override
    public List<Author> getAll() {
        return entityManager.createQuery(
                "select a from Author a order by a.id",
                Author.class).getResultList();
    }

    @Override
    public void deleteById(long id) {
        entityManager.remove(entityManager.find(Author.class, id));
    }

    @Override
    public long deleteAll() {
        return entityManager.createQuery(
                "delete from Author a").executeUpdate();
    }
}
