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

    private Author tolstoy;
    private Author king;
    private Author lukyanenko;
    private Author shishkov;
    private Author perumov;
    private Author panov;
    private Author belyanin;

    private Genre russianClassics;
    private Genre fantastic;
    private Genre fantasy;
    private Genre horrors;
    private Genre adventures;

    private Book firstBook;
    private Book secondBook;
    private Book thirdBook;
    private Book forthBook;
    private Book fifthBook;
    private Book sixthBook;

    @ChangeSet(order = "000", id = "dropDB", author = "SubFructal", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "SubFructal", runAlways = true)
    public void initAuthors(AuthorRepository repository){
        tolstoy = new Author("1", "Лев Толстой");
        king = new Author("2", "Стивен Кинг");
        lukyanenko = new Author("3", "Сергей Лукьяненко");
        shishkov = new Author("4", "Вячеслав Шишков");
        perumov = new Author("5", "Ник Перумов");
        panov = new Author("6", "Вадим Панов");
        belyanin = new Author("7", "Андрей Белянин");

        repository.saveAll(List.of(tolstoy, king, lukyanenko, shishkov, perumov, panov, belyanin)).subscribe();
    }

    @ChangeSet(order = "002", id = "initGenres", author = "SubFructal", runAlways = true)
    public void initGenres(GenreRepository repository){
        russianClassics = new Genre("1", "Русская классика");
        fantastic = new Genre("2", "Фантастика");
        fantasy = new Genre("3", "Фэнтези");
        horrors = new Genre("4", "Ужасы");
        adventures = new Genre("5", "Приключения");

        repository.saveAll(List.of(russianClassics, fantastic, fantasy, horrors, adventures)).subscribe();
    }

    @ChangeSet(order = "003", id = "initBooks", author = "SubFructal", runAlways = true)
    public void initBooks(BookRepository repository){
        firstBook = new Book("1", "Война и мир", russianClassics, tolstoy);
        secondBook = new Book("2", "Кладбище домашних животных", horrors, king);
        thirdBook = new Book("3", "Угрюм-река", russianClassics, shishkov);
        forthBook = new Book("4", "Лабиринт отражений", fantastic, lukyanenko);
        fifthBook = new Book("5", "Кристина", horrors, king);
        sixthBook = new Book("6", "Гибель богов", fantasy, perumov);

        repository.saveAll(List.of(firstBook, secondBook, thirdBook, forthBook, fifthBook, sixthBook)).subscribe();
    }

    @ChangeSet(order = "004", id = "initComments", author = "SubFructal", runAlways = true)
    public void initComments(CommentRepository repository){
        repository.save(new Comment("Война и мир: комментарий 1", firstBook)).subscribe();
        repository.save(new Comment("Война и мир: комментарий 2", firstBook)).subscribe();
        repository.save(new Comment("Кладбище домашних животных: комментарий 1", secondBook)).subscribe();
        repository.save(new Comment("Кладбище домашних животных: комментарий 2", secondBook)).subscribe();
        repository.save(new Comment("Угрюм-река: комментарий 1", thirdBook)).subscribe();
        repository.save(new Comment("Угрюм-река: комментарий 2", thirdBook)).subscribe();
        repository.save(new Comment("Лабиринт отражений: комментарий 1", forthBook)).subscribe();
        repository.save(new Comment("Лабиринт отражений: комментарий 2", forthBook)).subscribe();
        repository.save(new Comment("Кристина: комментарий 1", fifthBook)).subscribe();
        repository.save(new Comment("Кристина: комментарий 2", fifthBook)).subscribe();
        repository.save(new Comment("Гибель богов: комментарий 1", sixthBook)).subscribe();
        repository.save(new Comment("Гибель богов: комментарий 2", sixthBook)).subscribe();

    }
}
