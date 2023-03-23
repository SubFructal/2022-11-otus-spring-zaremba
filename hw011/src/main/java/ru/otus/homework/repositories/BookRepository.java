package ru.otus.homework.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Flux<Book> findAllByGenre(Genre genre, Sort sort);

    Flux<Book> findAllByAuthor(Author author, Sort sort);
}
