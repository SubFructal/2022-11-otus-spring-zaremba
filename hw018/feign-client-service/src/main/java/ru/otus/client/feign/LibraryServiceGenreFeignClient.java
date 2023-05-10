package ru.otus.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.client.dto.GenreDto;

import java.util.List;

@FeignClient(name = "libraryServiceGenreFeignClient", url = "http://localhost:9001")
public interface LibraryServiceGenreFeignClient {

    @GetMapping(value = "/api/genres")
    List<GenreDto> findAllGenres();
}
