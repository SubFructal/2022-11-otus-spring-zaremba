package ru.otus.homework.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;
import ru.otus.homework.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс CommentRepositoryJpa")
@DataJpaTest
@Import(value = {CommentRepositoryJpa.class})
class CommentRepositoryJpaTest {

    private static final String NEW_COMMENT_TEXT = "Новый комментарий";
    private static final String UPDATED_COMMENT_TEXT = "Обновленный комментарий";
    private static final long TEST_BOOK_ID = 1;
    private static final long EXISTING_COMMENT_ID = 1;

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;


    @DisplayName("должен добавлять комментарий в БД")
    @Test
    void shouldInsertComment() {
        var expectedComment = new Comment();
        expectedComment.setCommentText(NEW_COMMENT_TEXT);
        var testBook = testEntityManager.find(Book.class, TEST_BOOK_ID);
        expectedComment.setBook(testBook);

        commentRepositoryJpa.save(expectedComment);
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

    @DisplayName("должен изменять имеющийся в БД комментарий при отключении объекта комментарий от контекста")
    @Test
    void shouldUpdateCommentWithDetaching() {
        var comment = testEntityManager.find(Comment.class, EXISTING_COMMENT_ID);
        testEntityManager.detach(comment);
        comment.setCommentText(UPDATED_COMMENT_TEXT);
        commentRepositoryJpa.save(comment);
        var updatedComment = testEntityManager.find(Comment.class, EXISTING_COMMENT_ID);

        assertThat(updatedComment.getCommentText()).isEqualTo(UPDATED_COMMENT_TEXT);
    }
    
    @DisplayName("должен возвращать ожидаемый комментарий по идентификатору")
    @Test
    void shouldReturnExpectedCommentById() {
        var optionalActualComment = commentRepositoryJpa.getById(EXISTING_COMMENT_ID);
        var expectedComment = testEntityManager.find(Comment.class, EXISTING_COMMENT_ID);

        assertThat(optionalActualComment)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять заданный комментарий по его идентификатору")
    @Test
    void shouldDeleteCommentById() {
        var existingComment = testEntityManager.find(Genre.class, EXISTING_COMMENT_ID);
        assertThat(existingComment).isNotNull();

        commentRepositoryJpa.deleteById(EXISTING_COMMENT_ID);

        var deletedComment = testEntityManager.find(Comment.class, EXISTING_COMMENT_ID);
        assertThat(deletedComment).isNull();
    }
}