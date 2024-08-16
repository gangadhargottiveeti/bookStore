package com.beachUpSkilling.bookStore.controller;

import com.beachUpSkilling.bookStore.dto.BookDto;
import com.beachUpSkilling.bookStore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookStore")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> books() {
        return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }
}
