package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.homework.configs.ApplicationLocaleProvider;

@Service
@RequiredArgsConstructor
public class LocalizationMessageServiceImpl implements LocalizationMessageService {

    private final ApplicationLocaleProvider localeProvider;
    private final MessageSource messageSource;

    @Override
    public String getLocalizedMessage(String code) {
        return messageSource.getMessage(code, new String[0], localeProvider.getLocale());
    }
}
