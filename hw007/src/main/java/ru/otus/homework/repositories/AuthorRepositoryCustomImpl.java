package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long deleteAllCustom() {
        return entityManager.createQuery(
                "delete from Author a").executeUpdate();
    }
}
