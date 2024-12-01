package com.example.banquemisr.challenge05.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Syntactic/Semantic errors goes as 400. */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  
    public InvalidInputException() {
      super();
    }
  
    public InvalidInputException(String message) {
      super(message);
    }
}
