package ru.otus.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.client.dto.AuthorDto;

import java.util.List;

@FeignClient(name = "libraryServiceAuthorFeignClient", url = "http://localhost:9001")
public interface LibraryServiceAuthorFeignClient {

    @GetMapping(value = "/api/authors")
    List<AuthorDto> findAllAuthors();
}
