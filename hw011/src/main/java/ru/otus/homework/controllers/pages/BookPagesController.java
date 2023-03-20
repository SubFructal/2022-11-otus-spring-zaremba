package ru.otus.homework.controllers.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookPagesController {

    @GetMapping(value = "/")
    public String listAllBooksPage() {
        return "books";
    }

    @GetMapping(value = "/books-by-author")
    public String listBooksByAuthorPage() {
        return "books-by-author";
    }

    @GetMapping(value = "/books-by-genre")
    public String listBooksByGenrePage() {
        return "books-by-genre";
    }

    @GetMapping(value = "/edit")
    public String specificBookPage() {
        return "edit-book";
    }
}
