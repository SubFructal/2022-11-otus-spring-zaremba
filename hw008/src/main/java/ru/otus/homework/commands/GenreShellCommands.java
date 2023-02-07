package ru.otus.homework.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.services.GenreService;
import ru.otus.homework.services.converters.GenreToStringConverter;

import java.util.stream.Collectors;

import static java.lang.String.format;

@ShellComponent
@RequiredArgsConstructor
public class GenreShellCommands {

    private final GenreService genreService;
    private final GenreToStringConverter converter;

    @ShellMethod(key = {"get-genres-count", "genres-count"},
            value = "Возвращает количество всех жанров в БД")
    public String getAllGenresCount() {
        var count = genreService.getGenresCount();
        return format("Общее количество жанров в БД: %d", count);
    }

    @ShellMethod(key = {"add-new-genre", "new-genre"},
            value = "Добавляет новый жанр в БД: укажите название жанра")
    public String addNewGenre(String genreName) {
        var genre = genreService.addGenre(genreName);
        return format("Добавлен новый жанр: %s", converter.convertToString(genre));
    }

    @ShellMethod(key = {"change-genre"},
            value = "Изменяет существующий в БД жанр: укажите идентификатор жанра, название жанра")
    public String changeGenre(String id, String genreName) {
        var genre = genreService.changeGenre(id, genreName);
        return format("Жанр изменен: %s", converter.convertToString(genre));
    }

    @ShellMethod(key = {"find-genre-by-id", "genre-by-id"},
            value = "Ищет жанр в БД по его идентификатору: укажите идентификатор жанра")
    public String findGenreById(String id) {
        var genre = genreService.findGenreById(id);
        return format("Жанр найден: %s", converter.convertToString(genre));
    }

    @ShellMethod(key = {"find-genre-by-name", "genre-by-name"},
            value = "Ищет жанр в БД по его названию: укажите название жанра")
    public String findGenreByName(String genreName) {
        var genre = genreService.findGenreByName(genreName);
        return format("Жанр найден: %s", converter.convertToString(genre));
    }

    @ShellMethod(key = {"show-all-genres", "all-genres"},
            value = "Выводит список всех жанров в БД")
    public String showAllGenres() {
        var genres = genreService.getAllGenres();
        return genres.stream().map(converter::convertToString).collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = {"delete-genre-from-db", "delete-genre"},
            value = "Удаляет жанр из БД по его идентификатору: укажите идентификатор жанра")
    public String deleteGenreFromDatabase(String id) {
        var genre = genreService.deleteGenreById(id);
        return format("Жанр удален: %s", converter.convertToString(genre));
    }

    @ShellMethod(key = {"delete-all-genres-from-db", "delete-all-genres"},
            value = "Удаляет все жанры из БД")
    public String deleteAllGenres() {
        var count = genreService.deleteAllGenres();
        return format("%d жанров удалено", count);
    }
}
