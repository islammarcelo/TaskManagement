package com.example.banquemisr.challenge05.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

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

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService mockUserService;

    @Test
    void testRegister_Success() {
        // Arrange
        RegisterRequestDTO requestDTO = new RegisterRequestDTO();
        requestDTO.setUsername("newUser");
        requestDTO.setEmail("newuser@example.com");
        requestDTO.setPassword("password123");
        requestDTO.setRole("ADMIN");

        User savedUser = new User();
        savedUser.setUsername(requestDTO.getUsername());
        savedUser.setEmail(requestDTO.getEmail());
        savedUser.setRole(Role.ADMIN);

        UserInfoResponseDTO responseDTO = new UserInfoResponseDTO();
        responseDTO.setUsername(savedUser.getUsername());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setRole(savedUser.getRole().name());

        when(mockUserRepository.findByUsername(requestDTO.getUsername())).thenReturn(Optional.empty());
        when(mockUserRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());
        when(mockPasswordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");
        when(mockUserRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserInfoResponseDTO result = mockUserService.register(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        assertEquals("newuser@example.com", result.getEmail());
        assertEquals("ADMIN", result.getRole());

        verify(mockUserRepository, times(1)).findByUsername(requestDTO.getUsername());
        verify(mockUserRepository, times(1)).findByEmail(requestDTO.getEmail());
        verify(mockUserRepository, times(1)).save(any(User.class));
    }

        @Test
    void testRegister_UsernameAlreadyExists_ThrowsDuplicateException() {
        // Arrange
        RegisterRequestDTO requestDTO = new RegisterRequestDTO();
        requestDTO.setUsername("existingUser");
        requestDTO.setEmail("newuser@example.com");
        requestDTO.setPassword("password123");
        requestDTO.setRole("ADMIN");

        when(mockUserRepository.findByUsername(requestDTO.getUsername())).thenReturn(Optional.of(new User()));

        // Act & Assert
        DuplicateException exception = assertThrows(DuplicateException.class, () -> {
            mockUserService.register(requestDTO);
        });

        assertEquals("The username is already taken. Please choose a different one.", exception.getMessage());
        verify(mockUserRepository, times(1)).findByUsername(requestDTO.getUsername());
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    void testRegister_EmailAlreadyExists_ThrowsDuplicateException() {
        // Arrange
        RegisterRequestDTO requestDTO = new RegisterRequestDTO();
        requestDTO.setUsername("newUser");
        requestDTO.setEmail("existing@example.com");
        requestDTO.setPassword("password123");
        requestDTO.setRole("ADMIN");

        when(mockUserRepository.findByUsername(requestDTO.getUsername())).thenReturn(Optional.empty());
        when(mockUserRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        DuplicateException exception = assertThrows(DuplicateException.class, () -> {
            mockUserService.register(requestDTO);
        });

        assertEquals("The email is already taken. Please choose a different one.", exception.getMessage());
        verify(mockUserRepository, times(1)).findByUsername(requestDTO.getUsername());
        verify(mockUserRepository, times(1)).findByEmail(requestDTO.getEmail());
    }

        @Test
    void testRegister_InvalidRole_ThrowsInvalidInputException() {
        // Arrange
        RegisterRequestDTO requestDTO = new RegisterRequestDTO();
        requestDTO.setUsername("newUser");
        requestDTO.setEmail("newuser@example.com");
        requestDTO.setPassword("password123");
        requestDTO.setRole("INVALID_ROLE");

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            mockUserService.register(requestDTO);
        });

        assertEquals("Invalid role : INVALID_ROLE should be like ADMIN or REQULAR_USER", exception.getMessage());
        verifyNoInteractions(mockUserRepository);
    }

    @Test
    void testRegister_NullRole_ThrowsIllegalArgumentException() {
        // Arrange
        RegisterRequestDTO requestDTO = new RegisterRequestDTO();
        requestDTO.setUsername("newUser");
        requestDTO.setEmail("newuser@example.com");
        requestDTO.setPassword("password123");
        requestDTO.setRole(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mockUserService.register(requestDTO);
        });

        assertEquals("Role must not be null", exception.getMessage());
        verifyNoInteractions(mockUserRepository);
    }

        @Test
    void testLogin_Success() {
        // Arrange
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setUsername("validUser");
        requestDTO.setPassword("validPassword");

        User user = new User();
        user.setUsername("validUser");
        user.setPassword("encodedPassword");

        String expectedToken = "jwt-token";

        when(mockUserRepository.findByUsername(requestDTO.getUsername())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(user)).thenReturn(expectedToken);

        // Act
        AuthResponseDTO response = mockUserService.login(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());

        verify(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword())
        );
        verify(mockUserRepository, times(1)).findByUsername(requestDTO.getUsername());
        verify(jwtTokenProvider, times(1)).generateToken(user);
    }


    @Test
    void testLogin_UserNotFound_ThrowsNotFoundException() {
        // Arrange
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setUsername("invalidUser");
        requestDTO.setPassword("anyPassword");

        when(mockUserRepository.findByUsername(requestDTO.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            mockUserService.login(requestDTO);
        });

        assertEquals("The username is not found.", exception.getMessage());
        verify(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword())
        );
        verify(mockUserRepository, times(1)).findByUsername(requestDTO.getUsername());
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void testGetByUsername_Success() {
        // Arrange
        String username = "validUser";
        User user = new User();
        user.setUsername(username);

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        User result = mockUserService.getByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(mockUserRepository).findByUsername(username);
    }

    @Test
    void testGetByUsername_UserNotFound_ThrowsNotFoundException() {
        // Arrange
        String username = "nonExistentUser";

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            mockUserService.getByUsername(username);
        });

        assertEquals("User with username not found", exception.getMessage());
        verify(mockUserRepository).findByUsername(username);
    }

    @Test
    void testGetAllUser_Success() {
        // Arrange
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        List<User> users = Arrays.asList(user1, user2);

        when(mockUserRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = mockUserService.getAllUser();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
        verify(mockUserRepository).findAll();
    }


}
