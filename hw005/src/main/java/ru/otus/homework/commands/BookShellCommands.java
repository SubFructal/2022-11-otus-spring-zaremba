package ru.otus.homework.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.models.Book;
import ru.otus.homework.services.BookService;

import java.util.stream.Collectors;

import static java.lang.String.format;

@ShellComponent
@RequiredArgsConstructor
public class BookShellCommands {

    private final BookService bookService;

    @ShellMethod(key = {"get-books-count", "books-count"},
            value = "Возвращает количество всех книг в БД")
    public String getAllBooksCount() {
        var count = bookService.getBooksCount();
        return format("Общее количество книг в БД: %d", count);
    }

    @ShellMethod(key = {"add-new-book-with-ids", "new-book-ids"},
            value = "Добавляет новую книгу в БД: укажите название книги, идентификатор жанра, идентификатор автора")
    public String addNewBookWithIds(String title, long genreId, long authorId) {
        var book = bookService.addBook(title, genreId, authorId);
        return format("Добавлена новая книга: %s", book);
    }

    @ShellMethod(key = {"add-new-book-with-names", "new-book-names"},
            value = "Добавляет новую книгу в БД: укажите название книги, жанр, имя автора")
    public String addNewBookWithNames(String title, String genreName, String authorName) {
        var book = bookService.addBook(title, genreName, authorName);
        return format("Добавлена новая книга: %s", book);
    }

    @ShellMethod(key = {"change-book-with-ids", "change-book-ids"},
            value = "Изменяет существующую в БД книгу: укажите идентификатор книги, название книги, " +
                    "идентификатор жанра, идентификатор автора")
    public String changeBookWithIds(long id, String title, long genreId, long authorId) {
        var book = bookService.changeBook(id, title,genreId, authorId);
        return format("Книга изменена: %s", book);
    }

    @ShellMethod(key = {"change-book-with-names", "change-book-names"},
            value = "Изменяет существующую в БД книгу: укажите идентификатор книги, название книги, жанр, имя автора")
    public String changeBookWithNames(long id, String title, String genreName, String authorName) {
        var book = bookService.changeBook(id, title,genreName, authorName);
        return format("Книга изменена: %s", book);
    }

    @ShellMethod(key = {"find-book-by-id", "book-by-id"},
            value = "Ищет книгу в БД по ее идентификатору: укажите идентификатор книги")
    public String findBookById(long id) {
        var book = bookService.findBookById(id);
        return format("Книга найдена: %s", book);
    }

    @ShellMethod(key = {"show-all-books", "all-books"},
            value = "Выводит список всех книг в БД")
    public String showAllBooks() {
        var authors = bookService.getAllBooks();
        return authors.stream().map(Book::toString).collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = {"show-books-by-genre", "books-by-genre"},
            value = "Выводит список всех книг определенного жанра: укажите жанр")
    public String showAllBooksByGenre(String genreName) {
        var authors = bookService.findAllBooksByGenre(genreName);
        return authors.stream().map(Book::toString).collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = {"show-books-by-author", "books-by-author"},
            value = "Выводит список всех книг определенного автора: укажите имя автора")
    public String showAllBooksByAuthor(String authorName) {
        var authors = bookService.findAllBooksByAuthor(authorName);
        return authors.stream().map(Book::toString).collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = {"delete-book-from-db", "delete-book"},
            value = "Удаляет книгу из БД по ее идентификатору: укажите идентификатор книги")
    public String deleteBookFromDatabase(long id) {
        var book = bookService.deleteBookById(id);
        return format("Книга удалена: %s", book);
    }

    @ShellMethod(key = {"delete-all-books-from-db", "delete-all-books"},
            value = "Удаляет все книги из БД")
    public String deleteAllBooks() {
        var count = bookService.deleteAllBooks();
        return format("%d книг удалено", count);
    }
}
