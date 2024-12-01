package com.example.banquemisr.challenge05.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.banquemisr.challenge05.models.Task;

public interface TaskRepository extends 
    JpaRepository<Task, Long>, 
        JpaSpecificationExecutor<Task>{

    List<Task> findByDueDate(LocalDate today);
    
} 
