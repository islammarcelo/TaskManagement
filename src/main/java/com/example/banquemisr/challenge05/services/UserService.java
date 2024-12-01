package com.example.banquemisr.challenge05.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.banquemisr.challenge05.dtos.request.LoginRequestDTO;
import com.example.banquemisr.challenge05.dtos.request.RegisterRequestDTO;
import com.example.banquemisr.challenge05.dtos.response.AuthResponseDTO;
import com.example.banquemisr.challenge05.dtos.response.UserInfoResponseDTO;
import com.example.banquemisr.challenge05.enums.Role;
import com.example.banquemisr.challenge05.exceptions.DuplicateException;
import com.example.banquemisr.challenge05.exceptions.InvalidInputException;
import com.example.banquemisr.challenge05.exceptions.NotFoundException;
import com.example.banquemisr.challenge05.models.User;
import com.example.banquemisr.challenge05.repositories.UserRepository;
import com.example.banquemisr.challenge05.security.JwtTokenProvider;



@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private AuthenticationManager authenticationManager;

    public UserInfoResponseDTO register(RegisterRequestDTO requestDTO) {

        validateRole(requestDTO.getRole());
        Optional<User> usernameOptional = userRepository.findByUsername(requestDTO.getUsername());
        if(usernameOptional.isPresent()){
            throw new DuplicateException("The username is already taken. Please choose a different one.");
        }

        Optional<User> emailOptional = userRepository.findByEmail(requestDTO.getEmail());
        if(emailOptional.isPresent()){
            throw new DuplicateException("The email is already taken. Please choose a different one.");
        }

        User user = createUser(requestDTO);
        
       return user.tResponse();
    }

    private void validateRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role must not be null");
        }
        try {
            Role.valueOf(role.toUpperCase()); // Ensure case-insensitivity
        } catch (IllegalArgumentException e) {
           throw new InvalidInputException("Invalid role : " + role + " should be like ADMIN or REQULAR_USER");
        }
    }

    private User createUser(RegisterRequestDTO requestDTO) {

        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setUsername(requestDTO.getUsername());
        user.setRole(Role.valueOf(requestDTO.getRole().toUpperCase()));
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        return userRepository.save(user);
    }

    public AuthResponseDTO login(LoginRequestDTO requestDTO) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword())
        );

        Optional<User> usernameOptional = userRepository.findByUsername(requestDTO.getUsername());
        if(!usernameOptional.isPresent()){
            throw new NotFoundException("The username is not found.");
        }

        String token = jwtTokenProvider.generateToken(usernameOptional.get());
        return new AuthResponseDTO (token);

    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("User with username not found"));
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }


    
}
