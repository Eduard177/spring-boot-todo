package com.pichardo.SpringTodoApp.controllers;

import com.pichardo.SpringTodoApp.dto.Login;
import com.pichardo.SpringTodoApp.dto.NewUserDto;
import com.pichardo.SpringTodoApp.services.UserDetailsServiceImpl;
import com.pichardo.SpringTodoApp.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegister_Success() throws Exception {
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setUsername("testUser");
        newUserDto.setPassword("testPassword");

        doNothing().when(userDetailsService).save(any(NewUserDto.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"testPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        verify(userDetailsService, times(1)).save(any(NewUserDto.class));
    }

    @Test
    public void testLogin_Success() throws Exception {
        Login loginRequest = new Login();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        UserDetails userDetails = mock(UserDetails.class);
        given(userDetailsService.loadUserByUsername("testUser")).willReturn(userDetails);
        given(jwtUtil.generateToken(userDetails)).willReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"testPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername("testUser");
        verify(jwtUtil, times(1)).generateToken(userDetails);
    }
}
