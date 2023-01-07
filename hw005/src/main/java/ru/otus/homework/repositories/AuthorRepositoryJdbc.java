package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.homework.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJdbc implements AuthorRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public long count() {
        var count = jdbc.getJdbcOperations().queryForObject("select count(*) from authors", Long.class);
        return Objects.isNull(count) ? 0 : count;
    }

    @Override
    public Author insert(Author author) {
        var params = new MapSqlParameterSource(Map.of("name", author.getName()));
        var keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into authors(name) values(:name)", params, keyHolder, new String[]{"id"});
        author.setId(keyHolder.getKey().longValue());
        return author;
    }

    @Override
    public Author update(Author author) {
        jdbc.update("update authors set name = :name where id = :id",
                Map.of("name", author.getName(), "id", author.getId()));
        return author;
    }

    @Override
    public Optional<Author> getById(long id) {
        return jdbc.query("select id, name from authors where id = :id",
                Map.of("id", id), new AuthorRowMapper()).stream().findFirst();
    }

    @Override
    public Optional<Author> getByName(String authorName) {
        return jdbc.query("select id, name from authors where name = :authorName",
                Map.of("authorName", authorName), new AuthorRowMapper()).stream().findFirst();
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select id, name from authors order by id", new AuthorRowMapper());
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from authors where id = :id", Map.of("id", id));
    }

    @Override
    public long deleteAll() {
        return jdbc.getJdbcOperations().update("delete from authors");
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Author(rs.getLong("id"), rs.getString("name"));
        }
    }

}
