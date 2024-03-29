package ru.otus.client.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.client.dto.CommentDto;
import ru.otus.client.feign.LibraryServiceFeignClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final LibraryServiceFeignClient feignClient;
    private final String diffPostfix;

    public CommentServiceImpl(LibraryServiceFeignClient feignClient, @Value("${diff-postfix}") String diffPostfix) {
        this.feignClient = feignClient;
        this.diffPostfix = diffPostfix;
    }

    @HystrixCommand(fallbackMethod = "getEmptyCommentsList")
    @Override
    public List<CommentDto> findAllCommentsForSpecificBook(long bookId) {
        return feignClient.findAllCommentsForSpecificBook(bookId).stream().peek(commentDto -> {
            var commentText = commentDto.getCommentText();
            commentDto.setCommentText(commentText + diffPostfix);
        }).collect(Collectors.toList());
    }

    private List<CommentDto> getEmptyCommentsList(long bookId) {
        return Collections.emptyList();
    }

}
