package ru.otus.homework.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Answer {
    private final String answerText;
    private final boolean right;
}
