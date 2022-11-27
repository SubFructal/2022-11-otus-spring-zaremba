package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppRunnerImpl implements AppRunner {

    private final IOService ioService;
    private final QuestionService questionService;
    private final OutputPreparerService outputPreparer;

    @Override
    public void run() {
        questionService.getAllQuestions().forEach(question -> ioService.output(outputPreparer.prepareOutput(question)));
    }
}
