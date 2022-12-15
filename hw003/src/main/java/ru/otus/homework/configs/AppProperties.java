package ru.otus.homework.configs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;


@ConfigurationProperties(prefix = "app.properties")
@RequiredArgsConstructor
@Getter
public class AppProperties implements ApplicationLocaleProvider, ApplicationPropertiesProvider {

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
}
