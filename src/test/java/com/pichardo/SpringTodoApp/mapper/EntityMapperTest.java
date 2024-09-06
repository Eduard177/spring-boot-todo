package com.pichardo.SpringTodoApp.mapper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import com.pichardo.SpringTodoApp.dto.NewTaskDto;
import com.pichardo.SpringTodoApp.dto.NewUserDto;
import com.pichardo.SpringTodoApp.models.Task;
import com.pichardo.SpringTodoApp.models.User;
import com.pichardo.SpringTodoApp.mapper.EntityMapper;

public class EntityMapperTest {

    private EntityMapper entityMapper;

    @BeforeEach
    public void setUp() {
        entityMapper = new EntityMapper();
    }

    @Test
    public void testNewUserDtoToUser() {

        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setUsername("testUser");
        newUserDto.setPassword("testPassword");

        User user = entityMapper.newUserDtoToUser(newUserDto);

        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void testNewTaskDtoToTask() {
        NewTaskDto newTaskDto = new NewTaskDto();
        newTaskDto.setName("Test Task");
        newTaskDto.setDescription("This is a test task");

        Task task = entityMapper.newTaskDtooToTask(newTaskDto);

        assertNotNull(task);
        assertEquals("Test Task", task.getName());
        assertEquals("This is a test task", task.getDescription());
    }
}
