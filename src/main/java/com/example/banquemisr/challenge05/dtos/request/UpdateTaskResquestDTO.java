package com.example.banquemisr.challenge05.dtos.request;

import lombok.Data;

@Data
public class UpdateTaskResquestDTO {
    private String title;
    private String description;
    private String taskStatus;
    private String priority;
    private String dueDate;
}
