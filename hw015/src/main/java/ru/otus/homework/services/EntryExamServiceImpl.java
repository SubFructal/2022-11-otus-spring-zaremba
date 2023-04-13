package ru.otus.homework.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Applicant;

import static ru.otus.homework.utils.AppUtils.calculateEntryExamResult;
import static ru.otus.homework.utils.AppUtils.delay;

@Service
@Slf4j
public class EntryExamServiceImpl implements EntryExamService {
    @Override
    public Applicant exam(Applicant applicant) {
        log.info("Начало вступительного экзамена абитуриентом {}", applicant.getName());
        delay();
        var entryExamResult = calculateEntryExamResult();
        log.info("Абитуриент {} сдал вступительный экзамен", applicant.getName());
        return new Applicant(applicant.getName(), entryExamResult);
    }
}
