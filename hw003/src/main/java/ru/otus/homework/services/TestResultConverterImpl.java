package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestResultConverterImpl implements TestResultConverter {

    private final LocalizationMessageService messageService;

    @Override
    public String convertTestResultToString(TestResult testResult, int scoreToPass) {
        var userFirstName = testResult.getUserFirstName();
        var userLastName = testResult.getUserLastName();
        var rightAnswers = testResult.getRightAnswers();
        var total = testResult.getTotal();
        return String.format(messageService.getLocalizedMessage("test.result.pattern"), userFirstName,
                userLastName, total, rightAnswers, scoreToPass, determineTestResult(rightAnswers, scoreToPass));
    }

    private String determineTestResult(int rightAnswers, int scoreToPass) {
        return rightAnswers >= scoreToPass ?
                messageService.getLocalizedMessage("message.test.passed") :
                messageService.getLocalizedMessage("message.test.failed");
    }
}
