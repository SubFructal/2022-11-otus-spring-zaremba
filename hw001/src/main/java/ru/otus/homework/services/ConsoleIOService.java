package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;

import java.io.PrintStream;

@RequiredArgsConstructor
public class ConsoleIOService implements IOService {

    private final PrintStream out;

    @Override
    public void output(String message) {
        out.println(message);
    }
}
