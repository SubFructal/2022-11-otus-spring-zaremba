package ru.otus.homework.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс ConsoleIOService")
class ConsoleIOServiceTest {

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String TEXT_FOR_PRINT = "Test text for print";

    @DisplayName("должен корректно выводить данные в консоль")
    @Test
    void shouldPrintTestTextCorrectly() {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var ioService = new ConsoleIOService(new PrintStream(byteArrayOutputStream));
        ioService.output(TEXT_FOR_PRINT);

        assertThat(byteArrayOutputStream.toString()).isEqualTo(TEXT_FOR_PRINT + LINE_SEPARATOR);
    }
}