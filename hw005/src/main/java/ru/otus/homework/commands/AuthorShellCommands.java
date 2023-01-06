package ru.otus.homework.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.models.Author;
import ru.otus.homework.services.AuthorService;

import java.util.stream.Collectors;

import static java.lang.String.*;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellCommands {

    private final AuthorService authorService;

    @ShellMethod(key = {"get-authors-count", "authors-count"},
            value = "Возвращает количество всех авторов в БД")
    public String getAllAuthorsCount() {
        var count = authorService.getAuthorsCount();
        return format("Общее количество авторов в БД: %d", count);
    }

    @ShellMethod(key = {"add-new-author", "new-author"},
            value = "Добавляет нового автора в БД: укажите имя автора")
    public String addNewAuthor(String authorName) {
        var author = authorService.addAuthor(authorName);
        return format("Добавлен новый автор: %s", author);
    }

    @ShellMethod(key = {"change-author"},
            value = "Изменяет существующего в БД автора: укажите идентификатор автора, имя автора")
    public String changeAuthor(long id, String authorName) {
        var author = authorService.changeAuthor(id, authorName);
        return format("Автор изменен: %s", author);
    }

    @ShellMethod(key = {"find-author-by-id", "author-by-id"},
            value = "Ищет автора в БД по его идентификатору: укажите идентификатор автора")
    public String findAuthorById(long id) {
        var author = authorService.findAuthorById(id);
        return format("Автор найден: %s", author);
    }

    @ShellMethod(key = {"find-author-by-name", "author-by-name"},
            value = "Ищет автора в БД по его имени: укажите имя автора")
    public String findAuthorByName(String authorName) {
        var author = authorService.findAuthorByName(authorName);
        return format("Автор найден: %s", author);
    }

    @ShellMethod(key = {"show-all-authors", "all-authors"},
            value = "Выводит список всех авторов в БД")
    public String showAllAuthors() {
        var authors = authorService.getAllAuthors();
        return authors.stream().map(Author::toString).collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = {"delete-author-from-db", "delete-author"},
            value = "Удаляет автора из БД по его идентификатору: укажите идентификатор автора")
    public String deleteAuthorFromDatabase(long id) {
        var author = authorService.deleteAuthorById(id);
        return format("Автор удален: %s", author);
    }

    @ShellMethod(key = {"delete-all-authors-from-db", "delete-all-authors"},
            value = "Удаляет всех авторов из БД")
    public String deleteAllAuthors() {
        var count = authorService.deleteAllAuthors();
        return format("%d авторов удалено", count);
    }
}
