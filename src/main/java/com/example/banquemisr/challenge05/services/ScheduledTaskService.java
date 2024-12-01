package com.example.banquemisr.challenge05.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.banquemisr.challenge05.models.Task;
import com.example.banquemisr.challenge05.repositories.TaskRepository;

@Service
public class ScheduledTaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 0 * * ?") // Run every day at 12 AM
    public void sendDueDateEmails() {
        LocalDate today = LocalDate.now();
        List<Task> tasksDueToday = taskRepository.findByDueDate(today);

        if (!tasksDueToday.isEmpty()) {
            tasksDueToday.forEach(task -> {
                userService.getAllUser().forEach(user -> {
                    EmailSender.sendEmail(
                        user.getEmail(),
                        "Reminder: Task Due Today",
                        "Task Title: " + task.getTitle() + "\nDescription: " + task.getDescription(),
                        emailService
                    );
                });
            });
        }
    }
}
