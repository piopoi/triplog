package com.triplog.api.common.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class ErrorDetail {

    private final String objectName;
    private final String field;
    private final String code;
    private final String message;

    @Builder
    public ErrorDetail(FieldError fieldError) {
        this.objectName = fieldError.getObjectName();
        this.field = fieldError.getField();
        this.code = fieldError.getCode();
        this.message = fieldError.getDefaultMessage();
    }
}
