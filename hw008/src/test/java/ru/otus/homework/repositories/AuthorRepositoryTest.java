package ru.otus.homework.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;
import ru.otus.homework.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuthorRepository")
//@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DataMongoTest
class AuthorRepositoryTest {

    private static final String EXISTING_AUTHOR_ID = "3";

    private List<Author> authors;
    private List<Genre> genres;
    private List<Book> books;
    private List<Comment> comments;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        authors = mongoTemplate.findAll(Author.class);
        genres = mongoTemplate.findAll(Genre.class);
        books = mongoTemplate.findAll(Book.class);
        comments = mongoTemplate.findAll(Comment.class);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.getDb().drop();

        mongoTemplate.insertAll(authors);
        mongoTemplate.insertAll(genres);
        mongoTemplate.insertAll(books);
        mongoTemplate.insertAll(comments);
    }

    @DisplayName("должен каскадно удалять заданного автора по его идентификатору и все связанные с ним сущности")
    @Test
    void shouldCascadeDeleteAuthorById() {
        var existingAuthor = mongoTemplate.findById(EXISTING_AUTHOR_ID, Author.class);
        var existingBooks = mongoTemplate.find(
                Query.query(Criteria.where("author").is(existingAuthor)), Book.class);
        var existingComments = mongoTemplate.find(
                Query.query(Criteria.where("book").in(existingBooks)), Comment.class);

        assertThat(existingBooks).isNotNull().hasSize(2);
        assertThat(existingComments).isNotNull().hasSize(3);

        authorRepository.deleteByIdCustom(EXISTING_AUTHOR_ID);

        var existingCommentsIds = existingComments.stream().map(Comment::getId).collect(Collectors.toList());
        var existingBooksIds = existingBooks.stream().map(Book::getId).collect(Collectors.toList());

        var deletedComments = mongoTemplate.find(
                Query.query(Criteria.where("id").in(existingCommentsIds)), Comment.class);

        var deletedBooks = mongoTemplate.find(
                Query.query(Criteria.where("id").in(existingBooksIds)), Book.class);

        var deletedAuthor = mongoTemplate.findById(EXISTING_AUTHOR_ID, Author.class);

        assertThat(deletedComments).isNotNull().hasSize(0);
        assertThat(deletedBooks).isNotNull().hasSize(0);
        assertThat(deletedAuthor).isNull();
    }

    @DisplayName("должен каскадно удалять всех авторов и все связанные с каждым автором сущности")
    @Test
    void shouldCascadeDeleteForAllAuthors() {
        var existingAuthors = mongoTemplate.findAll(Author.class);
        var existingBooks = mongoTemplate.findAll(Book.class);
        var existingComments = mongoTemplate.findAll(Comment.class);

        assertThat(existingAuthors).isNotNull().hasSize(3);
        assertThat(existingBooks).isNotNull().hasSize(4);
        assertThat(existingComments).isNotNull().hasSize(5);

        authorRepository.deleteAllCustom();

        var deletedComments = mongoTemplate.findAll(Comment.class);
        var deletedBooks = mongoTemplate.findAll(Book.class);
        var deletedAuthors = mongoTemplate.findAll(Author.class);

        assertThat(deletedComments).isNotNull().hasSize(0);
        assertThat(deletedBooks).isNotNull().hasSize(0);
        assertThat(deletedAuthors).isNotNull().hasSize(0);
    }
}