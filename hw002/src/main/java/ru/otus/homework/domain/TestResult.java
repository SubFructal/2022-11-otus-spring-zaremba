package ru.otus.homework.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestResult {

    private static final String TEST_RESULT_PATTERN = "Dear %s %s. Total questions amount: %d. " +
            "Right answers: %d, must be at least %d. %s";
    private static final String MSG_TEST_PASSED = "Test passed. Congratulations!";
    private static final String MSG_TEST_FAILED = "Test failed. Try again.";

    private final User user;
    private final int scoreToPass;
    private int total;
    private int rightAnswers;

    public void incrementAnswers(boolean mustIncremented) {
        total++;
        if (mustIncremented) {
            rightAnswers++;
        }
    }

    private String determineTestResult() {
        return rightAnswers >= scoreToPass ? MSG_TEST_PASSED : MSG_TEST_FAILED;
    }

    @Override
    public String toString() {
        return String.format(TEST_RESULT_PATTERN, user.getFirstName(), user.getLastName(), total,
                rightAnswers, scoreToPass, determineTestResult());
    }

}
