package com.bortnik.expensetracker;

import com.bortnik.expensetracker.exceptions.auth.InvalidJwtToken;
import com.bortnik.expensetracker.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtTokenProviderTests {

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    private final String username = "testuser";
    private final String invalidToken = "invalidToken";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtToken",
                "bXlTZWNyZXRLZXlGb3JKd3RUb2tlbkdlbmVyYXRpb25BbmRWYWxpZGF0aW9uMTIzNDU2Nzg5MA==");
        long expiration = 3600000L;
        ReflectionTestUtils.setField(jwtTokenProvider, "expiration", expiration);
        jwtTokenProvider.init();
    }

    @Test
    void generateToken_ShouldGenerateValidToken() {
        final String token = jwtTokenProvider.generateToken(username);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void getUsernameFromToken_ShouldReturnUsername() {
        final String token = jwtTokenProvider.generateToken(username);

        final String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void validateToken_ShouldPassForValidToken() {
        final String token = jwtTokenProvider.generateToken(username);

        assertDoesNotThrow(() -> jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_ShouldThrowExceptionForInvalidToken() {
        final var exception = assertThrows(InvalidJwtToken.class,
                () -> jwtTokenProvider.validateToken(invalidToken));

        assertEquals("Expired or invalid JWT token", exception.getMessage());
    }

    @Test
    void validateToken_ShouldThrowExceptionForNullToken() {
        final var exception = assertThrows(InvalidJwtToken.class,
                () -> jwtTokenProvider.validateToken(null));

        assertEquals("Expired or invalid JWT token", exception.getMessage());
    }

    @Test
    void validateToken_ShouldThrowExceptionForEmptyToken() {
        final var exception = assertThrows(InvalidJwtToken.class,
                () -> jwtTokenProvider.validateToken(""));

        assertEquals("Expired or invalid JWT token", exception.getMessage());
    }

    @Test
    void resolveToken_ShouldReturnTokenFromBearerHeader() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        String validToken = "Bearer validToken";
        when(request.getHeader("Authorization")).thenReturn(validToken);

        final Optional<String> resolvedToken = jwtTokenProvider.resolveToken(request);

        assertTrue(resolvedToken.isPresent());
        assertEquals("validToken", resolvedToken.get());
    }

    @Test
    void resolveToken_ShouldReturnEmptyWhenNoBearerPrefix() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("invalidToken");

        final Optional<String> resolvedToken = jwtTokenProvider.resolveToken(request);

        assertTrue(resolvedToken.isEmpty());
    }

    @Test
    void resolveToken_ShouldReturnEmptyWhenNoAuthorizationHeader() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        final Optional<String> resolvedToken = jwtTokenProvider.resolveToken(request);

        assertTrue(resolvedToken.isEmpty());
    }

    @Test
    void resolveToken_ShouldReturnEmptyWhenEmptyAuthorizationHeader() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("");

        final Optional<String> resolvedToken = jwtTokenProvider.resolveToken(request);

        assertTrue(resolvedToken.isEmpty());
    }

    @Test
    void resolveToken_ShouldReturnEmptyWhenOnlyBearerPrefix() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        final Optional<String> resolvedToken = jwtTokenProvider.resolveToken(request);

        assertTrue(resolvedToken.isPresent());
        assertEquals("", resolvedToken.get());
    }

    @Test
    void getUsernameFromToken_ShouldThrowExceptionForInvalidToken() {
        assertThrows(Exception.class,
                () -> jwtTokenProvider.getUsernameFromToken(invalidToken));
    }

    @Test
    void init_ShouldInitializeSecretKey() {
        final JwtTokenProvider newProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(newProvider, "jwtToken",
                "bXlTZWNyZXRLZXlGb3JKd3RUb2tlbkdlbmVyYXRpb25BbmRWYWxpZGF0aW9uMTIzNDU2Nzg5MA==");

        assertDoesNotThrow(newProvider::init);

        assertDoesNotThrow(() -> newProvider.generateToken(username));
    }
}