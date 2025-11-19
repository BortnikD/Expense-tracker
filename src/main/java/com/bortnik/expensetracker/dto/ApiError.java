package com.bortnik.expensetracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Value
@Builder
public class ApiError {
    String error;
    String message;
    HttpStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String, String> meta;
}
