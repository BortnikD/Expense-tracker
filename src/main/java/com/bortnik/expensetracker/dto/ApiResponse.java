package com.bortnik.expensetracker.dto;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Map;

@Value
@Builder
public class ApiResponse<T> {
    @Builder.Default
    OffsetDateTime timestamp = OffsetDateTime.now();

    boolean success;
    T data;
    ApiError apiError;
    Map<String, Object> metadata;
}
