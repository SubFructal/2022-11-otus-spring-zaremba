package ru.otus.homework.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Locale;


@ConfigurationProperties(prefix = "app.properties")
@ConstructorBinding
@RequiredArgsConstructor
public class AppProperties implements LocaleProvider, ScoreToPassProvider, LocalizedCsvResourceNameProvider {

    private final String csvResourceName;
    private final int scoreToPass;
    private final Locale locale;

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public int getScoreToPass() {
        return scoreToPass;
    }

    @Override
    public String getLocalizedCsvResourceName() {
        return locale.toString() + "_" + csvResourceName;
    }
}
