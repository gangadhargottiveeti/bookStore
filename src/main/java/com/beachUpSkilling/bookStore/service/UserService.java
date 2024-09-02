package com.beachUpSkilling.bookStore.service;

import com.beachUpSkilling.bookStore.dto.UserRequest;
import com.beachUpSkilling.bookStore.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse saveUser(UserRequest userRequest);
    UserResponse getUser();
    List<UserResponse> getAllUser();
}
