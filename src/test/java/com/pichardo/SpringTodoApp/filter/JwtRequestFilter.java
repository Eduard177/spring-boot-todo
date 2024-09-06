package com.pichardo.SpringTodoApp.filter;

import com.pichardo.SpringTodoApp.filters.JwtRequestFilter;
import com.pichardo.SpringTodoApp.services.UserDetailsServiceImpl;
import com.pichardo.SpringTodoApp.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

class JwtRequestFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-jwt-token");

        MockHttpServletResponse response = new MockHttpServletResponse();

        UserDetails userDetails = new User("testUser", "password", Collections.emptyList());

        when(jwtUtil.extractUsername("valid-jwt-token")).thenReturn("testUser");
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtUtil.validateToken("valid-jwt-token", userDetails)).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        verify(jwtUtil).extractUsername("valid-jwt-token");
        verify(jwtUtil).validateToken("valid-jwt-token", userDetails);
        verify(userDetailsService).loadUserByUsername("testUser");

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-jwt-token");

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.extractUsername("invalid-jwt-token")).thenReturn("testUser");
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(null);
        when(jwtUtil.validateToken("invalid-jwt-token", null)).thenReturn(false);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil).extractUsername("invalid-jwt-token");
        verify(jwtUtil, never()).validateToken(anyString(), any(UserDetails.class));


        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil, never()).extractUsername(anyString());
        verify(jwtUtil, never()).validateToken(anyString(), any(UserDetails.class));
        verify(userDetailsService, never()).loadUserByUsername(anyString());

        verify(filterChain).doFilter(request, response);
    }
}
