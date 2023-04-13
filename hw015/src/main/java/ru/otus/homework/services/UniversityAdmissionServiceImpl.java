package ru.otus.homework.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Applicant;
import ru.otus.homework.domain.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UniversityAdmissionServiceImpl implements UniversityAdmissionService {

    private static final int MIN_REQUIRED_POINTS_NUMBER = 42;

    @Override
    public Collection<Student> accept(Collection<Applicant> applicants) {
        log.info("{} человек сдали вступительные экзамены и получили определенное количество проходных баллов", applicants.size());
        List<Student> students = new ArrayList<>();
        for (var applicant : applicants) {
            if (applicant.getEntryExamResultPoints() >= MIN_REQUIRED_POINTS_NUMBER) {
                var student = new Student();
                student.setName(applicant.getName());
                log.info("Абитуриент {} зачислен в университет", student.getName());
                students.add(student);
            } else {
                log.info("Абитуриент {} не зачислен в университет", applicant.getName());
            }
        }
        log.info("{} человек набрали достаточное количество баллов и были зачислены в университет", students.size());
        return students;
    }
}
