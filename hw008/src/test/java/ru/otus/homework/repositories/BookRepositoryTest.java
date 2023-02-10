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

@DisplayName("BookRepository")
//@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DataMongoTest
class BookRepositoryTest {

    private static final String EXISTING_BOOK_ID = "3";

    private List<Author> authors;
    private List<Genre> genres;
    private List<Book> books;
    private List<Comment> comments;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BookRepository bookRepository;

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

    @DisplayName("должен каскадно удалять заданную книгу по ее идентификатору и все связанные с ней сущности")
    @Test
    void shouldCascadeDeleteBookById() {
        var existingBook = mongoTemplate.findById(EXISTING_BOOK_ID, Book.class);
        var existingComments = mongoTemplate.find(
                Query.query(Criteria.where("book").is(existingBook)), Comment.class);

        assertThat(existingComments).isNotNull().hasSize(2);

        bookRepository.deleteByIdCustom(EXISTING_BOOK_ID);

        var existingCommentsIds = existingComments.stream().map(Comment::getId).collect(Collectors.toList());

        var deletedComments = mongoTemplate.find(
                Query.query(Criteria.where("id").in(existingCommentsIds)), Comment.class);

        var deletedBook = mongoTemplate.findById(EXISTING_BOOK_ID, Book.class);

        assertThat(deletedComments).isNotNull().hasSize(0);
        assertThat(deletedBook).isNull();
    }

    @DisplayName("должен каскадно удалять все книги и все связанные с каждой книгой сущности")
    @Test
    void shouldCascadeDeleteForAllBooks() {
        var existingBooks = mongoTemplate.findAll(Book.class);
        var existingComments = mongoTemplate.findAll(Comment.class);

        assertThat(existingBooks).isNotNull().hasSize(4);
        assertThat(existingComments).isNotNull().hasSize(5);

        bookRepository.deleteAllCustom();

        var deletedComments = mongoTemplate.findAll(Comment.class);
        var deletedBooks = mongoTemplate.findAll(Book.class);

        assertThat(deletedComments).isNotNull().hasSize(0);
        assertThat(deletedBooks).isNotNull().hasSize(0);
    }
}