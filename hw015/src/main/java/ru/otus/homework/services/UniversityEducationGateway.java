package ru.otus.homework.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.homework.domain.Applicant;
import ru.otus.homework.domain.Employee;

import java.util.Collection;

@MessagingGateway
public interface UniversityEducationGateway {
    @Gateway(requestChannel = "applicantChannel", replyChannel = "employeeChannel")
    Collection<Employee> startLearning(Collection<Applicant> applicants);
}
