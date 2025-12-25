package com.tek.exceptions.doctor;

public class DoctorInvalidCredentialsException extends RuntimeException {
    public DoctorInvalidCredentialsException(String message) {
        super(message);
    }
}