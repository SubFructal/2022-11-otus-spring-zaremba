package ru.otus.homework.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Student;

import static ru.otus.homework.utils.AppUtils.delay;
import static ru.otus.homework.utils.AppUtils.getGraduateWorkReadiness;

@Service
@Slf4j
public class GraduateWorkPrepareServiceImpl implements GraduateWorkPrepareService {
    @Override
    public Student prepareGraduateWork(Student student) {
        log.info("Начало выполнения дипломной работы студентом {}", student.getName());
        delay();
        var graduateWorkReadiness = getGraduateWorkReadiness();
        log.info("Окончание выполнения дипломной работы студентом {}", student.getName());
        return new Student(student.getName(), graduateWorkReadiness);
    }
}
