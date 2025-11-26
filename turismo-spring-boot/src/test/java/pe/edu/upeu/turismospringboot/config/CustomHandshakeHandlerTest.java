package pe.edu.upeu.turismospringboot.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.WebSocketHandler;
import pe.edu.upeu.turismospringboot.service.auth.JwtService;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomHandshakeHandlerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private WebSocketHandler webSocketHandler;

    @Mock
    private HttpServletRequest httpServletRequest;

    private CustomHandshakeHandler handshakeHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        handshakeHandler = new CustomHandshakeHandler();

        // INYECTAR jwtService SIN MODIFICAR EL CÓDIGO ORIGINAL
        ReflectionTestUtils.setField(handshakeHandler, "jwtService", jwtService);
    }

    @Test
    void testDetermineUser_TokenValido() {
        String token = "validToken";

        when(httpServletRequest.getParameter("token")).thenReturn(token);
        when(jwtService.isTokenValid(token)).thenReturn(true);
        when(jwtService.getUsernameFromToken(token)).thenReturn("bustinza");

        ServletServerHttpRequest request =
                new ServletServerHttpRequest(httpServletRequest);

        Principal principal = handshakeHandler.determineUser(
                request, webSocketHandler, new HashMap<>());

        assertNotNull(principal);
        assertEquals("bustinza", principal.getName());
    }

    @Test
    void testDetermineUser_TokenInvalido() {
        when(httpServletRequest.getParameter("token")).thenReturn("wrong");
        when(jwtService.isTokenValid("wrong")).thenReturn(false);

        ServletServerHttpRequest request =
                new ServletServerHttpRequest(httpServletRequest);

        Principal principal = handshakeHandler.determineUser(
                request, webSocketHandler, new HashMap<>());

        assertNull(principal);
    }

    @Test
    void testDetermineUser_SinToken() {
        when(httpServletRequest.getParameter("token")).thenReturn(null);

        ServletServerHttpRequest request =
                new ServletServerHttpRequest(httpServletRequest);

        Principal principal = handshakeHandler.determineUser(
                request, webSocketHandler, new HashMap<>());

        assertNull(principal);
        verify(jwtService, never()).isTokenValid(any());
    }
}
