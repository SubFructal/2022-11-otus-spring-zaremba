package ru.otus.homework.services;

import ru.otus.homework.domain.TestResult;

public interface TestResultConverter {
    String convertTestResultToString(TestResult testResult, int scoreToPass);
}
