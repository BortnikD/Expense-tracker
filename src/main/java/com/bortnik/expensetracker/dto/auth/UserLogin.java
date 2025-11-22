package com.bortnik.expensetracker.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
final public class UserLogin {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be great then 8")
    private String password;
}
