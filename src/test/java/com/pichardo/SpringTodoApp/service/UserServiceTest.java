package com.pichardo.SpringTodoApp.service;

import com.pichardo.SpringTodoApp.models.User;
import com.pichardo.SpringTodoApp.repositories.UserRepository;
import com.pichardo.SpringTodoApp.services.TaskService;
import com.pichardo.SpringTodoApp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsers_whenLogged_shouldReturnUsers() throws AccessDeniedException {
        when(taskService.isLogged()).thenReturn(true);

        User user1 = new User();
        User user2 = new User();
        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> users = userService.getUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUsers_whenNotLogged_shouldThrowAccessDeniedException() {
        when(taskService.isLogged()).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> userService.getUsers());
    }

    @Test
    void getUser_whenUserExists_shouldReturnUser() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUser(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUser_whenUserDoesNotExist_shouldReturnNull() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User foundUser = userService.getUser(1L);

        assertNull(foundUser);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void deleteUser_whenLogged_shouldDeleteUser() throws AccessDeniedException {
        when(taskService.isLogged()).thenReturn(true);

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<?> call = userService.deleteUser(1L);

        assertEquals(call.getBody(),"User was successfully deleted");
    }

    @Test
    void deleteUser_whenNotLogged_shouldThrowAccessDeniedException() {
        when(taskService.isLogged()).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> userService.deleteUser(1L));
    }
}
