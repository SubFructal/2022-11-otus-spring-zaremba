package ru.otus.client.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.client.dto.BookDto;
import ru.otus.client.feign.LibraryServiceBookFeignClient;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final LibraryServiceBookFeignClient feignClient;
    private final String diffPostfix;

    public BookServiceImpl(LibraryServiceBookFeignClient feignClient, @Value("${diff-postfix}") String diffPostfix) {
        this.feignClient = feignClient;
        this.diffPostfix = diffPostfix;
    }

    @HystrixCommand(fallbackMethod="getEmptyBooksList")
    @Override
    public List<BookDto> getAllBooks() {
        return feignClient.findAllBooks().stream().peek(bookDto -> {
            var title = bookDto.getTitle();
            bookDto.setTitle(title + diffPostfix);
        }).collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod="getEmptyBooksList")
    @Override
    public List<BookDto> findAllBooksByAuthor(String authorName) {
        return feignClient.findAllBooksByAuthor(authorName).stream().peek(bookDto -> {
            var title = bookDto.getTitle();
            bookDto.setTitle(title + diffPostfix);
        }).collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod="getEmptyBooksList")
    @Override
    public List<BookDto> findAllBooksByGenre(String genreName) {
        return feignClient.findAllBooksByGenre(genreName).stream().peek(bookDto -> {
            var title = bookDto.getTitle();
            bookDto.setTitle(title + diffPostfix);
        }).collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod="getDefaultBook")
    @Override
    public BookDto findBookById(long id) {

        sleepRandomly();

        var bookDto = feignClient.findBookById(id);
        var title = bookDto.getTitle();
        bookDto.setTitle(title + diffPostfix);
        return bookDto;

    }

    @HystrixCommand(fallbackMethod="getDefaultBook")
    @Override
    public BookDto deleteBookById(long id) {
        var bookDto = feignClient.deleteBook(id);
        var title = bookDto.getTitle();
        bookDto.setTitle(title + diffPostfix);
        return bookDto;
    }

    @HystrixCommand(fallbackMethod = "getDefaultDeletedBooksCount")
    @Override
    public String deleteAllBooks() {
        return "Удалено " + feignClient.deleteAllBooks() + " книг";
    }

    @HystrixCommand(fallbackMethod = "getDefaultBook")
    @Override
    public BookDto addBook(BookDto bookDto) {
        var responseBookDto = feignClient.createNewBook(bookDto);
        var title = responseBookDto.getTitle();
        responseBookDto.setTitle(title + diffPostfix);
        return responseBookDto;
    }

    @HystrixCommand(fallbackMethod = "getDefaultBook")
    @Override
    public BookDto changeBook(long id, BookDto bookDto) {
        var responseBookDto = feignClient.editBook(id, bookDto);
        var title = responseBookDto.getTitle();
        responseBookDto.setTitle(title + diffPostfix);
        return responseBookDto;
    }

    private List<BookDto> getEmptyBooksList() {
        return Collections.emptyList();
    }

    private List<BookDto> getEmptyBooksList(String param) {
        return Collections.emptyList();
    }

    private BookDto getDefaultBook(long id) {
        return new BookDto(0, "N/A", "N/A", "N/A");
    }

    private BookDto getDefaultBook(BookDto bookDto) {
        return new BookDto(0, "N/A", "N/A", "N/A");
    }

    private BookDto getDefaultBook(long id, BookDto bookDto) {
        return new BookDto(0, "N/A", "N/A", "N/A");
    }

    private String getDefaultDeletedBooksCount() {
        return "Удалено 0 книг";
    }

    private void sleepRandomly() {
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        if (randomNum == 3) {
            System.out.println("It is a chance for demonstrating Hystrix action");
            try {
                System.out.println("Start sleeping...." + System.currentTimeMillis());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Hystrix thread interrupted...." + System.currentTimeMillis());
                throw new RuntimeException(e);
            }
        }
    }
}
