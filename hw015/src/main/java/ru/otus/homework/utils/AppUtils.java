package ru.otus.homework.utils;

import ru.otus.homework.domain.Applicant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class AppUtils {

    private static final Random RANDOM = new Random();

    private AppUtils() {
    }

    public static Collection<Applicant> formApplicantsGroup() {
        var applicantsGroup = new ArrayList<Applicant>();
        for (int index = 0; index < 20; index++) {
            var applicant = new Applicant();
            applicant.setName("person" + (index+1));
            applicantsGroup.add(applicant);
        }
        return applicantsGroup;
    }

    public static int calculateEntryExamResult() {
        return RANDOM.nextInt(61);
    }

    public static boolean getGraduateWorkReadiness() {
        return RANDOM.nextBoolean();
    }

    public static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
