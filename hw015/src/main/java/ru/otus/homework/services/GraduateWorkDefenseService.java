package ru.otus.homework.services;

import ru.otus.homework.domain.Employee;
import ru.otus.homework.domain.Student;

import java.util.Collection;

public interface GraduateWorkDefenseService {
    Collection<Employee> defenseGraduateWork(Collection<Student> students);
}
