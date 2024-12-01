package com.example.banquemisr.challenge05.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banquemisr.challenge05.dtos.request.CreateTaskResquestDTO;
import com.example.banquemisr.challenge05.dtos.request.SearchTaskRequestDTO;
import com.example.banquemisr.challenge05.dtos.request.UpdateTaskResquestDTO;
import com.example.banquemisr.challenge05.dtos.response.ResponseDTO;
import com.example.banquemisr.challenge05.dtos.response.ResponseMessageDTO;
import com.example.banquemisr.challenge05.dtos.response.TaskResponseDTO;
import com.example.banquemisr.challenge05.exceptions.InvalidInputException;
import com.example.banquemisr.challenge05.services.TaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/task")
@Api(tags = "Tasks")
public class TaskController {
    @Autowired private TaskService taskService;

    @PostMapping
    @ApiOperation("Create Task")
    public ResponseEntity<ResponseDTO<TaskResponseDTO>> createTask (
        @RequestBody @Valid CreateTaskResquestDTO requestDTO){
        TaskResponseDTO  taskResponseDTO = taskService.createTask(requestDTO);
        String message = "Created task successfully";
        ResponseDTO<TaskResponseDTO> response= new ResponseDTO<TaskResponseDTO>();
        response.setData(taskResponseDTO);
        response.setMessage(message);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getTasks(
        @ModelAttribute SearchTaskRequestDTO searchTaskRequestDTO,
        Pageable pageable
    ) {
        Page<TaskResponseDTO> tasks = taskService.getTasks(searchTaskRequestDTO, pageable);
        
        return new ResponseEntity<>(tasks, HttpStatus.OK);        
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Delete Task")
    public ResponseEntity<ResponseMessageDTO> deleteTask (
        @PathVariable("id") Long id){
        taskService.deleteTask(id);
        String message = "Deleted task successfully";
        ResponseMessageDTO response= new ResponseMessageDTO();
        response.setMessage(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation("Update Task")
    public ResponseEntity<ResponseDTO<TaskResponseDTO>> upateTask (
        @RequestBody UpdateTaskResquestDTO requestDTO,
        @PathVariable("id") Long id){
        if(isEmptyRequest(requestDTO)){
            throw new InvalidInputException("At least one parameter must be provided in the request body.");
        }    
        TaskResponseDTO  taskResponseDTO = taskService.updateTask(requestDTO, id);
        String message = "Updated task successfully";
        ResponseDTO<TaskResponseDTO> response= new ResponseDTO<TaskResponseDTO>();
        response.setData(taskResponseDTO);
        response.setMessage(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean isEmptyRequest(UpdateTaskResquestDTO requestDTO) {
        return requestDTO == null || 
               (requestDTO.getTitle() == null && 
                requestDTO.getDescription() == null && 
                requestDTO.getPriority() == null && 
                requestDTO.getDueDate() == null &&
                requestDTO.getTaskStatus() == null);
    }




}
