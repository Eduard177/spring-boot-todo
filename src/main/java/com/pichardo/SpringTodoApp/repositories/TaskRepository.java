package com.pichardo.SpringTodoApp.repositories;

import com.pichardo.SpringTodoApp.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
}
