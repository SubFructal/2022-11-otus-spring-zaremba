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
        tolstoy = repository.save(new Author("Лев Толстой"));
        king = repository.save(new Author("Стивен Кинг"));
        lukyanenko = repository.save(new Author("Сергей Лукьяненко"));
        shishkov = repository.save(new Author("Вячеслав Шишков"));
        perumov = repository.save(new Author("Ник Перумов"));
        panov = repository.save(new Author("Вадим Панов"));
        belyanin = repository.save(new Author("Андрей Белянин"));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "SubFructal", runAlways = true)
    public void initGenres(GenreRepository repository){
        russianClassics = repository.save(new Genre("Русская классика"));
        fantastic = repository.save(new Genre("Фантастика"));
        fantasy = repository.save(new Genre("Фэнтези"));
        horrors = repository.save(new Genre("Ужасы"));
        adventures = repository.save(new Genre("Приключения"));
    }

    @ChangeSet(order = "003", id = "initBook", author = "SubFructal", runAlways = true)
    public void initTeachers(BookRepository repository){
        firstBook = repository.save(new Book("Война и мир", russianClassics, tolstoy));
        secondBook = repository.save(new Book("Кладбище домашних животных", horrors, king));
        thirdBook = repository.save(new Book("Угрюм-река", russianClassics, shishkov));
        forthBook = repository.save(new Book("Лабиринт отражений", fantastic, lukyanenko));
        fifthBook = repository.save(new Book("Кристина", horrors, king));
        sixthBook = repository.save(new Book("Гибель богов", fantasy, perumov));
    }

    @ChangeSet(order = "004", id = "initComment", author = "SubFructal", runAlways = true)
    public void initTeachers(CommentRepository repository){
        repository.save(new Comment("Война и мир: комментарий 1", firstBook));
        repository.save(new Comment("Война и мир: комментарий 2", firstBook));
        repository.save(new Comment("Кладбище домашних животных: комментарий 1", secondBook));
        repository.save(new Comment("Кладбище домашних животных: комментарий 2", secondBook));
        repository.save(new Comment("Угрюм-река: комментарий 1", thirdBook));
        repository.save(new Comment("Угрюм-река: комментарий 2", thirdBook));
        repository.save(new Comment("Лабиринт отражений: комментарий 1", forthBook));
        repository.save(new Comment("Лабиринт отражений: комментарий 2", forthBook));
        repository.save(new Comment("Кристина: комментарий 1", fifthBook));
        repository.save(new Comment("Кристина: комментарий 2", fifthBook));
        repository.save(new Comment("Гибель богов: комментарий 1", sixthBook));
        repository.save(new Comment("Гибель богов: комментарий 2", sixthBook));

    }
}
