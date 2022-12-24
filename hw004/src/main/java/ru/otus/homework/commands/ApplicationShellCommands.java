package ru.otus.homework.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.services.AppRunner;
import ru.otus.homework.services.IOService;
import ru.otus.homework.services.LocalizationMessageService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationShellCommands {

    private static final String AGREEMENT = "y";

    private final LocalizationMessageService messageService;
    private final AppRunner appRunner;
    private final IOService ioService;

    private boolean isAgree = false;

    @ShellMethod(value = "User agree to testing (y/n)", key = {"agree", "agree-to-testing"})
    public void agreeToTesting(@ShellOption(defaultValue = "y") String userAnswer) {
        this.isAgree = userAnswer.equalsIgnoreCase(AGREEMENT);
        ioService.output(isAgree ? messageService.getLocalizedMessage("confirmation.of.agreement") :
                messageService.getLocalizedMessage("confirmation.of.disagreement"));
    }

    @ShellMethod(value = "Start testing system", key = {"start", "start-testing"})
    @ShellMethodAvailability(value = "isAgreementToTestingReceived")
    public void startTesting() {
        appRunner.run();
        ioService.output("\n" + messageService.getLocalizedMessage("testing.is.finished"));
    }

    private Availability isAgreementToTestingReceived() {
        return isAgree ? Availability.available() :
                Availability.unavailable(messageService.getLocalizedMessage("confirmation.consent"));
    }
}
