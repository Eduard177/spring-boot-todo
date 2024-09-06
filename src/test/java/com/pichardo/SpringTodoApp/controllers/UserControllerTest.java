package com.pichardo.SpringTodoApp.controllers;

import com.pichardo.SpringTodoApp.models.User;
import com.pichardo.SpringTodoApp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getUsers_ReturnsListOfUsers() throws Exception {
        // Simular datos
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        // Simular comportamiento del servicio
        when(userService.getUsers()).thenReturn(users);

        // Ejecutar solicitud GET y verificar el resultado
        mockMvc.perform(get("/user/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));  // Verificar que hay 2 usuarios
    }

    @Test
    void deleteUser_DeletesUserSuccessfully() throws Exception {
        // Simular eliminación exitosa devolviendo ResponseEntity.ok()
        when(userService.deleteUser(1L)).thenReturn(ResponseEntity.ok().build());

        // Ejecutar solicitud DELETE y verificar el resultado
        mockMvc.perform(delete("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
