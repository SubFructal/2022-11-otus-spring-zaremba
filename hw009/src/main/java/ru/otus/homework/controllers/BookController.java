package ru.otus.homework.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.models.Book;
import ru.otus.homework.services.BookService;
import ru.otus.homework.services.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;

    @GetMapping("/")
    public String listAllBooksPage(Model model) {
        List<Book> books = bookService.getAllBooks();
        var booksCount = bookService.getBooksCount();
        model.addAttribute("books", books);
        model.addAttribute("booksCount", booksCount);
        return "books";
    }

    @GetMapping("/books-by-author")
    public String listBooksByAuthorPage(@RequestParam(value = "name") String authorName, Model model) {
        List<Book> books = bookService.findAllBooksByAuthor(authorName);
        model.addAttribute("books", books);
        return "books-by-author";
    }

    @GetMapping("/books-by-genre")
    public String listBooksByGenrePage(@RequestParam(value = "name") String genreName, Model model) {
        List<Book> books = bookService.findAllBooksByGenre(genreName);
        model.addAttribute("books", books);
        return "books-by-genre";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam(value = "id") long id) {
        bookService.deleteBookById(id);
        return "redirect:/";
    }

    @GetMapping("/delete-all")
    public String deleteAllBooks() {
        bookService.deleteAllBooks();
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String specificBookPage(@RequestParam(value = "id") long id, Model model) {
        var book = bookService.findBookById(id);
        var comments = commentService.findAllCommentsForSpecificBook(id);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "edit-book";
    }

    @PostMapping("/edit")
    public String editBook(@RequestParam(value = "id") long id,
                       @RequestParam(value = "title") String title,
                       @RequestParam(value = "genre") String genreName,
                       @RequestParam(value = "author") String authorName) {
        bookService.changeBook(id, title, genreName, authorName);
        return "redirect:/";
    }

    @PostMapping("/add")
    public String addBook(@RequestParam(value = "title") String title,
                      @RequestParam(value = "genre") String genreName,
                      @RequestParam(value = "author") String authorName) {
        bookService.addBook(title, genreName, authorName);
        return "redirect:/";
    }
}
