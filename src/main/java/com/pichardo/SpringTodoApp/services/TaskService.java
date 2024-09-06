package com.pichardo.SpringTodoApp.services;

import com.pichardo.SpringTodoApp.dto.NewTaskDto;
import com.pichardo.SpringTodoApp.mapper.EntityMapper;
import com.pichardo.SpringTodoApp.models.Task;
import com.pichardo.SpringTodoApp.models.User;
import com.pichardo.SpringTodoApp.repositories.TaskRepository;
import com.pichardo.SpringTodoApp.repositories.UserRepository;
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

    public List<Task> getUserTasks(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task addTask(Long userId, NewTaskDto taskDto) {
        Optional<User> user = userRepository.findById(userId);
        Task task = entityMapper.newTaskDtooToTask(taskDto);
        user.ifPresent(task::setUser);
        return taskRepository.save(task);
    }

    public void resolveTask(Long taskId, String username) throws Exception {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            if (task.getUser().getUsername().equals(username)) {
                task.setResolved(true);
                taskRepository.save(task);
            } else {
                throw new AccessDeniedException("No tienes permiso para actualizar esta tarea.");
            }
        } else {
            throw new Exception("Tarea no encontrada con ID: " + taskId);
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
}
