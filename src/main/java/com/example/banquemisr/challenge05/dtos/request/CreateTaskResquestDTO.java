package com.example.banquemisr.challenge05.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTaskResquestDTO {
    @NotNull(message = "title must not be null")
    @NotBlank(message = "title must not be empty")
    private String title;

    @NotNull(message = "description must not be null")
    @NotBlank(message = "description must not be empty")
    private String description;

    @NotNull(message = "taskStatus must not be null")
    @NotBlank(message = "taskStatus must not be empty")
    private String taskStatus;

    @NotNull(message = "priority must not be null")
    @NotBlank(message = "priority must not be empty")
    private String priority;

    @NotNull(message = "dueDate must not be null")
    @NotBlank(message = "dueDate must not be empty")
    private String dueDate;
}
