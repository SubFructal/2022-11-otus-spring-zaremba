package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.stereotype.Repository;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long count() {
        return entityManager.createQuery(
                "select count(b) from Book b",
                Long.class).getSingleResult();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public Optional<Book> getById(long id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("genre-author-entity-graph");
        var query = entityManager.createQuery("select b from Book b where b.id = :id", Book.class);
        query.setParameter("id", id);
        query.setHint(EntityGraphType.FETCH.getKey(), entityGraph);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> getAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("genre-author-entity-graph");
        var query = entityManager.createQuery("select b from Book b order by b.id", Book.class);
        query.setHint(EntityGraphType.FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public List<Book> getAllForSomeGenre(Genre genre) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("genre-author-entity-graph");
        var query = entityManager.createQuery(
                "select b from Book b where b.genre = :genre", Book.class);
        query.setParameter("genre", genre);
        query.setHint(EntityGraphType.FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public List<Book> getAllForSomeAuthor(Author author) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("genre-author-entity-graph");
        var query = entityManager.createQuery(
                "select b from Book b where b.author = :author", Book.class);
        query.setParameter("author", author);
        query.setHint(EntityGraphType.FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        entityManager.remove(entityManager.find(Book.class, id));
    }

    @Override
    public long deleteAll() {
        return entityManager.createQuery(
                "delete from Book b").executeUpdate();
    }
}
