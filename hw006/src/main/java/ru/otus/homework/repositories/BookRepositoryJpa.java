package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.stereotype.Repository;
import ru.otus.homework.models.Book;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
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
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> getAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("genre-author-entity-graph");
        var query = entityManager.createQuery("select b from Book b order by b.id", Book.class);
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
