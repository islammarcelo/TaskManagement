package com.example.banquemisr.challenge05.dtos.request;

import lombok.Data;

@Data
public class SearchTaskRequestDTO {
    String query;
    String taskStatus;
    String dueDate;
}
