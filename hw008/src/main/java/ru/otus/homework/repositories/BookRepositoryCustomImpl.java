package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteByIdCustom(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("book.id").is(id)), Comment.class);
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Book.class);
    }

    @Override
    public void deleteAllCustom() {
        mongoTemplate.remove(new Query(), Comment.class);
        mongoTemplate.remove(new Query(), Book.class);
    }
}
