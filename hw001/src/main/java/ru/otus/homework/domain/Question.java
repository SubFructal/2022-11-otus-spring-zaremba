package ru.otus.homework.domain;

import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Question {
    private final String questionText;
    private final List<Answer> answers;
}
