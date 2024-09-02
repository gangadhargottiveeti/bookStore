package com.beachUpSkilling.bookStore.dto;

import com.beachUpSkilling.bookStore.model.UserRole;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {
    private Long id;
    private String username;
    private Set<UserRole> roles;
}
