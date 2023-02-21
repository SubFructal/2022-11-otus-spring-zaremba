package ru.otus.homework.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.services.CommentService;
import ru.otus.homework.services.converters.CommentToStringConverter;

import java.util.stream.Collectors;

import static java.lang.String.format;

@ShellComponent
@RequiredArgsConstructor
public class CommentsShellCommands {

    private final CommentService commentService;
    private final CommentToStringConverter converter;

    @ShellMethod(key = {"add-new-comment", "new-comment"},
            value = "Добавляет новый комментарий к книге: укажите идентификатор книги и текст комментария")
    public String addNewComment(String bookId, String commentText) {
        var comment = commentService.addComment(bookId, commentText);
        return format("Добавлен новый комментарий: %s", converter.convertToString(comment));
    }

    @ShellMethod(key = {"change-comment"},
            value = "Изменяет существующий комментарий к книге: укажите идентификатор комментария и текст комментария")
    public String changeComment(String id, String commentText) {
        var comment = commentService.changeComment(id, commentText);
        return format("Комментарий изменен: %s", converter.convertToString(comment));
    }

    @ShellMethod(key = {"find-comment-by-id", "comment-by-id"},
            value = "Ищет комментарий к книге по его идентификатору: укажите идентификатор комментария")
    public String findCommentById(String id) {
        var comment = commentService.findCommentById(id);
        return format("Комментарий найден: %s", converter.convertToString(comment));
    }

    @ShellMethod(key = {"show-all-comments-for-book", "comments-for-book"},
            value = "Выводит список всех комментариев к определенной книге: укажите идентификатор книги")
    public String showAllCommentsForBook(String bookId) {
        var comments = commentService.findAllCommentsForSpecificBook(bookId);
        return comments.stream().map(converter::convertToString).collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = {"delete-comment-from-db", "delete-comment"},
            value = "Удаляет комментарий к книге по его идентификатору: укажите идентификатор комментария")
    public String deleteCommentFromDatabase(String id) {
        var comment = commentService.deleteCommentById(id);
        return format("Комментарий удален: %s", converter.convertToString(comment));
    }
}
