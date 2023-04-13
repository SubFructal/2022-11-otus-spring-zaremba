package ru.otus.homework.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Employee;
import ru.otus.homework.domain.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class GraduateWorkDefenseServiceImpl implements GraduateWorkDefenseService {
    @Override
    public Collection<Employee> defenseGraduateWork(Collection<Student> students) {
        log.info("{} человек вышли на защиту дипломной работы", students.size());
        List<Employee> employees = new ArrayList<>();
        for (var student : students) {
            if (student.isGraduateWorkReadiness()) {
                log.info("Студент {} успешно сдал дипломную работу", student.getName());
                employees.add(new Employee(student.getName()));
            } else {
                log.info("Студент {} не сдал дипломную работу", student.getName());
            }
        }
        return employees;
    }
}
