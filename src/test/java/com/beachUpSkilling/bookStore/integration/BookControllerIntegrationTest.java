package com.beachUpSkilling.bookStore.integration;

import com.beachUpSkilling.bookStore.BookStoreApplication;
import com.beachUpSkilling.bookStore.dto.BookDto;
import com.beachUpSkilling.bookStore.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BookStoreApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JwtService jwtService;

    @Test
    @Sql(scripts = {"classpath:InsertInitialBookRecordForTest.sql"})
    void shouldReturnBooksWhenBooksApiIsCalled() {
        BookDto[] listOfBooks = testRestTemplate.getForObject("http://localhost:" + port + "/bookStore/books", BookDto[].class);
        assertThat(listOfBooks).isNotNull();
        assertThat(listOfBooks.length).isEqualTo(2);
    }

    @Test
    @Sql(scripts = {"classpath:InsertInitialBookRecordForTest.sql", "classpath:InsertUserForTest.sql"})
    void shouldReturnBooksWhenTitleIsGivenAsPathVariable() {

        String jwtToken = jwtService.GenerateToken("user");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<BookDto[]> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/bookStore/books/gangadhar",
                HttpMethod.GET,
                entity,
                BookDto[].class
        );

        BookDto[] listOfBooks = response.getBody();
        assertThat(listOfBooks).isNotNull();
        assertThat(listOfBooks.length).isEqualTo(1);
    }
}
