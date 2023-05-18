package ru.otus.homework.configs;

import com.github.benmanes.caffeine.cache.Cache;
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
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Comment;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@EnableBatchProcessing
@Configuration
public class CommentsMigrationStepConfig {

    private static final int CHUNK_SIZE = 5;

    @Autowired
    MongoTemplate mongoTemplate;

    @Bean
    public ItemReader<Comment> commentsReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Comment>()
                .name("commentsReader")
                .dataSource(dataSource)
                .sql("select books.title, authors.name, genres.genre, comments.comment " +
                        "from books left join authors on books.author_id = authors.id " +
                        "left join genres on books.genre_id = genres.id " +
                        "left join comments on books.id = comments.book_id order by books.id")
                .rowMapper(new CommentRowMapper())
                .build();
    }

    @Bean
    public ItemProcessor<Comment, Comment> transformCommentsProcessor(Cache<String, Book> bookCache) {
        return new ItemProcessor<Comment, Comment>() {
            @Override
            public Comment process(Comment comment) throws Exception {
                comment.setBook(bookCache.getIfPresent(comment.getBook().getTitle()));
                return comment;
            }
        };
    }

    @Bean
    public ItemWriter<Comment> commentsWriter() {
        return new MongoItemWriterBuilder<Comment>()
                .template(mongoTemplate)
                .collection("comments")
                .build();
    }

    @Bean
    public Step importCommentsStep(StepBuilderFactory stepBuilderFactory, ItemReader<Comment> itemReader,
                                   ItemProcessor<Comment, Comment> itemProcessor, ItemWriter<Comment> itemWriter) {
        return stepBuilderFactory.get("commentsMigrationStep")
                .<Comment, Comment>chunk(CHUNK_SIZE)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        mongoTemplate.remove(new Query(), Comment.class);
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        return null;
                    }
                })
                .build();
    }

    private static class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            var book = new Book();
            book.setTitle(rs.getString("title") + "_" + rs.getString("name") +
                    "_" + rs.getString("genre"));
            return new Comment(rs.getString("comment"), book);
        }
    }
}
