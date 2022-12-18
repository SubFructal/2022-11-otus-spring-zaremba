package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.configs.ScoreToPassProvider;
import ru.otus.homework.domain.TestResult;

@Service
@RequiredArgsConstructor
public class AppRunnerImpl implements AppRunner {

    private static final String HEADER_DELIMITER = "-------------------------------------------------";

    private final IOService ioService;
    private final QuestionService questionService;
    private final OutputPreparerService outputPreparer;
    private final UserService userService;
    private final TestResultConverter testResultConverter;
    private final ScoreToPassProvider propertiesProvider;
    private final LocalizationMessageService messageService;

    @Override
    public void run() {
        ioService.output(HEADER_DELIMITER);
        ioService.output(messageService.getLocalizedMessage("message.header"));
        ioService.output(HEADER_DELIMITER);
        var user = userService.getUser();
        var testResult = new TestResult(user);

        var questions = questionService.getAllQuestions();
        questions.forEach(question -> {
            var answer = ioService.readInt(outputPreparer.prepareOutput(question)
                    + messageService.getLocalizedMessage("message.enter.answer"));
            var isRight = question.getAnswers().get(answer).isRight();
            testResult.incrementAnswers(isRight);
            ioService.output(isRight ?
                    messageService.getLocalizedMessage("message.right.answer") + "\n" :
                    messageService.getLocalizedMessage("message.wrong.answer") + "\n");
        });
        ioService.output(testResultConverter.convertTestResultToString(testResult,propertiesProvider.getScoreToPass()));
    }
}
