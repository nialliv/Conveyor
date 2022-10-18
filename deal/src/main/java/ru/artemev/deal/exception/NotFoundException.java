package ru.artemev.deal.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(String error) {
        super(HttpStatus.NOT_FOUND, new ApiError(NotFoundException.class.toString(), error));
    }
}
