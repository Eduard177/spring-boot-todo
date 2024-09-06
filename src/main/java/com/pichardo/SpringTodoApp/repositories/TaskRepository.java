package com.pichardo.SpringTodoApp.repositories;

import com.pichardo.SpringTodoApp.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {        // Since it is a generic, we are passing the type (Todo) and the ID type (which is long)
    // We are just providing the interface, Spring will provide us with the implementation

    // if we want some special operations which are not there in CrudRepository, then we can include it here

    public List<Task> findByUserId(Long userId);
}
