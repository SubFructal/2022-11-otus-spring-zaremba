package ru.otus.homework.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;

@DisplayName("Класс QuestionDaoCsv")
class QuestionDaoCsvTest {

    private static final String CSV_RESOURCE_NAME = "testQuestions.csv";

    @DisplayName("должен корректно читать csv-файл")
    @Test
    void shouldReadCsvFileCorrectly() {
        var expectedQuestions = new ArrayList<Question>();
        expectedQuestions.add(new Question("question1", Arrays.asList(
                new Answer("answer11", true),
                new Answer("answer12", false),
                new Answer("answer13", false),
                new Answer("answer14", false)
        )));
        expectedQuestions.add(new Question("question2", Arrays.asList(
                new Answer("answer21", false),
                new Answer("answer22", true)
        )));
        expectedQuestions.add(new Question("question3", Arrays.asList(
                new Answer("answer31", false),
                new Answer("answer32", true),
                new Answer("answer33", false)
        )));

        var questionDao = new QuestionDaoCsv(CSV_RESOURCE_NAME);

        assertThat(questionDao.findAll()).containsAll(expectedQuestions);
    }
}