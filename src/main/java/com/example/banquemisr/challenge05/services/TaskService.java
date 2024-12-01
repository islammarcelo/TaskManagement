package com.example.banquemisr.challenge05.services;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.banquemisr.challenge05.dtos.request.CreateTaskResquestDTO;
import com.example.banquemisr.challenge05.dtos.request.SearchTaskRequestDTO;
import com.example.banquemisr.challenge05.dtos.request.UpdateTaskResquestDTO;
import com.example.banquemisr.challenge05.dtos.response.TaskResponseDTO;
import com.example.banquemisr.challenge05.enums.TaskStatus;
import com.example.banquemisr.challenge05.exceptions.InvalidInputException;
import com.example.banquemisr.challenge05.exceptions.NotFoundException;
import com.example.banquemisr.challenge05.models.Task;
import com.example.banquemisr.challenge05.models.User;
import com.example.banquemisr.challenge05.repositories.TaskRepository;
import com.example.banquemisr.challenge05.services.specification.TaskSpec;

@Service
public class TaskService {
    @Autowired private TaskRepository taskRepository;
    @Autowired private ModelMapper modelMapper;
    @Autowired private UserService userService;
    @Autowired private EmailService emailService;

    
    public TaskResponseDTO createTask(CreateTaskResquestDTO requestDTO) {
        validateTaskStatus(requestDTO.getTaskStatus());
        Task task = modelMapper.map(requestDTO, Task.class);
        task = taskRepository.save(task);
        TaskResponseDTO taskResponseDTO = modelMapper.map(task, TaskResponseDTO.class);
        return taskResponseDTO;
    }

    private void validateTaskStatus(String taskStatus) {
        if (taskStatus == null || taskStatus.isBlank()) {
            throw new IllegalArgumentException("Task status must not be null");
        }
        try {
            // Try converting the input string to a TaskStatus enum
            TaskStatus.valueOf(taskStatus.toUpperCase()); // Ensure case-insensitivity
        } catch (IllegalArgumentException e) {
            // If conversion fails, the taskStatus is invalid
            throw new InvalidInputException("Invalid task status: " + taskStatus + " should be like TODO, IN_PROGRESS or DONE");
        }
    }

    public Page<TaskResponseDTO> getTasks(SearchTaskRequestDTO searchTaskRequestDTO, Pageable pageable) {
        TaskSpec taskSpec = TaskSpec.builder()
            .requestDTO(searchTaskRequestDTO).build();
        Page<Task> tasks =
        taskRepository.findAll(taskSpec, pageable); 
        Page<TaskResponseDTO> taskPage =
        tasks.map(
            task ->
                modelMapper.map(
                    task, TaskResponseDTO.class));   
        return taskPage;            
    }

    public void deleteTask(Long taskId){
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new NotFoundException("Task with id = " + taskId + " not found"));
        taskRepository.delete(task);
    }

    @Transactional
    public TaskResponseDTO updateTask(UpdateTaskResquestDTO requestDTO, Long id) {
        String username = AuthenticationService.getUsername();
        User user = userService.getByUsername(username);
        Task task = taskRepository.findById(id).orElseThrow(()-> new NotFoundException("Task with id = " + id + " not found"));
        if(requestDTO.getTitle() != null && !requestDTO.getTitle().isEmpty())
            task.setTitle(requestDTO.getTitle());
        if(requestDTO.getDescription() != null && !requestDTO.getDescription().isEmpty())
            task.setDescription(requestDTO.getDescription());
        if(requestDTO.getPriority() != null && !requestDTO.getPriority().isEmpty())
            task.setPriority(requestDTO.getPriority());
        if(requestDTO.getTaskStatus() != null && !requestDTO.getTaskStatus().isEmpty()){
            validateTaskStatus(requestDTO.getTaskStatus());
            task.setTaskStatus(TaskStatus.valueOf(requestDTO.getTaskStatus().toUpperCase()));        
        }
        if(requestDTO.getDueDate() != null && !requestDTO.getDueDate().isEmpty()){
            try {
                LocalDate parsedDate = LocalDate.parse(requestDTO.getDueDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                task.setDueDate(parsedDate);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format for dueDate: " + requestDTO.getDueDate());
            }
        }
        task = taskRepository.save(task);
        TaskResponseDTO taskResponseDTO = modelMapper.map(task, TaskResponseDTO.class);
        EmailSender.sendEmail(
            user.getEmail(),
            "Updated task successfully",
            "Update Task",
            emailService);

        return taskResponseDTO;
    }


}
