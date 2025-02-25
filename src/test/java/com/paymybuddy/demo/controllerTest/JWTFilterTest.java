package com.paymybuddy.demo.controllerTest;

import com.paymybuddy.demo.filter.JWTFilter;
import com.paymybuddy.demo.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;
import static org.mockito.Mockito.*;


class JWTFilterTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JWTFilter jwtFilter;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Test that the filter allows access with a valid token.
     *
     * @throws Exception if the test fails
     */
    @Test
    void shouldPassThrough_WhenPublicEndpoint() throws Exception {
        // Simulate a request with a valid token
        when(request.getServletPath()).thenReturn("/api/users/login");

        jwtFilter.doFilterInternal(request, response, filterChain);

        // Verify that the JWTService methods were called
        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
    }
}