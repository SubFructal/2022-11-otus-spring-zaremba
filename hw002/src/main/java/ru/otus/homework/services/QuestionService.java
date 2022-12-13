package ru.otus.homework.services;

import ru.otus.homework.domain.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestions();
}
