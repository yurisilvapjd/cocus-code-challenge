package com.cocus.codechallenge.exceptions.handlers;

import com.cocus.codechallenge.dtos.response.ErrorResponse;
import com.cocus.codechallenge.exceptions.ResourceAlreadyExistsException;
import com.cocus.codechallenge.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> genericException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .withMessage(ex.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .withStatus(HttpStatus.NOT_FOUND.value())
                .withMessage(ex.getMessage()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ResourceAlreadyExistsException.class})
    public ResponseEntity<?> resourceAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .withStatus(HttpStatus.CONFLICT.value())
                .withMessage(ex.getMessage()).build(), HttpStatus.CONFLICT);
    }
}
