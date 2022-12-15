package ru.otus.homework.dao;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.homework.configs.AppProperties;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.exceptions.QuestionsNotLoadedFromCsvSourceException;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class QuestionDaoCsv implements QuestionDao {

    private static final String IS_RIGHT = "true";
    private final AppProperties appProperties;
    private final MessageSource messageSource;

    @Override
    public List<Question> findAll() {
        var csvResourceNameLocalized = getCsvResourceNameLocalized(messageSource, appProperties);

        try (var reader = new CSVReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().
                getResourceAsStream(csvResourceNameLocalized), "File not found")))) {
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
            throw new QuestionsNotLoadedFromCsvSourceException(e);
        }
    }

    private String getCsvResourceNameLocalized(MessageSource messageSource,
                                               AppProperties appProperties) {
        return messageSource.getMessage("localization.name",
                new String[]{appProperties.getCsvResourceName()}, appProperties.getLocale());
    }
}
