package com.beachUpSkilling.bookStore.controller;

import com.beachUpSkilling.bookStore.dto.BookDto;
import com.beachUpSkilling.bookStore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.catalina.authenticator.SavedRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Book Api", description = "Operations related to books")
@RestController
@RequestMapping("/bookStore")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "get list of books", tags = {"Books"})
    @ApiResponses(value = {
            @ApiResponse(description = "successful response", responseCode = "200"),
            @ApiResponse(responseCode = "403", description = "Accessing to this resource is forbidden"),
            @ApiResponse(responseCode = "404", description = "resource not found")
    })
    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> books() {
        return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }

    @GetMapping("/books/{title}")
    public ResponseEntity<List<BookDto>> booksByTitleIgnoreCase(@PathVariable String title) {
        return new ResponseEntity<>(bookService.getBooksByTitleIgnoreCase(title), HttpStatus.OK);
    }
}
