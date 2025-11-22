package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.ApiError;
import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.exceptions.BadCredentials;
import com.bortnik.expensetracker.exceptions.BadRequest;
import com.bortnik.expensetracker.exceptions.auth.InvalidJwtToken;
import com.bortnik.expensetracker.exceptions.budget.BudgetPlanAlreadyExists;
import com.bortnik.expensetracker.exceptions.budget.BudgetPlanNotFound;
import com.bortnik.expensetracker.exceptions.category.CategoryAlreadyExists;
import com.bortnik.expensetracker.exceptions.category.CategoryNotFound;
import com.bortnik.expensetracker.exceptions.expenses.ExpensesNotFound;
import com.bortnik.expensetracker.exceptions.notification.NotificationNotFound;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.exceptions.user.UserAlreadyExists;
import com.bortnik.expensetracker.exceptions.user.UserNotFound;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(AccessError.class)
    ResponseEntity<ApiResponse<ApiError>> handleAccessError(AccessError accessError) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("Access Error")
                        .message(accessError.getMessage())
                        .status(HttpStatus.FORBIDDEN)
                        .build()
        );
    }

    @ExceptionHandler(UserNotFound.class)
    ResponseEntity<ApiResponse<ApiError>> handleUserNotFound(UserNotFound userNotFound) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("User Not Found")
                        .message(userNotFound.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    @ExceptionHandler(UserAlreadyExists.class)
    ResponseEntity<ApiResponse<ApiError>> handleUserAlreadyExists(UserAlreadyExists userAlreadyExists) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("User Already Exists")
                        .message(userAlreadyExists.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .build()
        );
    }

    @ExceptionHandler(ExpensesNotFound.class)
    ResponseEntity<ApiResponse<ApiError>> handleExpensesNotFound(ExpensesNotFound expensesNotFound) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("Expenses Not Found")
                        .message(expensesNotFound.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    @ExceptionHandler(NotificationNotFound.class)
    ResponseEntity<ApiResponse<ApiError>> handleNotificationNotFound(NotificationNotFound notificationNotFound) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("Notification Not Found")
                        .message(notificationNotFound.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        );
    }


    @ExceptionHandler(CategoryNotFound.class)
    ResponseEntity<ApiResponse<ApiError>> handleCategoryNotFound(CategoryNotFound categoryNotFound) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("Category Not Found")
                        .message(categoryNotFound.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    @ExceptionHandler(CategoryAlreadyExists.class)
    ResponseEntity<ApiResponse<ApiError>> handleCategoryAlreadyExists(CategoryAlreadyExists categoryAlreadyExists) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("Category Already Exists")
                        .message(categoryAlreadyExists.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .build()
        );
    }

    @ExceptionHandler(BudgetPlanNotFound.class)
    ResponseEntity<ApiResponse<ApiError>> handleBudgetPlanNotFound(BudgetPlanNotFound budgetPlanNotFound) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("BudgetPlan Not Found")
                        .message(budgetPlanNotFound.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    @ExceptionHandler(BudgetPlanAlreadyExists.class)
    ResponseEntity<ApiResponse<ApiError>> handleBudgetPlanAlreadyExists(BudgetPlanAlreadyExists budgetPlanAlreadyExists) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("Budget Plan Already Exists")
                        .message(budgetPlanAlreadyExists.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .build()
        );
    }

    @ExceptionHandler(InvalidJwtToken.class)
    ResponseEntity<ApiResponse<ApiError>> handleInvalidJwtToken(InvalidJwtToken invalidJwtToken) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("Invalid Jwt Token")
                        .message(invalidJwtToken.getMessage())
                        .status(HttpStatus.UNAUTHORIZED)
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<ApiError>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));

        return buildResponseEntity(
                ApiError.builder()
                        .error("Validation Error")
                        .message("Invalid fields")
                        .status(HttpStatus.BAD_REQUEST)
                        .meta(fieldErrors)
                        .build()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiResponse<ApiError>> handleConstraintViolationException(ConstraintViolationException exception) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("Constraint Violation Exception")
                        .message(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }

    @ExceptionHandler(BadRequest.class)
    ResponseEntity<ApiResponse<ApiError>> handleBadRequest(BadRequest exception) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("Bad Request")
                        .message(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ApiResponse<ApiError>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return buildResponseEntity(
          ApiError.builder()
                        .error("Method Argument Type Mismatch Exception")
                        .message(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }

    @ExceptionHandler(BadCredentials.class)
    ResponseEntity<ApiResponse<ApiError>> handleBadCredentials(BadCredentials exception) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("Bad Credentials")
                        .message(exception.getMessage())
                        .status(HttpStatus.UNAUTHORIZED)
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<ApiError>> handleException(Exception exception) {
        return buildResponseEntity(
                ApiError.builder()
                        .error("Internal Server Error")
                        .message(exception.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()
        );
    }

    private ResponseEntity<ApiResponse<ApiError>> buildResponseEntity(ApiError apiError) {
        ApiResponse<ApiError> errorResponse = ApiResponseFactory.error(apiError);
        return ResponseEntity.status(apiError.getStatus()).body(errorResponse);
    }
}
