package com.triplog.api.exception.handler;

import static com.triplog.api.exception.constants.ErrorConstants.MESSAGE_ERROR_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.triplog.api.exception.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HttpStatus httpStatus = BAD_REQUEST;
        log.error("MethodArgumentNotValidException", e);
        final ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.of(httpStatus, e.getMessage(), e.getFieldErrors());
        return ResponseEntity.status(httpStatus).body(errorResponseDTO);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        HttpStatus httpStatus = UNAUTHORIZED;
        log.error("AccessDeniedException", e);
        final ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.of(httpStatus, MESSAGE_ERROR_UNAUTHORIZED);
        return ResponseEntity.status(httpStatus).body(errorResponseDTO);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        HttpStatus httpStatus = BAD_REQUEST;
        log.error("IllegalStateException", e);
        final ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus).body(errorResponseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e) {
        HttpStatus httpStatus = INTERNAL_SERVER_ERROR;
        log.error("Exception", e);
        final ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.of(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus).body(errorResponseDTO);
    }
}
