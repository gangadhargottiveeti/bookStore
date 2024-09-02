package com.beachUpSkilling.bookStore.dto;

import com.beachUpSkilling.bookStore.model.UserRole;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequest {
    private Long id;
    private String username;
    private String password;
    private Set<UserRole> roles;
}
