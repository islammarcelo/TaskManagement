package com.example.banquemisr.challenge05.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.banquemisr.challenge05.dtos.request.CreateTaskResquestDTO;
import com.example.banquemisr.challenge05.dtos.request.SearchTaskRequestDTO;
import com.example.banquemisr.challenge05.dtos.response.TaskResponseDTO;
import com.example.banquemisr.challenge05.enums.TaskStatus;
import com.example.banquemisr.challenge05.exceptions.InvalidInputException;
import com.example.banquemisr.challenge05.exceptions.NotFoundException;
import com.example.banquemisr.challenge05.models.Task;
import com.example.banquemisr.challenge05.repositories.TaskRepository;
import com.example.banquemisr.challenge05.services.specification.TaskSpec;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock private TaskRepository mockTaskRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private EmailSender mockEmailSender;
    @Mock private AuthenticationService mockAuthenticationService;
    @Mock private UserService mockUserService;
    @InjectMocks private TaskService mockTaskService;

        @Test
    void testCreateTask_Success() {
        // Arrange
        CreateTaskResquestDTO requestDTO = new CreateTaskResquestDTO();
        requestDTO.setTaskStatus("TODO");
        requestDTO.setTitle("Test Task");

        Task task = new Task();
        task.setTaskStatus(TaskStatus.TODO);
        task.setTitle("Test Task");

        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setTaskStatus(TaskStatus.TODO);
        taskResponseDTO.setTitle("Test Task");

        when(modelMapper.map(requestDTO, Task.class)).thenReturn(task);
        when(mockTaskRepository.save(task)).thenReturn(task);
        when(modelMapper.map(task, TaskResponseDTO.class)).thenReturn(taskResponseDTO);

        // Act
        TaskResponseDTO result = mockTaskService.createTask(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(TaskStatus.TODO, result.getTaskStatus());
        assertEquals("Test Task", result.getTitle());

        verify(mockTaskRepository, times(1)).save(any(Task.class));
    }

        @Test
    void testCreateTask_InvalidTaskStatus_ThrowsException() {
        // Arrange
        CreateTaskResquestDTO requestDTO = new CreateTaskResquestDTO();
        requestDTO.setTaskStatus("INVALID");

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            mockTaskService.createTask(requestDTO);
        });

        assertEquals("Invalid task status: INVALID should be like TODO, IN_PROGRESS or DONE", exception.getMessage());
        verifyNoInteractions(mockTaskRepository);
    }

    @Test
    void testCreateTask_NullTaskStatus_ThrowsException() {
        // Arrange
        CreateTaskResquestDTO requestDTO = new CreateTaskResquestDTO();
        requestDTO.setTaskStatus(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mockTaskService.createTask(requestDTO);
        });

        assertEquals("Task status must not be null", exception.getMessage());
        verifyNoInteractions(mockTaskRepository);
    }

    @Test
    void testCreateTask_BlankTaskStatus_ThrowsException() {
        // Arrange
        CreateTaskResquestDTO requestDTO = new CreateTaskResquestDTO();
        requestDTO.setTaskStatus(" ");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mockTaskService.createTask(requestDTO);
        });

        assertEquals("Task status must not be null", exception.getMessage());
        verifyNoInteractions(mockTaskRepository);
    }

        @Test
    void testGetTasks_Success() {
        // Arrange
        SearchTaskRequestDTO searchTaskRequestDTO = new SearchTaskRequestDTO();
        searchTaskRequestDTO.setTaskStatus("TODO");
        Pageable pageable = PageRequest.of(0, 10);

        Task task1 = new Task();
        task1.setTaskStatus(TaskStatus.TODO);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTaskStatus(TaskStatus.TODO);
        task2.setTitle("Task 2");

        List<Task> taskList = List.of(task1, task2);
        Page<Task> taskPage = new PageImpl<>(taskList, pageable, taskList.size());

        TaskResponseDTO responseDTO1 = new TaskResponseDTO();
        responseDTO1.setTaskStatus(TaskStatus.TODO);
        responseDTO1.setTitle("Task 1");

        TaskResponseDTO responseDTO2 = new TaskResponseDTO();
        responseDTO2.setTaskStatus(TaskStatus.TODO);
        responseDTO2.setTitle("Task 2");

        when(mockTaskRepository.findAll(any(TaskSpec.class), eq(pageable))).thenReturn(taskPage);
        when(modelMapper.map(task1, TaskResponseDTO.class)).thenReturn(responseDTO1);
        when(modelMapper.map(task2, TaskResponseDTO.class)).thenReturn(responseDTO2);

        // Act
        Page<TaskResponseDTO> result = mockTaskService.getTasks(searchTaskRequestDTO, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Task 1", result.getContent().get(0).getTitle());
        assertEquals("Task 2", result.getContent().get(1).getTitle());

        verify(mockTaskRepository, times(1)).findAll(any(TaskSpec.class), eq(pageable));
    }

        @Test
    void testDeleteTask_Success() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        mockTaskService.deleteTask(taskId);

        // Assert
        verify(mockTaskRepository, times(1)).findById(taskId);
        verify(mockTaskRepository, times(1)).delete(task);
    }

    @Test
    void testDeleteTask_TaskNotFound_ThrowsException() {
        // Arrange
        Long taskId = 1L;

        when(mockTaskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            mockTaskService.deleteTask(taskId);
        });

        assertEquals("Task with id = " + taskId + " not found", exception.getMessage());
        verify(mockTaskRepository, times(1)).findById(taskId);
        verify(mockTaskRepository, never()).delete(any(Task.class));
    }

   
}
