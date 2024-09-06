package com.pichardo.SpringTodoApp.controllers;

import com.pichardo.SpringTodoApp.dto.NewTaskDto;
import com.pichardo.SpringTodoApp.models.Task;
import com.pichardo.SpringTodoApp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void testGetUserTasks_Success() throws Exception {
        // Simulamos que el usuario autenticado es "testUser"
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        given(taskService.getUserIdFromUsername("testUser")).willReturn(1L);

        // Simulamos una lista de tareas
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("Test Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setName("Test Task 2");

        List<Task> tasks = Arrays.asList(task1, task2);
        given(taskService.getUserTasks(1L)).willReturn(tasks);

        mockMvc.perform(get("/task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Task 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Test Task 2"));

        verify(taskService, times(1)).getUserIdFromUsername("testUser");
        verify(taskService, times(1)).getUserTasks(1L);
    }

    @Test
    public void testAddTask_Success() throws Exception {
        // Simulamos que el usuario autenticado es "testUser"
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        given(taskService.getUserIdFromUsername("testUser")).willReturn(1L);

        NewTaskDto newTaskDto = new NewTaskDto();
        newTaskDto.setName("New Task");
        newTaskDto.setDescription("New Task Description");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setName("New Task");

        given(taskService.addTask(eq(1L), any(NewTaskDto.class))).willReturn(savedTask);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Task\",\"description\":\"New Task Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Task"));

        verify(taskService, times(1)).getUserIdFromUsername("testUser");
        verify(taskService, times(1)).addTask(eq(1L), any(NewTaskDto.class));
    }

    @Test
    public void testResolveTask_Success() throws Exception {
        // Simulamos que el usuario autenticado es "testUser"
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        doNothing().when(taskService).resolveTask(1L, "testUser");

        mockMvc.perform(put("/task/1/resolve"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).resolveTask(1L, "testUser");
    }

    @Test
    public void testDeleteTask_Success() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/task/1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask(1L);
    }

}
