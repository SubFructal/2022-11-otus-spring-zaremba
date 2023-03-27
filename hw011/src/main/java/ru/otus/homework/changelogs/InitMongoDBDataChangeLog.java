package ru.otus.homework.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;
import ru.otus.homework.models.Genre;
import ru.otus.homework.repositories.AuthorRepository;
import ru.otus.homework.repositories.BookRepository;
import ru.otus.homework.repositories.CommentRepository;
import ru.otus.homework.repositories.GenreRepository;

import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private final Author tolstoy = new Author("1", "Лев Толстой");
    private final Author king = new Author("2", "Стивен Кинг");
    private final Author lukyanenko = new Author("3", "Сергей Лукьяненко");
    private final Author shishkov = new Author("4", "Вячеслав Шишков");
    private final Author perumov = new Author("5", "Ник Перумов");
    private final Author panov = new Author("6", "Вадим Панов");
    private final Author belyanin = new Author("7", "Андрей Белянин");

    private final Genre russianClassics = new Genre("1", "Русская классика");
    private final Genre fantastic = new Genre("2", "Фантастика");
    private final Genre fantasy = new Genre("3", "Фэнтези");
    private final Genre horrors = new Genre("4", "Ужасы");
    private final Genre adventures = new Genre("5", "Приключения");

    private final Book firstBook = new Book("1", "Война и мир", russianClassics, tolstoy);
    private final Book secondBook = new Book("2", "Кладбище домашних животных", horrors, king);
    private final Book thirdBook = new Book("3", "Угрюм-река", russianClassics, shishkov);
    private final Book forthBook = new Book("4", "Лабиринт отражений", fantastic, lukyanenko);
    private final Book fifthBook = new Book("5", "Кристина", horrors, king);
    private final Book sixthBook = new Book("6", "Гибель богов", fantasy, perumov);

    private final Comment firstBookFirstComment = new Comment("Война и мир: комментарий 1", firstBook);
    private final Comment firstBookSecondComment = new Comment("Война и мир: комментарий 2", firstBook);
    private final Comment secondBookFirstComment = new Comment("Кладбище домашних животных: комментарий 1", secondBook);
    private final Comment secondBookSecondComment = new Comment("Кладбище домашних животных: комментарий 2", secondBook);
    private final Comment thirdBookFirstComment = new Comment("Угрюм-река: комментарий 1", thirdBook);
    private final Comment thirdBookSecondComment = new Comment("Угрюм-река: комментарий 2", thirdBook);
    private final Comment forthBookFirstComment = new Comment("Лабиринт отражений: комментарий 1", forthBook);
    private final Comment forthBookSecondComment = new Comment("Лабиринт отражений: комментарий 2", forthBook);
    private final Comment fifthBookFirstComment = new Comment("Кристина: комментарий 1", fifthBook);
    private final Comment fifthBookSecondComment = new Comment("Кристина: комментарий 2", fifthBook);
    private final Comment sixthBookFirstComment = new Comment("Гибель богов: комментарий 1", sixthBook);
    private final Comment sixthBookSecondComment = new Comment("Гибель богов: комментарий 2", sixthBook);

    @ChangeSet(order = "000", id = "dropDB", author = "SubFructal", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "SubFructal", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        repository.saveAll(List.of(tolstoy, king, lukyanenko, shishkov, perumov, panov, belyanin)).subscribe();
    }

    @ChangeSet(order = "002", id = "initGenres", author = "SubFructal", runAlways = true)
    public void initGenres(GenreRepository repository) {
        repository.saveAll(List.of(russianClassics, fantastic, fantasy, horrors, adventures)).subscribe();
    }

    @ChangeSet(order = "003", id = "initBooks", author = "SubFructal", runAlways = true)
    public void initBooks(BookRepository repository) {
        repository.saveAll(List.of(firstBook, secondBook, thirdBook, forthBook, fifthBook, sixthBook)).subscribe();
    }

    @ChangeSet(order = "004", id = "initComments", author = "SubFructal", runAlways = true)
    public void initComments(CommentRepository repository) {
        repository.saveAll(List.of(
                firstBookFirstComment, firstBookSecondComment,
                secondBookFirstComment, secondBookSecondComment,
                thirdBookFirstComment, thirdBookSecondComment,
                forthBookFirstComment, forthBookSecondComment,
                fifthBookFirstComment, fifthBookSecondComment,
                sixthBookFirstComment, sixthBookSecondComment)).subscribe();
    }
}
