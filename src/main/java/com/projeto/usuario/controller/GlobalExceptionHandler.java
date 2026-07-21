package com.projeto.usuario.controller;

import com.projeto.usuario.infrastructure.exception.ConflictException;
import com.projeto.usuario.infrastructure.exception.IllegalArgumentException;
import com.projeto.usuario.infrastructure.exception.ResourceNotFondException;
import com.projeto.usuario.infrastructure.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFondException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFondException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String>handleConflictException(ConflictException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String>handleUnauthorizedException(UnauthorizedException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
