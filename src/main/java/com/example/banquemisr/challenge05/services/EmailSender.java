package com.example.banquemisr.challenge05.services;

import org.springframework.mail.SimpleMailMessage;

public class EmailSender {

  public static void sendEmail(
      String userEmail, String reply, String subject, EmailService emailService) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(userEmail);
    mailMessage.setSubject(subject);
    mailMessage.setText(reply);
    emailService.sendEmail(mailMessage);
  }
}
