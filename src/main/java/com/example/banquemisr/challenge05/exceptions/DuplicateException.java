package com.example.banquemisr.challenge05.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  
    public DuplicateException() {
      super();
    }
  
    public DuplicateException(String message) {
      super(message);
    }
}