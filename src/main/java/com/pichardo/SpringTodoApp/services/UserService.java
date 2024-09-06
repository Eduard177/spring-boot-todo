package com.pichardo.SpringTodoApp.services;

import com.pichardo.SpringTodoApp.models.User;
import com.pichardo.SpringTodoApp.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService{
    private final UserRepository userRepo;
    private TaskService taskService;

    public UserService(UserRepository userRepo, TaskService taskService) {
        this.userRepo = userRepo;
        this.taskService = taskService;
    }

    public List<User> getUsers() throws AccessDeniedException {
        if(taskService.isLogged()){
            List<User> allUsers = new ArrayList<>();

            userRepo.findAll().forEach(allUsers::add);

            return allUsers;
        }
        throw new AccessDeniedException("You are not authenticated");

    }

    public User getUser(Long id){
        Optional<User> res = userRepo.findById(id);
        return res.orElse(null);
    }

    public ResponseEntity<?> deleteUser(Long id) throws AccessDeniedException {
        if(taskService.isLogged()) {
            userRepo.delete(getUser(id));
            return ResponseEntity.ok("User was successfully deleted");
        }
        throw new AccessDeniedException("You are not authenticated");

    }
}
