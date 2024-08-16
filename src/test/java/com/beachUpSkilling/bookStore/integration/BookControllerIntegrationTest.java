package com.beachUpSkilling.bookStore.integration;

import com.beachUpSkilling.bookStore.BookStoreApplication;
import com.beachUpSkilling.bookStore.dto.BookDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BookStoreApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Sql(scripts = {"classpath:InsertInitialBookRecordForTest.sql"})
    void shouldReturnBooksWhenBooksApiIsCalled() {
        BookDto[] listOfBooks = testRestTemplate.getForObject("http://localhost:" + port + "/bookStore/books", BookDto[].class);
        assertThat(listOfBooks).isNotNull();
        assertThat(listOfBooks.length).isEqualTo(2);
    }
}
