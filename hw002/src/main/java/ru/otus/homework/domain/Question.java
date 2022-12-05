package ru.otus.homework.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Question {
    private final String questionText;
    private final List<Answer> answers;
}
