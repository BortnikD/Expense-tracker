package com.bortnik.expensetracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ApiError {
    private LocalDateTime timestamp;
    private String error;
    private String message;

    private HttpStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> meta;
}
