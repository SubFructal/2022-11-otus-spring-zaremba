package ru.otus.homework.services;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Question;

@Service
public class OutputPreparerServiceImpl implements OutputPreparerService {
    @Override
    public String prepareOutput(Question question) {
        var builder = new StringBuilder(question.getQuestionText() + "\n");
        question.getAnswers().forEach(answer -> builder
                .append("   ")
                .append(question.getAnswers().indexOf(answer))
                .append(". ")
                .append(answer.getAnswerText())
                .append("\n"));
        return builder.toString();
    }
}
