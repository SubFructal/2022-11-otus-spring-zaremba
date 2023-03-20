package ru.otus.homework.controllers.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorPagesController {

    @GetMapping(value = "/authors")
    public String listAllAuthorsPage() {
        return "authors";
    }
}
