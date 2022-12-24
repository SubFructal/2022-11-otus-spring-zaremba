package ru.otus.homework.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.homework.configs.LocaleProvider;
import ru.otus.homework.configs.LocalizedCsvResourceNameProvider;
import ru.otus.homework.configs.ScoreToPassProvider;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Класс QuestionDaoCsv")
@SpringBootTest
class QuestionDaoCsvTest {
    private static final String CSV_RESOURCE_NAME = "testQuestions.csv";

    @MockBean
    private LocalizedCsvResourceNameProvider resourceNameProvider;

    @MockBean
    private LocaleProvider localeProvider;

    @MockBean
    private ScoreToPassProvider propertiesProvider;

    @Autowired
    private QuestionDaoCsv questionDao;

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

        given(resourceNameProvider.getLocalizedCsvResourceName()).willReturn(CSV_RESOURCE_NAME);

        assertThat(questionDao.findAll())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedQuestions);
    }
}