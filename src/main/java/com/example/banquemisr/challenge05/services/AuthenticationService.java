package com.example.banquemisr.challenge05.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
public class AuthenticationService {

    public static String getUsername() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
      return userPrincipal.getUsername();
    }
  }
