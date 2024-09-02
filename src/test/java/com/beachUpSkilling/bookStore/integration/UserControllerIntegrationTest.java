package com.beachUpSkilling.bookStore.integration;

import com.beachUpSkilling.bookStore.BookStoreApplication;
import com.beachUpSkilling.bookStore.dto.AuthRequestDTO;
import com.beachUpSkilling.bookStore.dto.JwtResponseDTO;
import com.beachUpSkilling.bookStore.dto.UserRequest;
import com.beachUpSkilling.bookStore.dto.UserResponse;
import com.beachUpSkilling.bookStore.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = BookStoreApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private JwtService jwtService;

    @Test
    @Sql(scripts = {"classpath:InsertInitialBookRecordForTest.sql", "classpath:InsertUserForTest.sql"})
    void shouldSaveUserWhenSaveApiIsCalled() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("newuser");
        userRequest.setPassword("newpassword");

        ResponseEntity<UserResponse> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/save",
                userRequest,
                UserResponse.class
        );

        UserResponse savedUser = response.getBody();
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("newuser");
    }

    @Test
    @Sql(scripts = {"classpath:InsertUserForTest.sql"})
    void shouldAuthenticateUserAndReturnJwtTokenWhenLoginApiIsCalled() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setUsername("user");
        authRequestDTO.setPassword("password");

        ResponseEntity<JwtResponseDTO> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/login",
                authRequestDTO,
                JwtResponseDTO.class
        );

        JwtResponseDTO jwtResponse = response.getBody();
        assertThat(jwtResponse).isNotNull();
        assertThat(jwtResponse.getAccessToken()).isNotBlank();
    }

    @Test
    @Sql(scripts = {"classpath:InsertUserForTest.sql"})
    void shouldReturnForbiddenWhenInvalidCredentialsAreUsedInLoginApi() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setUsername("invaliduser");
        authRequestDTO.setPassword("wrongpassword");

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/login",
                authRequestDTO,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @Sql(scripts = {"classpath:InsertUserForTest.sql"})
    void shouldReturnListOfUsersWhenUsersApiIsCalled() {
        String jwtToken = jwtService.GenerateToken("user");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserResponse[]> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/users",
                HttpMethod.GET,
                entity,
                UserResponse[].class
        );

        UserResponse[] listOfUsers = response.getBody();
        assertThat(listOfUsers).isNotNull();
        assertThat(listOfUsers.length).isGreaterThan(0);
    }

}
