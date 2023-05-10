package ru.otus.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.library.models.Book;
import ru.otus.library.models.Comment;
import ru.otus.library.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CommentRepository")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CommentRepositoryTest {

    private static final String NEW_COMMENT_TEXT = "Новый комментарий";
    private static final String UPDATED_COMMENT_TEXT = "Обновленный комментарий";
    private static final long TEST_BOOK_ID = 1;
    private static final long EXISTING_COMMENT_ID = 1;

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private CommentRepository commentRepository;


    @DisplayName("должен добавлять комментарий в БД")
    @Test
    void shouldInsertComment() {
        var expectedComment = new Comment();
        expectedComment.setCommentText(NEW_COMMENT_TEXT);
        var testBook = testEntityManager.find(Book.class, TEST_BOOK_ID);
        expectedComment.setBook(testBook);

        commentRepository.save(expectedComment);
        assertThat(expectedComment.getId()).isGreaterThan(0);

        var actualComment = testEntityManager.find(Comment.class, expectedComment.getId());

        assertThat(actualComment)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);

        assertThat(actualComment)
                .matches(comment -> comment.getId() == 4)
                .matches(comment -> comment.getCommentText().equals(NEW_COMMENT_TEXT))
                .matches(comment -> comment.getBook() != null
                        && comment.getBook().getId() == TEST_BOOK_ID);
    }

    @DisplayName("должен изменять имеющийся в БД комментарий без отключения объекта комментария от контекста")
    @Test
    void shouldUpdateComment() {
        var comment = testEntityManager.find(Comment.class, EXISTING_COMMENT_ID);
        comment.setCommentText(UPDATED_COMMENT_TEXT);
        testEntityManager.flush();
        var updatedComment = testEntityManager.find(Comment.class, EXISTING_COMMENT_ID);

        assertThat(updatedComment.getCommentText()).isEqualTo(UPDATED_COMMENT_TEXT);
    }

    @DisplayName("должен изменять имеющийся в БД комментарий при отключении объекта комментария от контекста")
    @Test
    void shouldUpdateCommentWithDetaching() {
        var comment = testEntityManager.find(Comment.class, EXISTING_COMMENT_ID);
        testEntityManager.detach(comment);
        comment.setCommentText(UPDATED_COMMENT_TEXT);
        commentRepository.save(comment);
        var updatedComment = testEntityManager.find(Comment.class, EXISTING_COMMENT_ID);

        assertThat(updatedComment.getCommentText()).isEqualTo(UPDATED_COMMENT_TEXT);
    }

    @DisplayName("должен возвращать ожидаемый комментарий по идентификатору")
    @Test
    void shouldReturnExpectedCommentById() {
        var optionalActualComment = commentRepository.findById(EXISTING_COMMENT_ID);
        var expectedComment = testEntityManager.find(Comment.class, EXISTING_COMMENT_ID);

        assertThat(optionalActualComment)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен возвращать все комментарии для определенной книги")
    @Test
    void shouldReturnAllCommentsForSomeBook() {
        var testBook = testEntityManager.find(Book.class, 2L);
        var comments = commentRepository.findAllByBook(testBook);

        assertThat(comments).isNotNull().hasSize(2)
                .allMatch(comment -> !comment.getCommentText().equals(""))
                .allMatch(comment -> comment.getBook() != null)
                .containsOnlyOnce(testEntityManager.find(Comment.class, 2L))
                .containsOnlyOnce(testEntityManager.find(Comment.class, 3L))
                .doesNotContain(testEntityManager.find(Comment.class, 1L));
    }

    @DisplayName("должен удалять заданный комментарий по его идентификатору")
    @Test
    void shouldDeleteCommentById() {
        var existingComment = testEntityManager.find(Genre.class, EXISTING_COMMENT_ID);
        assertThat(existingComment).isNotNull();

        commentRepository.deleteById(EXISTING_COMMENT_ID);

        var deletedComment = testEntityManager.find(Comment.class, EXISTING_COMMENT_ID);
        assertThat(deletedComment).isNull();
    }
}