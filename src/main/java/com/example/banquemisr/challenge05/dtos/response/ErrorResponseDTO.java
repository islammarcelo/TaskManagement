package com.example.banquemisr.challenge05.dtos.response;

import com.example.banquemisr.challenge05.enums.ErrorCode;

import lombok.Data;

@Data
public class ErrorResponseDTO {
  
    private ErrorCode errorCode;
  
    private Boolean success;
  
    private String error;
}
