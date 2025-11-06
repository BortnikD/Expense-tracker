package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.ApiError;
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
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(AccessError.class)
    ResponseEntity<ApiError> handleAccessError(AccessError accessError) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Access Error")
                        .message(accessError.getMessage())
                        .status(HttpStatus.FORBIDDEN)
                        .build()
        );
    }

    @ExceptionHandler(UserNotFound.class)
    ResponseEntity<ApiError> handleUserNotFound(UserNotFound userNotFound) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("User Not Found")
                        .message(userNotFound.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    @ExceptionHandler(UserAlreadyExists.class)
    ResponseEntity<ApiError> handleUserAlreadyExists(UserAlreadyExists userAlreadyExists) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("User Already Exists")
                        .message(userAlreadyExists.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .build()
        );
    }

    @ExceptionHandler(ExpensesNotFound.class)
    ResponseEntity<ApiError> handleExpensesNotFound(ExpensesNotFound expensesNotFound) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Expenses Not Found")
                        .message(expensesNotFound.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    @ExceptionHandler(NotificationNotFound.class)
    ResponseEntity<ApiError> handleNotificationNotFound(NotificationNotFound notificationNotFound) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Notification Not Found")
                        .message(notificationNotFound.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        );
    }


    @ExceptionHandler(CategoryNotFound.class)
    ResponseEntity<ApiError> handleCategoryNotFound(CategoryNotFound categoryNotFound) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Category Not Found")
                        .message(categoryNotFound.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    @ExceptionHandler(CategoryAlreadyExists.class)
    ResponseEntity<ApiError> handleCategoryAlreadyExists(CategoryAlreadyExists categoryAlreadyExists) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Category Already Exists")
                        .message(categoryAlreadyExists.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .build()
        );
    }

    @ExceptionHandler(BudgetPlanNotFound.class)
    ResponseEntity<ApiError> handleBudgetPlanNotFound(BudgetPlanNotFound budgetPlanNotFound) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("BudgetPlan Not Found")
                        .message(budgetPlanNotFound.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    @ExceptionHandler(BudgetPlanAlreadyExists.class)
    ResponseEntity<ApiError> handleBudgetPlanAlreadyExists(BudgetPlanAlreadyExists budgetPlanAlreadyExists) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Budget Plan Already Exists")
                        .message(budgetPlanAlreadyExists.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .build()
        );
    }

    @ExceptionHandler(InvalidJwtToken.class)
    ResponseEntity<ApiError> handleInvalidJwtToken(InvalidJwtToken invalidJwtToken) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Invalid Jwt Token")
                        .message(invalidJwtToken.getMessage())
                        .status(HttpStatus.UNAUTHORIZED)
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));

        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Validation Error")
                        .message("Invalid fields")
                        .status(HttpStatus.BAD_REQUEST)
                        .meta(fieldErrors)
                        .build()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException exception) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Constraint Violation Exception")
                        .message(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }

    @ExceptionHandler(BadRequest.class)
    ResponseEntity<ApiError> handleBadRequest(BadRequest exception) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Bad Request")
                        .message(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Method Argument Type Mismatch Exception")
                        .message(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> handleException(Exception exception) {
        return buildResponseEntity(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .error("Internal Server Error")
                        .message(exception.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()
        );
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
