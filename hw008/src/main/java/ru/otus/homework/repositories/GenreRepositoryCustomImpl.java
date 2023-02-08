package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;
import ru.otus.homework.models.Genre;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteByIdCustom(String id) {
        var genre = mongoTemplate.findById(id, Genre.class);
        var books = mongoTemplate.find(Query.query(Criteria.where("genre").is(genre)), Book.class);
        var comments = mongoTemplate.find(Query.query(Criteria.where("book").in(books)), Comment.class);

        var commentIds = comments.stream().map(Comment::getId).collect(Collectors.toList());
        var bookIds = books.stream().map(Book::getId).collect(Collectors.toList());

        mongoTemplate.remove(Query.query(Criteria.where("id").in(commentIds)), Comment.class);
        mongoTemplate.remove(Query.query(Criteria.where("id").in(bookIds)), Book.class);
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Genre.class);
    }

    @Override
    public void deleteAllCustom() {
        mongoTemplate.remove(new Query(), Comment.class);
        mongoTemplate.remove(new Query(), Book.class);
        mongoTemplate.remove(new Query(), Genre.class);
    }
}
