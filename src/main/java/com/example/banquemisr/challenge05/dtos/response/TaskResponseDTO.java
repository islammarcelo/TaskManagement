package com.example.banquemisr.challenge05.dtos.response;

import com.example.banquemisr.challenge05.enums.TaskStatus;

import lombok.Data;

@Data
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private String priority;
    private String dueDate;
}
