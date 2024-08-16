package com.beachUpSkilling.bookStore.service;

import com.beachUpSkilling.bookStore.dto.BookDto;
import com.beachUpSkilling.bookStore.model.Book;
import com.beachUpSkilling.bookStore.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;

    public BookService(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }


    public List<BookDto> getBooks() {
        Iterable<Book> allBooks = bookRepository.findAll();
        return StreamSupport.stream(allBooks.spliterator(), false)
                .map(convertBookModelToBookDto())
                .collect(Collectors.toList());
    }

    private Function<Book, BookDto> convertBookModelToBookDto() {
        return book -> modelMapper.map(book, BookDto.class);
    }
}
