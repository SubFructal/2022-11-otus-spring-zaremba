package ru.otus.homework.domain;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Answer {
    private final String answerText;
    private final boolean isRight;
}

