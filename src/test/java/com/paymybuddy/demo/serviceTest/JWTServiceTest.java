package com.paymybuddy.demo.serviceTest;

import com.paymybuddy.demo.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JWTServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateToken_ShouldReturnToken() {
        // Mock des autorités et des comportements
        when(authentication.getName()).thenReturn("user@example.com");
        when(authentication.getAuthorities()).thenAnswer(invocation -> List.of(new SimpleGrantedAuthority("ROLE_USER")));



        JwtEncoderParameters capturedParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),
                JwtClaimsSet.builder()
                        .issuer("paymybuddy")
                        .subject("user@example.com")
                        .claim("roles", "ROLE_USER")
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plusSeconds(86400))
                        .build()
        );

        Jwt jwtMock = mock(Jwt.class);
        when(jwtMock.getTokenValue()).thenReturn("mockedToken");

        when(jwtEncoder.encode(any())).thenReturn(jwtMock);

        // Act
        String token = jwtService.generateToken(authentication);

        // Assert
        assertEquals("mockedToken", token);
    }

    @Test
    void validateToken_ShouldReturnTrue_WhenTokenIsValid() {
        Jwt jwtMock = mock(Jwt.class);
        when(jwtMock.getExpiresAt()).thenReturn(Instant.now().plusSeconds(3600));

        when(jwtDecoder.decode("validToken")).thenReturn(jwtMock);

        boolean isValid = jwtService.validateToken("validToken");

        assertTrue(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalse_WhenTokenIsExpired() {
        Jwt jwtMock = mock(Jwt.class);
        when(jwtMock.getExpiresAt()).thenReturn(Instant.now().minusSeconds(3600));

        when(jwtDecoder.decode("expiredToken")).thenReturn(jwtMock);

        boolean isValid = jwtService.validateToken("expiredToken");

        assertFalse(isValid);
    }

    @Test
    void extractUsername_ShouldReturnUsername() {
        Jwt jwtMock = mock(Jwt.class);
        when(jwtMock.getSubject()).thenReturn("user@example.com");

        when(jwtDecoder.decode("validToken")).thenReturn(jwtMock);

        String username = jwtService.extractUsername("validToken");

        assertEquals("user@example.com", username);
    }

    @Test
    void extractRoles_ShouldReturnRoles() {
        Jwt jwtMock = mock(Jwt.class);
        when(jwtMock.getClaim("roles")).thenReturn("ROLE_USER,ROLE_ADMIN");

        when(jwtDecoder.decode("validToken")).thenReturn(jwtMock);

        List<String> roles = jwtService.extractRoles("validToken");

        assertEquals(List.of("ROLE_USER", "ROLE_ADMIN"), roles);
    }

    @Test
    void extractRoles_ShouldReturnEmptyList_WhenRolesNull() {
        Jwt jwtMock = mock(Jwt.class);
        when(jwtMock.getClaim("roles")).thenReturn(null);

        when(jwtDecoder.decode("validToken")).thenReturn(jwtMock);

        List<String> roles = jwtService.extractRoles("validToken");

        assertTrue(roles.isEmpty());
    }
}
