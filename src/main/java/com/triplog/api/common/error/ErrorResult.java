package com.triplog.api.common.error;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResult {

    private List<ErrorDetail> errorDetails;

    @Builder
    public ErrorResult(Errors errors) {
        this.errorDetails = errors.getFieldErrors()
                .stream()
                .map(ErrorDetail::new)
                .toList();
    }
}
