package ru.otus.client.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.client.dto.GenreDto;
import ru.otus.client.feign.LibraryServiceGenreFeignClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final LibraryServiceGenreFeignClient feignClient;
    private final String diffPostfix;

    public GenreServiceImpl(LibraryServiceGenreFeignClient feignClient, @Value("${diff-postfix}") String diffPostfix) {
        this.feignClient = feignClient;
        this.diffPostfix = diffPostfix;
    }

    @HystrixCommand(fallbackMethod = "getEmptyGenresList")
    @Override
    public List<GenreDto> getAllGenres() {
        return feignClient.findAllGenres().stream().peek(genreDto -> {
            var name = genreDto.getName();
            genreDto.setName(name + diffPostfix);
        }).collect(Collectors.toList());
    }

    private List<GenreDto> getEmptyGenresList() {
        return Collections.emptyList();
    }
}
