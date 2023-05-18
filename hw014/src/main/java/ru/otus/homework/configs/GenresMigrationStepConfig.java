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
import ru.otus.homework.models.Genre;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@EnableBatchProcessing
@Configuration
public class GenresMigrationStepConfig {

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public Cache<String, Genre> genreCache() {
        return Caffeine.newBuilder()
                .maximumSize(Integer.MAX_VALUE)
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public ItemReader<Genre> genresReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Genre>()
                .name("genresReader")
                .dataSource(dataSource)
                .sql("select genre from genres order by id")
                .rowMapper(new GenreRowMapper())
                .build();
    }

    @Bean
    public ItemWriter<Genre> genresWriter() {
        return new MongoItemWriterBuilder<Genre>()
                .template(mongoTemplate)
                .collection("genres")
                .build();
    }

    @Bean
    public Step importGenresStep(StepBuilderFactory stepBuilderFactory, ItemReader<Genre> itemReader,
                                 ItemWriter<Genre> itemWriter, Cache<String, Genre> genreCache) {
        return stepBuilderFactory.get("genresMigrationStep")
                .<Genre, Genre>chunk(CHUNK_SIZE)
                .reader(itemReader)
                .writer(itemWriter)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        mongoTemplate.remove(new Query(), Genre.class);
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        mongoTemplate.findAll(Genre.class, "genres")
                                .forEach(genre -> genreCache.put(genre.getGenreName(), genre));
                        System.out.println(genreCache.asMap());
                        return null;
                    }
                })
                .build();
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Genre(rs.getString("genre"));
        }
    }
}
