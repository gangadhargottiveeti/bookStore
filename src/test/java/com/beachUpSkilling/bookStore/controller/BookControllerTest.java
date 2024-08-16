package com.beachUpSkilling.bookStore.controller;

import com.beachUpSkilling.bookStore.dto.BookDto;
import com.beachUpSkilling.bookStore.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @Test
    void shouldReturnListOfBookDtoWhenBooksMethodIsCalled() {
        BookDto bookDto = getBookDto();
        List<BookDto> allBooks = new ArrayList<>();
        allBooks.add(bookDto);
        when(bookService.getBooks()).thenReturn(allBooks);

        List<BookDto> actualBooks = bookController.books().getBody();

        assertThat(actualBooks.size()).isEqualTo(1);
    }

    private BookDto getBookDto() {
        return BookDto.builder()
                .id(UUID.randomUUID())
                .title("Gangadhar")
                .description("gangadhar book")
                .release_year(2002)
                .build();
    }
}
