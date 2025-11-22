package com.bortnik.expensetracker.dto.user;

import com.bortnik.expensetracker.entities.UserRole;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserCreateDTO {
    String username;
    String email;
    String password;
    UserRole role;
}
