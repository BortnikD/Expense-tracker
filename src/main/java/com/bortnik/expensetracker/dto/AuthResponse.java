package com.bortnik.expensetracker.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthResponse {
    String tokenType;
    String token;
    String username;
}
