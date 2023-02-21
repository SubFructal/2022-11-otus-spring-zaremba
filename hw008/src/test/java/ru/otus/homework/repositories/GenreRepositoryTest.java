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

@DisplayName("GenreRepository")
//@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DataMongoTest
class GenreRepositoryTest {

    private static final String EXISTING_GENRE_ID = "2";

    private List<Author> authors;
    private List<Genre> genres;
    private List<Book> books;
    private List<Comment> comments;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private GenreRepository genreRepository;

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

    @DisplayName("должен каскадно удалять заданный жанр по его идентификатору и все связанные с ним сущности")
    @Test
    void shouldCascadeDeleteGenreById() {
        var existingGenre = mongoTemplate.findById(EXISTING_GENRE_ID, Genre.class);
        var existingBooks = mongoTemplate.find(
                Query.query(Criteria.where("genre").is(existingGenre)), Book.class);
        var existingComments = mongoTemplate.find(
                Query.query(Criteria.where("book").in(existingBooks)), Comment.class);

        assertThat(existingBooks).isNotNull().hasSize(2);
        assertThat(existingComments).isNotNull().hasSize(2);

        genreRepository.deleteByIdCustom(EXISTING_GENRE_ID);

        var existingCommentsIds = existingComments.stream().map(Comment::getId).collect(Collectors.toList());
        var existingBooksIds = existingBooks.stream().map(Book::getId).collect(Collectors.toList());

        var deletedComments = mongoTemplate.find(
                Query.query(Criteria.where("id").in(existingCommentsIds)), Comment.class);

        var deletedBooks = mongoTemplate.find(
                Query.query(Criteria.where("id").in(existingBooksIds)), Book.class);

        var deletedGenre = mongoTemplate.findById(EXISTING_GENRE_ID, Genre.class);

        assertThat(deletedComments).isNotNull().hasSize(0);
        assertThat(deletedBooks).isNotNull().hasSize(0);
        assertThat(deletedGenre).isNull();
    }

    @DisplayName("должен каскадно удалять все жанры и все связанные с каждым жанром сущности")
    @Test
    void shouldCascadeDeleteForAllGenres() {
        var existingGenres = mongoTemplate.findAll(Author.class);
        var existingBooks = mongoTemplate.findAll(Book.class);
        var existingComments = mongoTemplate.findAll(Comment.class);

        assertThat(existingGenres).isNotNull().hasSize(3);
        assertThat(existingBooks).isNotNull().hasSize(4);
        assertThat(existingComments).isNotNull().hasSize(5);

        genreRepository.deleteAllCustom();

        var deletedComments = mongoTemplate.findAll(Comment.class);
        var deletedBooks = mongoTemplate.findAll(Book.class);
        var deletedGenres = mongoTemplate.findAll(Genre.class);

        assertThat(deletedComments).isNotNull().hasSize(0);
        assertThat(deletedBooks).isNotNull().hasSize(0);
        assertThat(deletedGenres).isNotNull().hasSize(0);
    }
}