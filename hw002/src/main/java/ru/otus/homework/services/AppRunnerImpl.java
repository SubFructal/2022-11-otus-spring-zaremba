package ru.otus.homework.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.TestResult;

@Service
public class AppRunnerImpl implements AppRunner {

    private static final String MSG_HEADER = "Welcome to the student's testing system";
    private static final String HEADER_DELIMITER = "----------------------------------------";
    private static final String MSG_RIGHT_ANSWER = "This is right answer\n";
    private static final String MSG_WRONG_ANSWER = "This is wrong answer\n";
    private static final String MSG_ENTER_ANSWER = "Enter number of answer";

    private final IOService ioService;
    private final QuestionService questionService;
    private final OutputPreparerService outputPreparer;
    private final UserService userService;
    private final int scoreToPass;

    public AppRunnerImpl(IOService ioService,
                         QuestionService questionService,
                         OutputPreparerService outputPreparer,
                         UserService userService,
                         @Value("${score.to.pass}") int scoreToPass) {
        this.ioService = ioService;
        this.questionService = questionService;
        this.outputPreparer = outputPreparer;
        this.userService = userService;
        this.scoreToPass = scoreToPass;
    }

    @Override
    public void run() {
        ioService.output(MSG_HEADER);
        ioService.output(HEADER_DELIMITER);
        var user = userService.getUser();
        var testResult = new TestResult(user, scoreToPass);

        var questions = questionService.getAllQuestions();
        questions.forEach(question -> {
            var answer = ioService.readInt(outputPreparer.prepareOutput(question) + MSG_ENTER_ANSWER);
            var isRight = question.getAnswers().get(answer).isRight();
            testResult.incrementAnswers(isRight);
            ioService.output(isRight ? MSG_RIGHT_ANSWER : MSG_WRONG_ANSWER);
        });
        ioService.output(testResult.toString());
    }
}
