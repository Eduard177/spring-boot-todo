package com.pichardo.SpringTodoApp.controllers;

import com.pichardo.SpringTodoApp.models.User;
import com.pichardo.SpringTodoApp.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }




}
