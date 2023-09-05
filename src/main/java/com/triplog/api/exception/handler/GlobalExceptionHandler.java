package com.triplog.api.exception.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.triplog.api.exception.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        final ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.of(BAD_REQUEST, e.getMessage(), e.getFieldErrors());
        return ResponseEntity.status(errorResponseDTO.getHttpStatus()).body(errorResponseDTO);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalStateException", e);
        final ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.of(BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(errorResponseDTO.getHttpStatus()).body(errorResponseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e) {
        log.error("Exception", e);
        final ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.of(INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(errorResponseDTO.getHttpStatus()).body(errorResponseDTO);
    }
}
