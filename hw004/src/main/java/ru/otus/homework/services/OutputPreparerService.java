package ru.otus.homework.services;

import ru.otus.homework.domain.Question;

public interface OutputPreparerService {
    String prepareOutput(Question question);
}
