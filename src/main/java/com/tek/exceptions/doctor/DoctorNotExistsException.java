package com.tek.exceptions.doctor;

public class DoctorNotExistsException extends RuntimeException {
    public DoctorNotExistsException(String message) {
        super(message);
    }
}
