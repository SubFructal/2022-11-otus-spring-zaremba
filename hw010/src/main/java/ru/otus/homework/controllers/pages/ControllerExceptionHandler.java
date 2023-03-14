package ru.otus.homework.controllers.pages;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.homework.exceptions.NotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    public ModelAndView handleNotFoundException(Exception exception) {
        var modelAndView = new ModelAndView("err500");
        modelAndView.addObject("message", exception.getMessage());
        return modelAndView;
    }
}
