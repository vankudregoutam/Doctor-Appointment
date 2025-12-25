package com.tek.exceptions.leave;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class LeaveAlreadyExistsException extends RuntimeException {
    public LeaveAlreadyExistsException(String message) {
        super(message);
    }
}
