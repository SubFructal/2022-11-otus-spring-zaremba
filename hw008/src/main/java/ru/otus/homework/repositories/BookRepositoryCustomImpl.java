package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteByIdCustom(String id) {
        var book = mongoTemplate.findById(id, Book.class);
        var comments = mongoTemplate.find(Query.query(Criteria.where("book").is(book)), Comment.class);

        var commentIds = comments.stream().map(Comment::getId).collect(Collectors.toList());

        mongoTemplate.remove(Query.query(Criteria.where("id").in(commentIds)), Comment.class);
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Book.class);
    }

    @Override
    public void deleteAllCustom() {
        mongoTemplate.remove(new Query(), Comment.class);
        mongoTemplate.remove(new Query(), Book.class);
    }
}
