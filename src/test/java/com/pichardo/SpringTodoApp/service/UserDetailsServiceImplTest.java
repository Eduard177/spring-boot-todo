package com.pichardo.SpringTodoApp.service;

import com.pichardo.SpringTodoApp.dto.NewUserDto;
import com.pichardo.SpringTodoApp.mapper.EntityMapper;
import com.pichardo.SpringTodoApp.models.User;
import com.pichardo.SpringTodoApp.repositories.UserRepository;
import com.pichardo.SpringTodoApp.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Datos simulados
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");

        // Simular repositorio
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Ejecutar método
        var userDetails = userDetailsService.loadUserByUsername(username);

        // Verificar el resultado
        assertEquals(username, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Datos simulados
        String username = "nonexistentuser";

        // Simular repositorio
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
        verify(userRepository).findByUsername(username);
    }

    @Test
    void save_UserDto_EncodesPasswordAndSavesUser() {
        // Datos simulados
        NewUserDto userDto = new NewUserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("rawPassword");

        User user = new User();

        // Simular codificación de la contraseña y mapeo
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(entityMapper.newUserDtoToUser(userDto)).thenReturn(user);

        // Ejecutar el método
        userDetailsService.save(userDto);

        // Verificar que la contraseña fue codificada y el usuario fue guardado
        assertEquals("encodedPassword", userDto.getPassword());
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(user);
    }
}
