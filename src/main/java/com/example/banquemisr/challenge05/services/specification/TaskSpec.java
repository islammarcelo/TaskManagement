package com.example.banquemisr.challenge05.services.specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.banquemisr.challenge05.dtos.request.SearchTaskRequestDTO;
import com.example.banquemisr.challenge05.models.Task;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;

@Builder
public class TaskSpec implements Specification<Task> {
    private SearchTaskRequestDTO requestDTO;

    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        

        if(requestDTO.getQuery() != null && !requestDTO.getQuery().isEmpty()){
            String queryTerm = "%" + requestDTO.getQuery().toLowerCase() + "%";
            Predicate taskTitlePredicate =
            criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), queryTerm);
            Predicate taskDescriptionPredicate =
            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), queryTerm);
            Predicate taskPirorityPredicate =
            criteriaBuilder.like(criteriaBuilder.lower(root.get("priority")), queryTerm);
            predicates.add(criteriaBuilder.or(taskTitlePredicate, taskDescriptionPredicate, taskPirorityPredicate));
        }

        if (requestDTO.getDueDate() != null && !requestDTO.getDueDate().isEmpty()) {
                try {
            LocalDate parsedDate = LocalDate.parse(requestDTO.getDueDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            predicates.add(
                criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), parsedDate)
            );
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format for dueDate: " + requestDTO.getDueDate());
            }
        }

        if(requestDTO.getTaskStatus() != null && !requestDTO.getTaskStatus().isEmpty()){
            predicates.add(
                criteriaBuilder.equal(root.get("taskStatus"), requestDTO.getTaskStatus().toUpperCase()));
        }



        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

    }
    
}
