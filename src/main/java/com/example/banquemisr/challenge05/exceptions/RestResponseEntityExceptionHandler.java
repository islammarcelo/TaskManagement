package com.example.banquemisr.challenge05.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.banquemisr.challenge05.dtos.response.ErrorResponseDTO;
import com.example.banquemisr.challenge05.enums.ErrorCode;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private ErrorResponseDTO response = new ErrorResponseDTO();

    // 404
    @ExceptionHandler({
            NotFoundException.class
    })
    protected ResponseEntity<Object> handleNotFound(
            final RuntimeException ex, final WebRequest request) {

        response.setError(ex.getMessage());
        response.setSuccess(false);
        response.setErrorCode(ErrorCode.NOT_FOUND);

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 409
    @ExceptionHandler({
            DuplicateException.class
    })
    protected ResponseEntity<Object> handleConflict(
            final RuntimeException ex, final WebRequest request) {

        response.setError(ex.getMessage());
        response.setSuccess(false);
        response.setErrorCode(ErrorCode.CONFLICT);
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // 400
    @ExceptionHandler({
            PasswordException.class,
            InvalidDateFormatException.class,
            InvalidInputException.class
    })
    protected ResponseEntity<Object> handleBadRequestException(
            final RuntimeException ex, final WebRequest request) {

        response.setError(ex.getMessage());
        response.setSuccess(false);
        response.setErrorCode(ErrorCode.BAD_REQUEST);

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
