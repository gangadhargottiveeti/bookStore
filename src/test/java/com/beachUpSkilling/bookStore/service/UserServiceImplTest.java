package com.beachUpSkilling.bookStore.service;

import com.beachUpSkilling.bookStore.dto.UserRequest;
import com.beachUpSkilling.bookStore.dto.UserResponse;
import com.beachUpSkilling.bookStore.model.UserInfo;
import com.beachUpSkilling.bookStore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldReturnAllUsersWhenGetAllUserMethodIsCalled() {
        UserInfo testUser = getTestUser();
        List<UserInfo> users = new ArrayList<>();
        users.add(testUser);

        UserResponse testUserResponse = getTestUserResponse();
        List<UserResponse> userResponses = new ArrayList<>();
        userResponses.add(testUserResponse);

        Type setOfDTOsType = new TypeToken<List<UserResponse>>(){}.getType();
        when(userRepository.findAll()).thenReturn(users);
        when(modelMapper.map(users, setOfDTOsType)).thenReturn(userResponses);

        List<UserResponse> actualUsers = userService.getAllUser();

        assertThat(actualUsers.size()).isEqualTo(1);
        assertEquals(userResponses, actualUsers);
        assertThat(actualUsers.get(0))
                .isNotNull()
                .hasFieldOrPropertyWithValue("username", "user");
    }

    @Test
    void shouldReturnUserResponseWhenGetUserIsCalled() {
        UserInfo testUser = getTestUser();
        UserResponse testUserResponse = getTestUserResponse();

        String username = "user";
        UserDetails userDetails = User
                .withUsername(username)
                .password("password")
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByUsername(username)).thenReturn(testUser);
        when(modelMapper.map(testUser, UserResponse.class)).thenReturn(testUserResponse);

        UserResponse result = userService.getUser();

        assertEquals(testUserResponse, result);

    }

    @Test
    void shouldSaveTheNewUserSuccessfully() {
        UserRequest userRequest = UserRequest.builder()
                .username("user")
                .password("password")
                .roles(null)
                .build();
        UserInfo testUser = new UserInfo();
        testUser.setUsername("user");
        testUser.setPassword("encodedPassword");
        UserResponse testUserResponse = new UserResponse();

        when(modelMapper.map(userRequest, UserInfo.class)).thenReturn(testUser);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(modelMapper.map(testUser, UserResponse.class)).thenReturn(testUserResponse);

        UserResponse result = userService.saveUser(userRequest);

        assertEquals(testUserResponse, result);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        UserRequest userRequest = UserRequest.builder()
                .username(null)
                .password("password")
                .roles(null)
                .build();

        RuntimeException exception = null;
        try {
            userService.saveUser(userRequest);
        } catch (RuntimeException e) {
            exception = e;
        }

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Parameter username is not found in request..!!");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        UserRequest userRequest = UserRequest.builder()
                .username("user")
                .password(null)
                .roles(null)
                .build();

        RuntimeException exception = null;
        try {
            userService.saveUser(userRequest);
        } catch (RuntimeException e) {
            exception = e;
        }

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Parameter password is not found in request..!!");
    }

    @Test
    void shouldUpdateExistingUserSuccessfully() {
        UserRequest userRequest = UserRequest.builder()
                .id(1L)
                .username("user")
                .password("password")
                .roles(null)
                .build();
        UserInfo existingUser = UserInfo.builder()
                .id(1L)
                .username("oldUsername")
                .password("oldPassword")
                .roles(null)
                .build();
        UserInfo updatedUser = getTestUser();
        UserResponse testUserResponse = getTestUserResponse();

        when(modelMapper.map(userRequest, UserInfo.class)).thenReturn(updatedUser);
        when(userRepository.findFirstById(userRequest.getId())).thenReturn(existingUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(modelMapper.map(updatedUser, UserResponse.class)).thenReturn(testUserResponse);

        UserResponse result = userService.saveUser(userRequest);

        assertEquals(testUserResponse, result);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        UserRequest userRequest = UserRequest.builder()
                .id(5L)
                .username("user")
                .password("password")
                .roles(null)
                .build();

        when(userRepository.findFirstById(userRequest.getId())).thenReturn(null);
        when(modelMapper.map(userRequest, UserInfo.class)).thenReturn(new UserInfo());
        RuntimeException exception = null;
        try {
            userService.saveUser(userRequest);
        } catch (RuntimeException e) {
            exception = e;
        }

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Can't find record with identifier: " + userRequest.getId());
    }

    private UserInfo getTestUser() {
        return UserInfo.builder()
                .id(1L)
                .username("user")
                .password("$2a$10$2.iUnYYY0xF8ynZd72cv5u0pNL2uXP0aA.1nLFJOzkqKqBIDzuJr.")
                .build();
    }

    private UserResponse getTestUserResponse() {
        return UserResponse.builder()
                .id(1L)
                .username("user")
                .roles(null)
                .build();
    }
}