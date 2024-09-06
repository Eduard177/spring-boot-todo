package com.pichardo.SpringTodoApp.controllers;

import com.pichardo.SpringTodoApp.models.User;
import com.pichardo.SpringTodoApp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public List<User> getUsers() throws AccessDeniedException {
        return userService.getUsers();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) throws AccessDeniedException {
        return userService.deleteUser(userId);
    }
}
