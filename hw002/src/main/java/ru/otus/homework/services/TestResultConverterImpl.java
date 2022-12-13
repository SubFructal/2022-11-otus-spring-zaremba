package ru.otus.homework.services;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.TestResult;

@Service
public class TestResultConverterImpl implements TestResultConverter {

    private static final String TEST_RESULT_PATTERN = "Dear %s %s. Total questions amount: %d. " +
            "Right answers: %d, must be at least %d. %s";
    private static final String MSG_TEST_PASSED = "Test passed. Congratulations!";
    private static final String MSG_TEST_FAILED = "Test failed. Try again.";

    @Override
    public String convertTestResultToString(TestResult testResult, int scoreToPass) {
        var userFirstName = testResult.getUserFirstName();
        var userLastName = testResult.getUserLastName();
        var rightAnswers = testResult.getRightAnswers();
        var total = testResult.getTotal();
        return String.format(TEST_RESULT_PATTERN, userFirstName, userLastName, total,
                rightAnswers, scoreToPass, determineTestResult(rightAnswers, scoreToPass));
    }

    private String determineTestResult(int rightAnswers, int scoreToPass) {
        return rightAnswers >= scoreToPass ? MSG_TEST_PASSED : MSG_TEST_FAILED;
    }
}
