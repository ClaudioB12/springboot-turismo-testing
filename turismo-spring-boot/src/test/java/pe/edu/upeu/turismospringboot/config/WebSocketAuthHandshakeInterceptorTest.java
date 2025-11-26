package pe.edu.upeu.turismospringboot.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.WebSocketHandler;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.repository.UsuarioRepository;
import pe.edu.upeu.turismospringboot.service.auth.JwtService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebSocketAuthHandshakeInterceptorTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private WebSocketHandler webSocketHandler;

    @Mock
    private HttpServletRequest httpServletRequest;

    private WebSocketAuthHandshakeInterceptor interceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interceptor = new WebSocketAuthHandshakeInterceptor();

        // INYECTAR DEPENDENCIAS SIN MODIFICAR TU CÓDIGO FUENTE
        ReflectionTestUtils.setField(interceptor, "jwtService", jwtService);
        ReflectionTestUtils.setField(interceptor, "usuarioRepository", usuarioRepository);
    }

    @Test
    void testBeforeHandshake_TokenValidoYUsuarioExiste() {
        String token = "validToken";

        // Mock del servlet request
        when(httpServletRequest.getParameter("token")).thenReturn(token);

        // Mock de JWT
        when(jwtService.isTokenValid(token)).thenReturn(true);
        when(jwtService.getUsernameFromToken(token)).thenReturn("bustinza");

        // Mock de usuario encontrado
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(99L);
        usuario.setUsername("bustinza");

        when(usuarioRepository.findByUsername("bustinza"))
                .thenReturn(Optional.of(usuario));

        ServletServerHttpRequest request =
                new ServletServerHttpRequest(httpServletRequest);

        HashMap<String, Object> attributes = new HashMap<>();

        boolean result = interceptor.beforeHandshake(
                request, null, webSocketHandler, attributes);

        assertTrue(result);
        assertEquals(99L, attributes.get("usuarioId"));
    }

    @Test
    void testBeforeHandshake_TokenValidoPeroUsuarioNoExiste() {
        when(httpServletRequest.getParameter("token")).thenReturn("valid");
        when(jwtService.isTokenValid("valid")).thenReturn(true);
        when(jwtService.getUsernameFromToken("valid")).thenReturn("noExiste");

        when(usuarioRepository.findByUsername("noExiste"))
                .thenReturn(Optional.empty());

        ServletServerHttpRequest request =
                new ServletServerHttpRequest(httpServletRequest);

        boolean result = interceptor.beforeHandshake(
                request, null, webSocketHandler, new HashMap<>());

        assertFalse(result); // Usuario no encontrado → handshake bloqueado
    }

    @Test
    void testBeforeHandshake_TokenInvalido() {
        when(httpServletRequest.getParameter("token")).thenReturn("bad");
        when(jwtService.isTokenValid("bad")).thenReturn(false);

        ServletServerHttpRequest request =
                new ServletServerHttpRequest(httpServletRequest);

        boolean result = interceptor.beforeHandshake(
                request, null, webSocketHandler, new HashMap<>());

        assertFalse(result);
    }

    @Test
    void testBeforeHandshake_SinToken() {
        when(httpServletRequest.getParameter("token")).thenReturn(null);

        ServletServerHttpRequest request =
                new ServletServerHttpRequest(httpServletRequest);

        boolean result = interceptor.beforeHandshake(
                request, null, webSocketHandler, new HashMap<>());

        assertFalse(result);

        verify(jwtService, never()).isTokenValid(any());
    }
}
