package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao dao;

    @Override
    public List<Question> getAllQuestions() {
        return dao.findAll();
    }
}
