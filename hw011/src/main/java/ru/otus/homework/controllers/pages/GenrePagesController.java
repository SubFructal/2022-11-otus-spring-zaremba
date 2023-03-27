package ru.otus.homework.controllers.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenrePagesController {

    @GetMapping(value = "/genres")
    public String listAllGenresPage() {
        return "genres";
    }
}
