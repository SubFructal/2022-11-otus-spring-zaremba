package ru.otus.homework.services;

import ru.otus.homework.domain.Applicant;
import ru.otus.homework.domain.Student;

import java.util.Collection;

public interface UniversityAdmissionService {
    Collection<Student> accept(Collection<Applicant> applicants);
}
