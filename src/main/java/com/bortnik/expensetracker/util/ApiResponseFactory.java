package com.bortnik.expensetracker.util;

import com.bortnik.expensetracker.dto.ApiError;
import com.bortnik.expensetracker.dto.ApiResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiResponseFactory {

    public <T> ApiResponse<T> success(final T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .success(true)
                .build();
    }

    public ApiResponse<ApiError> error(final ApiError apiError) {
        return ApiResponse.<ApiError>builder()
                .apiError(apiError)
                .success(false)
                .build();
    }
}
