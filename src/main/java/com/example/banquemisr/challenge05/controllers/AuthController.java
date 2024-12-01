package com.example.banquemisr.challenge05.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banquemisr.challenge05.dtos.request.LoginRequestDTO;
import com.example.banquemisr.challenge05.dtos.request.RegisterRequestDTO;
import com.example.banquemisr.challenge05.dtos.response.AuthResponseDTO;
import com.example.banquemisr.challenge05.dtos.response.ResponseDTO;
import com.example.banquemisr.challenge05.dtos.response.UserInfoResponseDTO;
import com.example.banquemisr.challenge05.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth")
@Api(tags = "Auth")
public class AuthController {
    @Autowired private UserService userService;

    @PostMapping(value = "/register")
    @ApiOperation("Register")
    public ResponseEntity<ResponseDTO<UserInfoResponseDTO>> register (
        @RequestBody @Valid RegisterRequestDTO requestDTO){
        UserInfoResponseDTO  userInfoResponseDTO = userService.register(requestDTO);
        String message = "User registered successfully";
        ResponseDTO<UserInfoResponseDTO> response= new ResponseDTO<UserInfoResponseDTO>();
        response.setData(userInfoResponseDTO);
        response.setMessage(message);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation("Login")
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<AuthResponseDTO>> login(@RequestBody @Valid LoginRequestDTO requestDTO) {
        
        AuthResponseDTO authResponseDTO = userService.login(requestDTO);
        ResponseDTO<AuthResponseDTO> response = new ResponseDTO<AuthResponseDTO>();
        String message = "User Login successfully ";
        response.setData(authResponseDTO);
        response.setMessage(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
