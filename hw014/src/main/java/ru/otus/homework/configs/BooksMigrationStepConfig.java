package ru.otus.homework.configs;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.RowMapper;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@EnableBatchProcessing
@Configuration
public class BooksMigrationStepConfig {

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public Cache<String, Book> bookCache() {
        return Caffeine.newBuilder()
                .maximumSize(Integer.MAX_VALUE)
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public ItemReader<Book> booksReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Book>()
                .name("booksReader")
                .dataSource(dataSource)
                .sql("select books.title, authors.name, genres.genre " +
                        "from books left join authors on books.author_id = authors.id " +
                        "left join genres on books.genre_id = genres.id order by books.id")
                .rowMapper(new BookRowMapper())
                .build();
    }

    @Bean
    public ItemProcessor<Book, Book> transformBooksProcessor(Cache<String, Author> authorCache,
                                                             Cache<String, Genre> genreCache) {
        return new ItemProcessor<Book, Book>() {
            @Override
            public Book process(Book book) throws Exception {
                book.setGenre(genreCache.getIfPresent(book.getGenre().getGenreName()));
                book.setAuthor(authorCache.getIfPresent(book.getAuthor().getName()));
                return book;
            }
        };
    }

    @Bean
    public ItemWriter<Book> booksWriter() {
        return new MongoItemWriterBuilder<Book>()
                .template(mongoTemplate)
                .collection("books")
                .build();
    }

    @Bean
    public Step importBooksStep(StepBuilderFactory stepBuilderFactory, ItemReader<Book> itemReader,
                                ItemProcessor<Book, Book> itemProcessor, ItemWriter<Book> itemWriter,
                                Cache<String, Book> bookCache) {
        return stepBuilderFactory.get("booksMigrationStep")
                .<Book, Book>chunk(CHUNK_SIZE)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        mongoTemplate.remove(new Query(), Book.class);
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        mongoTemplate.findAll(Book.class, "books")
                                .forEach(book -> bookCache.put(book.getTitle() + "_" + book.getAuthor().getName()
                                        + "_" + book.getGenre().getGenreName(), book));
                        System.out.println(bookCache.asMap());
                        return null;
                    }
                })
                .build();
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var genre = new Genre(rs.getString("genre"));
            var author = new Author(rs.getString("name"));
            return new Book(rs.getString("title"), genre, author);
        }
    }
}
