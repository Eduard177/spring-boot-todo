package com.pichardo.SpringTodoApp.service;

import com.pichardo.SpringTodoApp.dto.NewTaskDto;
import com.pichardo.SpringTodoApp.mapper.EntityMapper;
import com.pichardo.SpringTodoApp.models.Task;
import com.pichardo.SpringTodoApp.models.User;
import com.pichardo.SpringTodoApp.repositories.TaskRepository;
import com.pichardo.SpringTodoApp.repositories.UserRepository;
import com.pichardo.SpringTodoApp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void getUserTasks_UserAuthenticated_ReturnsTasks() throws AccessDeniedException {
        when(authentication.getName()).thenReturn("user");
        when(authentication.getAuthorities()).thenReturn(Arrays.asList());

        Long userId = 1L;
        Task task1 = new Task();
        Task task2 = new Task();
        when(taskRepository.findByUserId(userId)).thenReturn(Arrays.asList(task1, task2));

        var tasks = taskService.getUserTasks(userId);

        assertEquals(2, tasks.size());
        verify(taskRepository).findByUserId(userId);
    }

    @Test
    void getUserTasks_UserNotAuthenticated_ThrowsAccessDeniedException() {
        when(authentication.getName()).thenReturn("anonymousUser");

        Long userId = 1L;

        assertThrows(AccessDeniedException.class, () -> taskService.getUserTasks(userId));
    }

    @Test
    void addTask_UserAuthenticated_SuccessfullyAddsTask() throws AccessDeniedException {
        when(authentication.getName()).thenReturn("user");
        when(authentication.getAuthorities()).thenReturn(Arrays.asList());

        Long userId = 1L;
        NewTaskDto taskDto = new NewTaskDto();
        Task task = new Task();
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(entityMapper.newTaskDtooToTask(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.addTask(userId, taskDto);

        assertEquals(task, result);
        verify(taskRepository).save(task);
        verify(userRepository).findById(userId);
    }

    @Test
    void addTask_UserNotAuthenticated_ThrowsAccessDeniedException() {
        when(authentication.getName()).thenReturn("anonymousUser");

        Long userId = 1L;
        NewTaskDto taskDto = new NewTaskDto();

        assertThrows(AccessDeniedException.class, () -> taskService.addTask(userId, taskDto));
    }

    @Test
    void resolveTask_UserOwnsTask_SuccessfullyResolvesTask() throws Exception {
        Long taskId = 1L;
        String username = "user";
        Task task = new Task();
        User user = new User();
        user.setUsername(username);
        task.setUser(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.resolveTask(taskId, username);

        assertTrue(task.isResolved());
        verify(taskRepository).save(task);
    }

    @Test
    void resolveTask_UserDoesNotOwnTask_ThrowsAccessDeniedException() {
        Long taskId = 1L;
        String username = "user";
        Task task = new Task();
        User anotherUser = new User();
        anotherUser.setUsername("anotherUser");
        task.setUser(anotherUser);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.resolveTask(taskId, username));
    }

    @Test
    public void testGetUserIdFromUsername_UserExists() {
        String username = "testUser";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Long result = taskService.getUserIdFromUsername(username);

        assertEquals(1L, result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testGetUserIdFromUsername_UserDoesNotExist() {
        String username = "nonexistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Error exception = assertThrows(Error.class, () -> {
            taskService.getUserIdFromUsername(username);
        });
        assertEquals("We don't found a user with that username", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }
}
