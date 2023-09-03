package com.triplog.api.exception.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

/**
 * {
 *      "httpStatusCode":400,
 *      "httpStatus":"BAD_REQUEST",
 *      "message":"....",
 *      "errors": {
 *          "field": "",
 *          "value": "",
 *          "message": ""
 *      }
 * }
 */
@Getter
@ToString
public class ErrorResponse {

    private final int httpStatusCode;
    private final HttpStatus httpStatus;
    private final String message;
    private final List<CustomFieldError> errors;

    @Builder
    private ErrorResponse(HttpStatus httpStatus, String message, List<CustomFieldError> errors) {
        this.httpStatusCode = httpStatus.value();
        this.httpStatus = httpStatus;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return ErrorResponse.builder()
                .httpStatus(httpStatus)
                .message(message)
                .errors(Collections.emptyList())
                .build();
    }

    public static ErrorResponse of(HttpStatus httpStatus, String message, List<FieldError> fieldErrors) {
        List<CustomFieldError> customFieldErrors = fieldErrors.stream()
                .map(CustomFieldError::new)
                .toList();
        return ErrorResponse.builder()
                .httpStatus(httpStatus)
                .message(message)
                .errors(customFieldErrors)
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class CustomFieldError {
        private String field;
        private String value;
        private String message;

        private CustomFieldError(FieldError fieldError) {
            this.field = fieldError.getField();
            this.value = Objects.requireNonNull(fieldError.getRejectedValue()).toString();
            this.message = fieldError.getDefaultMessage();
        }
    }
}
