package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Applicant;
import ru.otus.homework.domain.Employee;

import java.util.Collection;

import static ru.otus.homework.utils.AppUtils.delay;
import static ru.otus.homework.utils.AppUtils.formApplicantsGroup;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppRunnerImpl implements AppRunner {

    private final UniversityEducationGateway universityEducationGateway;

    @Override
    public void run() {
        while (true) {
            Collection<Applicant> applicantsGroup = formApplicantsGroup();
            log.info("{} человек прибыло на вступительный экзамен", applicantsGroup.size());
            Collection<Employee> employees = universityEducationGateway.startLearning(applicantsGroup);
            log.info("{} человек закончили обучение и получили дипломы", employees.size());
            delay();
        }
    }
}
