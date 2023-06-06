package ru.otus.homework.configs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class MigrationJobConfig {

    public static final String JOB_NAME = "importLibraryJob";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job importLibrary(Step importAuthorsStep, Step importGenresStep,
                             Step importBooksStep, Step importCommentsStep) {
        return jobBuilderFactory.get(JOB_NAME)
                .start(importAuthorsStep)
                .next(importGenresStep)
                .next(importBooksStep)
                .next(importCommentsStep)
                .build();
    }
}
