package ru.otus.homework.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс QuestionDaoCsv")
class QuestionDaoCsvTest {
    private static final String CSV_RESOURCE_NAME = "testQuestions.csv";

    private QuestionDaoCsv questionDao;

    @BeforeEach
    void setUp() {
        questionDao = new QuestionDaoCsv(CSV_RESOURCE_NAME);
    }

    @DisplayName("должен корректно читать csv-файл")
    @Test
    void shouldReadCsvFileCorrectly() {
        var expectedQuestions = new ArrayList<Question>();
        expectedQuestions.add(new Question("question1", List.of(
                new Answer("answer11", true),
                new Answer("answer12", false),
                new Answer("answer13", false),
                new Answer("answer14", false)
        )));
        expectedQuestions.add(new Question("question2", List.of(
                new Answer("answer21", false),
                new Answer("answer22", true)
        )));
        expectedQuestions.add(new Question("question3", List.of(
                new Answer("answer31", false),
                new Answer("answer32", true),
                new Answer("answer33", false)
        )));

        assertThat(questionDao.findAll())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedQuestions);
    }
}