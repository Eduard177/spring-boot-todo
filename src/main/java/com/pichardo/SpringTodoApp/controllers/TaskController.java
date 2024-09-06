package com.pichardo.SpringTodoApp.controllers;

import com.pichardo.SpringTodoApp.dto.NewTaskDto;
import com.pichardo.SpringTodoApp.models.Task;
import com.pichardo.SpringTodoApp.services.TaskService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getUserTasks() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = taskService.getUserIdFromUsername(username);
        return taskService.getUserTasks(userId);
    }

    @PostMapping
    public Task addTask(@RequestBody NewTaskDto task) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = taskService.getUserIdFromUsername(username);
        return taskService.addTask(userId, task);
    }

    @PutMapping("/{taskId}/resolve")
    public void resolveTask(@PathVariable Long taskId) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        taskService.resolveTask(taskId, username);
    }


    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }
}
