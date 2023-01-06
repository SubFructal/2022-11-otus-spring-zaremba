package ru.otus.homework.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.homework.models.Author;
import ru.otus.homework.models.Book;
import ru.otus.homework.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJdbc implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public long count() {
        var count = jdbc.getJdbcOperations().queryForObject("select count(*) from books", Long.class);
        return Objects.isNull(count) ? 0 : count;
    }

    @Override
    public Book insert(Book book) {
        var params = new MapSqlParameterSource(Map.of(
                "title", book.getTitle(),
                "genre_id", book.getGenreId(),
                "author_id", book.getAuthorId()
        ));
        var keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into books(title, genre_id, author_id) values(:title, :genre_id, :author_id)",
                params, keyHolder, new String[]{"id"});
        book.setId(keyHolder.getKey().longValue());
        return book;
    }

    @Override
    public Book update(Book book) {
        var params = Map.of(
                "id", book.getId(),
                "title", book.getTitle(),
                "genre_id", book.getGenreId(),
                "author_id", book.getAuthorId()
        );
        jdbc.update("update books set title = :title,  genre_id = :genre_id, author_id = :author_id " +
                "where id = :id", params);
        return book;
    }

    @Override
    public Optional<Book> getById(long id) {
        var sql = "select books.id, books.title, books.author_id, authors.name, books.genre_id, genres.genre " +
                "from books left join authors on books.author_id = authors.id " +
                "left join genres on books.genre_id = genres.id where books.id = :id";
        return Optional.ofNullable(jdbc.query(sql, Map.of("id", id),
                rs -> rs.next() ? new BookRowMapper().mapRow(rs, 1) : null));
    }

    @Override
    public List<Book> getAll() {
        var sql = "select books.id, books.title, books.author_id, authors.name, books.genre_id, genres.genre " +
                "from books left join authors on books.author_id = authors.id " +
                "left join genres on books.genre_id = genres.id order by books.id";
        return jdbc.query(sql, new BookRowMapper());
    }

    @Override
    public List<Book> getAllByGenre(String genreName) {
        var sql = "select books.id, books.title, books.author_id, authors.name, books.genre_id, genres.genre " +
                "from books left join authors on books.author_id = authors.id " +
                "left join genres on books.genre_id = genres.id where genres.genre = :genreName order by books.id";
        return jdbc.query(sql, Map.of("genreName", genreName), new BookRowMapper());
    }

    @Override
    public List<Book> getAllByAuthor(String authorName) {
        var sql = "select books.id, books.title, books.author_id, authors.name, books.genre_id, genres.genre " +
                "from books left join authors on books.author_id = authors.id " +
                "left join genres on books.genre_id = genres.id where authors.name = :authorName order by books.id";
        return jdbc.query(sql, Map.of("authorName", authorName), new BookRowMapper());
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where id = :id", Map.of("id", id));
    }

    @Override
    public long clean() {
        return jdbc.getJdbcOperations().update("delete from books");
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var genre = new Genre(rs.getLong("genre_id"), rs.getString("genre"));
            var author = new Author(rs.getLong("author_id"), rs.getString("name"));
            return new Book(rs.getLong("id"), rs.getString("title"), genre, author);
        }
    }
}
