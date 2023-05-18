package ru.otus.homework.configs;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@EnableBatchProcessing
@Configuration
public class AuthorsMigrationStepConfig {

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public Cache<String, Author> authorCache() {
        return Caffeine.newBuilder()
                .maximumSize(Integer.MAX_VALUE)
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public ItemReader<Author> authorsReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Author>()
                .name("authorsReader")
                .dataSource(dataSource)
                .sql("select name from authors order by id")
                .rowMapper(new AuthorRowMapper())
                .build();
    }

    @Bean
    public ItemWriter<Author> authorsWriter() {
        return new MongoItemWriterBuilder<Author>()
                .template(mongoTemplate)
                .collection("authors")
                .build();
    }

    @Bean
    public Step importAuthorsStep(StepBuilderFactory stepBuilderFactory, ItemReader<Author> itemReader,
                                  ItemWriter<Author> itemWriter, Cache<String, Author> authorCache) {
        return stepBuilderFactory.get("authorsMigrationStep")
                .<Author, Author>chunk(CHUNK_SIZE)
                .reader(itemReader)
                .writer(itemWriter)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        mongoTemplate.remove(new Query(), Author.class);
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        mongoTemplate.findAll(Author.class, "authors")
                                .forEach(author -> authorCache.put(author.getName(), author));
                        System.out.println(authorCache.asMap());
                        return null;
                    }
                })
                .build();
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Author(rs.getString("name"));
        }
    }
}
