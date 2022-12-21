package ru.otus.homework.services;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class IOServiceStreams implements IOService {

    private final PrintStream out;
    private final Scanner in;

    public IOServiceStreams(PrintStream out, InputStream in) {
        this.out = out;
        this.in = new Scanner(in);
    }

    @Override
    public void output(String message) {
        out.println(message);
    }

    @Override
    public String readLn(String prompt) {
        output(prompt);
        return in.nextLine();
    }

    @Override
    public int readInt(String prompt) {
        output(prompt);
        return in.nextInt();
    }
}
