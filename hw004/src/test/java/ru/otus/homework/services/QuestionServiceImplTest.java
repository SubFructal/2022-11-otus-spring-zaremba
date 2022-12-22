package ru.otus.homework.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Класс QuestionServiceImpl")
@SpringBootTest
class QuestionServiceImplTest {
    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private QuestionServiceImpl questionService;

    @DisplayName("должен возвращать корректный список всех вопросов из источника данных")
    @Test
    void shouldReturnCorrectListOfQuestionsFromDataSource() {
        var testQuestions = new ArrayList<Question>();
        testQuestions.add(new Question("question1", List.of(
                new Answer("answer11", true),
                new Answer("answer12", false),
                new Answer("answer13", false),
                new Answer("answer14", false)
        )));
        testQuestions.add(new Question("question2", List.of(
                new Answer("answer21", false),
                new Answer("answer22", true)
        )));
        testQuestions.add(new Question("question3", List.of(
                new Answer("answer31", false),
                new Answer("answer32", true),
                new Answer("answer33", false)
        )));

        given(questionDao.findAll()).willReturn(testQuestions);

        assertThat(questionService.getAllQuestions())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(testQuestions);
    }
}