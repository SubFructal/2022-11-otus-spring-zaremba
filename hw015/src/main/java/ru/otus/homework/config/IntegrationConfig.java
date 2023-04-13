package ru.otus.homework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.homework.domain.Applicant;
import ru.otus.homework.domain.Student;
import ru.otus.homework.services.EntryExamService;
import ru.otus.homework.services.GraduateWorkDefenseService;
import ru.otus.homework.services.GraduateWorkPrepareService;
import ru.otus.homework.services.UniversityAdmissionService;

@Configuration
public class IntegrationConfig {

    @Autowired
    private EntryExamService entryExamService;
    @Autowired
    private UniversityAdmissionService universityAdmissionService;
    @Autowired
    private GraduateWorkPrepareService graduateWorkPrepareService;
    @Autowired
    private GraduateWorkDefenseService graduateWorkDefenseService;

    @Bean
    public QueueChannel applicantChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public PublishSubscribeChannel employeeChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(1000).get();
    }

    @Bean
    public IntegrationFlow applicantFlow() {
        return IntegrationFlows.from(applicantChannel())
                .split()
                .<Applicant, Applicant>transform(applicant -> entryExamService.exam(applicant))
                .aggregate()
                .handle(universityAdmissionService, "accept")
                .channel("applicantFlow.output")
                .get();
    }

    @Bean
    public IntegrationFlow studentFlow() {
        return IntegrationFlows.from("applicantFlow.output")
                .split()
                .<Student, Student>transform(student -> graduateWorkPrepareService.prepareGraduateWork(student))
                .aggregate()
                .handle(graduateWorkDefenseService, "defenseGraduateWork")
                .channel(employeeChannel())
                .get();
    }
}
