package ru.otus.homework.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.services.BookService;
import ru.otus.homework.services.converters.BookToStringConverter;

import java.util.stream.Collectors;

import static java.lang.String.format;

@ShellComponent
@RequiredArgsConstructor
public class BookShellCommands {

    private final BookService bookService;
    private final BookToStringConverter converter;

    @ShellMethod(key = {"get-books-count", "books-count"},
            value = "Возвращает количество всех книг в БД")
    public String getAllBooksCount() {
        var count = bookService.getBooksCount();
        return format("Общее количество книг в БД: %d", count);
    }

    @ShellMethod(key = {"add-new-book-with-ids", "new-book-ids"},
            value = "Добавляет новую книгу в БД: укажите название книги, идентификатор жанра, идентификатор автора")
    public String addNewBookWithIds(String title, String genreId, String authorId) {
        var book = bookService.addBookByIds(title, genreId, authorId);
        return format("Добавлена новая книга: %s", converter.convertToString(book));
    }

    @ShellMethod(key = {"add-new-book-with-names", "new-book-names"},
            value = "Добавляет новую книгу в БД: укажите название книги, жанр, имя автора")
    public String addNewBookWithNames(String title, String genreName, String authorName) {
        var book = bookService.addBookByNames(title, genreName, authorName);
        return format("Добавлена новая книга: %s", converter.convertToString(book));
    }

    @ShellMethod(key = {"change-book-with-ids", "change-book-ids"},
            value = "Изменяет существующую в БД книгу: укажите идентификатор книги, название книги, " +
                    "идентификатор жанра, идентификатор автора")
    public String changeBookWithIds(String id, String title, String genreId, String authorId) {
        var book = bookService.changeBookByIds(id, title, genreId, authorId);
        return format("Книга изменена: %s", converter.convertToString(book));
    }

    @ShellMethod(key = {"change-book-with-names", "change-book-names"},
            value = "Изменяет существующую в БД книгу: укажите идентификатор книги, название книги, жанр, имя автора")
    public String changeBookWithNames(String id, String title, String genreName, String authorName) {
        var book = bookService.changeBookByNames(id, title, genreName, authorName);
        return format("Книга изменена: %s", converter.convertToString(book));
    }

    @ShellMethod(key = {"find-book-by-id", "book-by-id"},
            value = "Ищет книгу в БД по ее идентификатору: укажите идентификатор книги")
    public String findBookById(String id) {
        var book = bookService.findBookById(id);
        return format("Книга найдена: %s", converter.convertToString(book));
    }

    @ShellMethod(key = {"show-all-books", "all-books"},
            value = "Выводит список всех книг в БД")
    public String showAllBooks() {
        var books = bookService.getAllBooks();
        return books.stream().map(converter::convertToString).collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = {"show-books-by-genre", "books-by-genre"},
            value = "Выводит список всех книг определенного жанра: укажите жанр")
    public String showAllBooksByGenre(String genreName) {
        var books = bookService.findAllBooksByGenre(genreName);
        return books.stream().map(converter::convertToString).collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = {"show-books-by-author", "books-by-author"},
            value = "Выводит список всех книг определенного автора: укажите имя автора")
    public String showAllBooksByAuthor(String authorName) {
        var books = bookService.findAllBooksByAuthor(authorName);
        return books.stream().map(converter::convertToString).collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = {"delete-book-from-db", "delete-book"},
            value = "Удаляет книгу из БД по ее идентификатору: укажите идентификатор книги")
    public String deleteBookFromDatabase(String id) {
        var book = bookService.deleteBookById(id);
        return format("Книга удалена: %s", converter.convertToString(book));
    }

    @ShellMethod(key = {"delete-all-books-from-db", "delete-all-books"},
            value = "Удаляет все книги из БД")
    public String deleteAllBooks() {
        var count = bookService.deleteAllBooks();
        return format("%d книг удалено", count);
    }
}
