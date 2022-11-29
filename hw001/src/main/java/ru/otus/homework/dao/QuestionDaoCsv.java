package ru.otus.homework.dao;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class QuestionDaoCsv implements QuestionDao {

    private static final String IS_RIGHT = "ok";
    private final String csvResourceName;

    @Override
    public List<Question> findAll() {
        try (var reader = new CSVReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().
                getResourceAsStream(csvResourceName), "File not found")))) {
            var content = reader.readAll();
            var questions = new ArrayList<Question>();
            for (var row : content) {
                var answers = new ArrayList<Answer>();
                for (int i = 1; i < row.length; i = i + 2) {
                    answers.add(new Answer(row[i], row[i + 1].equalsIgnoreCase(IS_RIGHT)));
                }
                questions.add(new Question(row[0], answers));
            }
            return questions;
        } catch (Exception e) {
            throw new RuntimeException("Error file reading", e);
        }
    }
}
