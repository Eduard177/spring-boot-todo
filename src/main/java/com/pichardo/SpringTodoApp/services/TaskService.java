package com.pichardo.SpringTodoApp.services;

import com.pichardo.SpringTodoApp.dto.NewTaskDto;
import com.pichardo.SpringTodoApp.mapper.EntityMapper;
import com.pichardo.SpringTodoApp.models.Task;
import com.pichardo.SpringTodoApp.models.User;
import com.pichardo.SpringTodoApp.repositories.TaskRepository;
import com.pichardo.SpringTodoApp.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, EntityMapper entityMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.entityMapper = entityMapper;
    }

    public List<Task> getUserTasks(Long userId) throws AccessDeniedException {
        if(isLogged()){
            return taskRepository.findByUserId(userId);
        }
        throw new AccessDeniedException("You are not authenticated");

    }

    public Task addTask(Long userId, NewTaskDto taskDto) throws AccessDeniedException {
        if(isLogged()){
            Optional<User> user = userRepository.findById(userId);
            Task task = entityMapper.newTaskDtooToTask(taskDto);
            user.ifPresent(task::setUser);
            return taskRepository.save(task);
        }else {
            throw new AccessDeniedException("You are not authenticated");

        }

    }

    public void resolveTask(Long taskId, String username) throws Exception {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            if (task.getUser().getUsername().equals(username)) {
                task.setResolved(true);
                taskRepository.save(task);
            } else {
                throw new AccessDeniedException("You are not authenticated");
            }
        } else {
            throw new Exception("Task not found " + taskId);
        }
    }


    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public Long getUserIdFromUsername(String username)throws Error {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return user.get().getId();
        }else {
            throw new Error("We don't found a user with that username");
        }
    }

    public Boolean isLogged(){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        return !user.getName().equals("anonymousUser") && user.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ANONYMOUS"));
    }
}
