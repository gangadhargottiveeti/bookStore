package com.beachUpSkilling.bookStore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookStore")
public class BookController {

    @GetMapping("/books")
    public String books() {
        return "Books from books API";
    }
}
