package ru.otus.homework.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestResult {
    private final User user;

    @Getter
    private int total;

    @Getter
    private int rightAnswers;

    public String getUserFirstName() {
        return this.user.getFirstName();
    }

    public String getUserLastName() {
        return this.user.getLastName();
    }

    public void incrementAnswers(boolean mustIncremented) {
        total++;
        if (mustIncremented) {
            rightAnswers++;
        }
    }
}
