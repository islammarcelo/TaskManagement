package com.example.banquemisr.challenge05.dtos.response;

import lombok.Data;

@Data
public class ResponseDTO<T> {
    private T data;
    private String message = "";
}
