package ru.avg.managerapp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BadRequestException extends RuntimeException {

    private final List<String> errors = new ArrayList<>();

    public BadRequestException(List<String> errors) {
        this.errors.addAll(errors);
    }

    public BadRequestException(String message, List<String> errors) {
        super(message);
        this.errors.addAll(errors);
    }

    public BadRequestException(String message, Throwable cause, List<String> errors) {
        super(message, cause);
        this.errors.addAll(errors);
    }

    public BadRequestException(Throwable cause, List<String> errors) {
        super(cause);
        this.errors.addAll(errors);
    }

    public BadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, List<String> errors) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errors.addAll(errors);
    }
}
