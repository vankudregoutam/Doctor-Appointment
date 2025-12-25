package com.tek.exceptions;

import com.tek.entity.dto.ExceptionResponseDTO;
import com.tek.exceptions.appointment.AppointmentAlreadyExistsException;
import com.tek.exceptions.doctor.DoctorAlreadyExistsException;
import com.tek.exceptions.doctor.DoctorInvalidCredentialsException;
import com.tek.exceptions.doctor.DoctorNotExistsException;
import com.tek.exceptions.leave.InvalidLeaveDateException;
import com.tek.exceptions.leave.LeaveAlreadyExistsException;
import com.tek.exceptions.user.InvalidCredentialsException;
import com.tek.exceptions.user.UserAlreadyExistsException;
import com.tek.exceptions.user.UserCanNotBeAdminException;
import com.tek.exceptions.user.UserNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> userAlreadyExistException(UserAlreadyExistsException exception, WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(request.getDescription(false), HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserCanNotBeAdminException.class)
    public ResponseEntity<ExceptionResponseDTO> userCanNotBeAdminException(UserCanNotBeAdminException exception,  WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(request.getDescription(false), HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> userNotExistsException(UserNotExistsException exception, WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(request.getDescription(false), HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponseDTO> invalidCredentials(InvalidCredentialsException exception, WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(request.getDescription(false), HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DoctorAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> doctorAlreadyExistsException(DoctorAlreadyExistsException exception, WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(request.getDescription(false), HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DoctorNotExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> doctorNotExistsException(DoctorNotExistsException doctorNotExistsException, WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(request.getDescription(false), HttpStatus.BAD_REQUEST, doctorNotExistsException.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DoctorInvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponseDTO> doctorInvalidCredentials(DoctorInvalidCredentialsException exception, WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(request.getDescription(false), HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppointmentAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> appointmentAlreadyExistsException(AppointmentAlreadyExistsException appointmentAlreadyExistsException, WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(request.getDescription(false),HttpStatus.ALREADY_REPORTED, appointmentAlreadyExistsException.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(LeaveAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> leaveAlreadyExistsException(LeaveAlreadyExistsException leaveAlreadyExistsException, WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(request.getDescription(false), HttpStatus.ALREADY_REPORTED, leaveAlreadyExistsException.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(InvalidLeaveDateException.class)
    public ResponseEntity<ExceptionResponseDTO> invalidLeaveDateException(InvalidLeaveDateException exception, WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(request.getDescription(false), HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ExceptionResponseDTO> unauthorizedUserException(UnauthorizedUserException unauthorizedUserException, WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(request.getDescription(false), HttpStatus.UNAUTHORIZED, unauthorizedUserException.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDTO> handleValidationExceptions(MethodArgumentNotValidException exception, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        String errorMessage = "Validation failed: " + errors.toString();
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                request.getDescription(false), 
                HttpStatus.BAD_REQUEST, 
                errorMessage, 
                LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseDTO> runtimeException(RuntimeException exception, WebRequest request) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                request.getDescription(false), 
                HttpStatus.INTERNAL_SERVER_ERROR, 
                exception.getMessage() != null ? exception.getMessage() : "An unexpected error occurred", 
                LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
