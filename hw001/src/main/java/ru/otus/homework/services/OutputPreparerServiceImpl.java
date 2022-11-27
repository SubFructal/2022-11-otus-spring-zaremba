package ru.otus.homework.services;

import ru.otus.homework.domain.Question;

public class OutputPreparerServiceImpl implements OutputPreparerService {
    @Override
    public String prepareOutput(Question question) {
        var builder = new StringBuilder(question.getQuestionText() + "\n");
        question.getAnswers().forEach(answer -> builder.append("   ").append(answer.getAnswerText()).append("\n"));
        return builder.toString();
    }
}
