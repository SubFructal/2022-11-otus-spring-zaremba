package ru.otus.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.client.dto.CommentDto;
import ru.otus.client.dto.GenreDto;

import java.util.List;

@FeignClient(name = "libraryServiceCommentsFeignClient", url = "http://localhost:9001")
public interface LibraryServiceCommentsFeignClient {

    @GetMapping(value = "/api/books/{id}/comments")
    List<CommentDto> findAllCommentsForSpecificBook(@PathVariable(value = "id") long id);
}
