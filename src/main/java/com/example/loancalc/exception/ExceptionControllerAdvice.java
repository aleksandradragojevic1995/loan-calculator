package com.example.loancalc.exception;

import com.example.loancalc.dto.error.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ErrorResponseDTO handleValidationExceptions(MethodArgumentNotValidException exception) {
        StringBuilder errorMessage = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String errorName = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
            errorMessage.append(errorName).append(" - ").append(error.getDefaultMessage()).append(";");
        });
        return new ErrorResponseDTO(errorMessage.toString(), HttpStatus.BAD_REQUEST.toString());
    }
}
