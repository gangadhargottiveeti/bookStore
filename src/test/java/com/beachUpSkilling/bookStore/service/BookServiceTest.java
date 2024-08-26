package com.beachUpSkilling.bookStore.service;

import com.beachUpSkilling.bookStore.dto.BookDto;
import com.beachUpSkilling.bookStore.model.Book;
import com.beachUpSkilling.bookStore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ModelMapper mapper;

    @Test
    void shouldReturnListOfBookDtoWhenGetBooksCalled() {
        List<Book> books = new ArrayList<>();
        Book book = getBook();
        books.add(book);
        BookDto bookDto = getBookDto();
        when(bookRepository.findAll()).thenReturn(books);
        when(mapper.map(book, BookDto.class)).thenReturn(bookDto);

        List<BookDto> actualBooks = bookService.getBooks();

        assertThat(actualBooks.size()).isEqualTo(1);
        assertThat(actualBooks.get(0))
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", "Gangadhar")
                .hasFieldOrPropertyWithValue("description", "gangadhar book")
                .hasFieldOrPropertyWithValue("release_year", 2002);
    }

    @Test
    void shouldReturnListOfBookDtoWhenBookTitleIsGivenIgnoreCase() {
        List<Book> books = new ArrayList<>();
        Book book = getBook();
        books.add(book);
        BookDto bookDto = getBookDto();
        when(mapper.map(book, BookDto.class)).thenReturn(bookDto);
        when(bookRepository.findBooksByTitleIgnoreCase(anyString())).thenReturn(books);

        List<BookDto> actualBooks = bookService.getBooksByTitleIgnoreCase("gangadhar");

        assertThat(actualBooks.size()).isEqualTo(1);
    }

    private Book getBook() {
        return Book.builder()
                .id(UUID.randomUUID())
                .title("Gangadhar")
                .description("gangadhar book")
                .release_year(2002)
                .build();
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
