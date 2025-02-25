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
    /**
     * Set up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Test that generating a token returns the correct token.
     */
    @Test
    void generateToken_ShouldReturnToken() {
        // Arrange: Mock authorities and behaviors
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

        // Act: Generate the token
        String token = jwtService.generateToken(authentication);

        // Assert: Verify the token is as expected
        assertEquals("mockedToken", token);
    }
    /**
     * Test that validating a token returns true when the token is valid.
     */
    @Test
    void validateToken_ShouldReturnTrue_WhenTokenIsValid() {
        // Arrange: Mock JWT behavior
        Jwt jwtMock = mock(Jwt.class);
        when(jwtMock.getExpiresAt()).thenReturn(Instant.now().plusSeconds(3600));

        when(jwtDecoder.decode("validToken")).thenReturn(jwtMock);
        // Act: Validate the token
        boolean isValid = jwtService.validateToken("validToken");
        // Assert: Verify the token is valid
        assertTrue(isValid);
    }
    /**
     * Test that validating a token returns false when the token is expired.
     */
    @Test
    void validateToken_ShouldReturnFalse_WhenTokenIsExpired() {
        // Arrange: Mock JWT behavior
        Jwt jwtMock = mock(Jwt.class);
        when(jwtMock.getExpiresAt()).thenReturn(Instant.now().minusSeconds(3600));

        when(jwtDecoder.decode("expiredToken")).thenReturn(jwtMock);

       // Act: Validate the token
        boolean isValid = jwtService.validateToken("expiredToken");
         // Assert: Verify the token is expired
        assertFalse(isValid);
    }
    /**
     * Test that extracting the username from a token returns the correct username.
     */
    @Test
    void extractUsername_ShouldReturnUsername() {
        // Arrange: Mock JWT behavior
        Jwt jwtMock = mock(Jwt.class);
        when(jwtMock.getSubject()).thenReturn("user@example.com");

        when(jwtDecoder.decode("validToken")).thenReturn(jwtMock);

        String username = jwtService.extractUsername("validToken");
        // Assert: Verify the username is correct
        assertEquals("user@example.com", username);
    }
    /**
     * Test that extracting roles from a token returns the correct roles.
     */
    @Test
    void extractRoles_ShouldReturnRoles() {
        // Arrange: Mock JWT behavior
        Jwt jwtMock = mock(Jwt.class);
        when(jwtMock.getClaim("roles")).thenReturn("ROLE_USER,ROLE_ADMIN");

        when(jwtDecoder.decode("validToken")).thenReturn(jwtMock);
         // Act: Extract the roles
        List<String> roles = jwtService.extractRoles("validToken");

        // Assert: Verify the roles are correct
        assertEquals(List.of("ROLE_USER", "ROLE_ADMIN"), roles);
    }
    /**
     * Test that extracting roles from a token returns an empty list when roles are null.
     */
    @Test
    void extractRoles_ShouldReturnEmptyList_WhenRolesNull() {
        // Arrange: Mock JWT behavior
        Jwt jwtMock = mock(Jwt.class);
        when(jwtMock.getClaim("roles")).thenReturn(null);

        when(jwtDecoder.decode("validToken")).thenReturn(jwtMock);

        // Act: Extract the roles
        List<String> roles = jwtService.extractRoles("validToken");

        // Assert: Verify the roles list is empty
        assertTrue(roles.isEmpty());
    }



}
