package ru.otus.homework.changelogs_test;

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
public class InitTestMongoDBDataChangeLog {

    private Author firstAuthor;
    private Author secondAuthor;
    private Author thirdAuthor;

    private Genre firstGenre;
    private Genre secondGenre;
    private Genre thirdGenre;

    private Book firstBook;
    private Book secondBook;
    private Book thirdBook;
    private Book forthBook;

    @ChangeSet(order = "000", id = "dropDB", author = "SubFructal", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "SubFructal", runAlways = true)
    public void initAuthors(AuthorRepository repository){
        firstAuthor = repository.save(new Author("1", "Автор_01"));
        secondAuthor = repository.save(new Author("2", "Автор_02"));
        thirdAuthor = repository.save(new Author("3", "Автор_03"));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "SubFructal", runAlways = true)
    public void initGenres(GenreRepository repository){
        firstGenre = repository.save(new Genre("1", "Жанр_01"));
        secondGenre = repository.save(new Genre("2", "Жанр_02"));
        thirdGenre = repository.save(new Genre("3", "Жанр_03"));
    }

    @ChangeSet(order = "003", id = "initBook", author = "SubFructal", runAlways = true)
    public void initBooks(BookRepository repository){
        firstBook = repository.save(new Book("1", "Книга_01", firstGenre, firstAuthor));
        secondBook = repository.save(new Book("2", "Книга_02", secondGenre, secondAuthor));
        thirdBook = repository.save(new Book("3", "Книга_03", thirdGenre, thirdAuthor));
        forthBook = repository.save(new Book("4", "Книга_04", secondGenre, thirdAuthor));
    }

    @ChangeSet(order = "004", id = "initComment", author = "SubFructal", runAlways = true)
    public void initComments(CommentRepository repository){
        repository.save(new Comment("1", "Комментарий 1 к Книге_01", firstBook));
        repository.save(new Comment("2", "Комментарий 1 к Книге_02", secondBook));
        repository.save(new Comment("3", "Комментарий 1 к Книге_03", thirdBook));
        repository.save(new Comment("4", "Комментарий 2 к Книге_03", thirdBook));
        repository.save(new Comment("5", "Комментарий 1 к Книге_04", forthBook));
    }
}
