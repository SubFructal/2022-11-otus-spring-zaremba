package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.homework.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJdbc implements GenreRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public long count() {
        var count = jdbc.getJdbcOperations().queryForObject("select count(*) from genres", Long.class);
        return Objects.isNull(count) ? 0 : count;
    }

    @Override
    public Genre insert(Genre genre) {
        var params = new MapSqlParameterSource(Map.of("genre", genre.getGenreName()));
        var keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into genres(genre) values(:genre)", params, keyHolder, new String[]{"id"});
        genre.setId(keyHolder.getKey().longValue());
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        jdbc.update("update genres set genre = :genre where id = :id",
                Map.of("genre", genre.getGenreName(), "id", genre.getId()));
        return genre;
    }

    @Override
    public Optional<Genre> getById(long id) {
        return Optional.ofNullable(jdbc.query("select id, genre from genres where id = :id",
                Map.of("id", id), rs -> rs.next() ? new GenreRowMapper().mapRow(rs, 1) : null));
    }

    @Override
    public Optional<Genre> getByName(String genreName) {
        return Optional.ofNullable(jdbc.query("select id, genre from genres where genre = :genreName",
                Map.of("genreName", genreName),
                rs -> rs.next() ? new GenreRowMapper().mapRow(rs, 1) : null));
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select id, genre from genres order by id", new GenreRowMapper());
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from genres where id = :id", Map.of("id", id));
    }

    @Override
    public long clean() {
        return jdbc.getJdbcOperations().update("delete from genres");
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Genre(rs.getLong("id"), rs.getString("genre"));
        }
    }
}
