package com.pichardo.SpringTodoApp.util;

import com.pichardo.SpringTodoApp.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @Mock
    private UserDetails userDetails;

    private String token;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil();

        userDetails = new User("testUser", "password", Collections.emptyList());

        token = jwtUtil.generateToken(userDetails);
    }

    @Test
    void testExtractUsername() {
        String username = jwtUtil.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    void testExtractExpiration() {
        Date expiration = jwtUtil.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testIsTokenExpired_NotExpired() {
        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void testGenerateToken() {
        String generatedToken = jwtUtil.generateToken(userDetails);

        assertNotNull(generatedToken);
        assertNotEquals("", generatedToken);
    }

    @Test
    void testValidateToken_ValidToken() {
        boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        userDetails = new User("testing", "password", Collections.emptyList());
        boolean isValid = jwtUtil.validateToken(token , userDetails);

        assertFalse(isValid);
    }
}
