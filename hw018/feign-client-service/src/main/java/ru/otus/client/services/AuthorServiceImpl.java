package ru.otus.client.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.client.dto.AuthorDto;
import ru.otus.client.feign.LibraryServiceFeignClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final LibraryServiceFeignClient feignClient;
    private final String diffPostfix;

    public AuthorServiceImpl(LibraryServiceFeignClient feignClient,  @Value("${diff-postfix}") String diffPostfix) {
        this.feignClient = feignClient;
        this.diffPostfix = diffPostfix;
    }

    @HystrixCommand(fallbackMethod="getEmptyAuthorsList")
    @Override
    public List<AuthorDto> getAllAuthors() {
        return feignClient.findAllAuthors().stream().peek(authorDto -> {
            var name = authorDto.getName();
            authorDto.setName(name + diffPostfix);
        }).collect(Collectors.toList());
    }

    private List<AuthorDto> getEmptyAuthorsList() {
        return Collections.emptyList();
    }
}
