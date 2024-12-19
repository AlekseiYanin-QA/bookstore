package com.greatbit.controllers;

import com.greatbit.models.BooksStorage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static java.lang.String.format;

@RestController
public class TestController {

    @RequestMapping ("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping ("/books")
    public String books() {
        return BooksStorage.getBooks().
                stream().
                map(book -> format("%s - %s - %s", book.getName(), book.getAuthor(), book.getPages())).
                collect(Collectors.joining("</br>"));
    }


}
