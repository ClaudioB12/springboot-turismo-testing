package pe.edu.upeu.turismospringboot.controller.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upeu.turismospringboot.model.dto.auth.AuthResponse;
import pe.edu.upeu.turismospringboot.model.dto.auth.LoginRequest;
import pe.edu.upeu.turismospringboot.model.dto.auth.RegisterRequest;
import pe.edu.upeu.turismospringboot.service.auth.AuthService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --------------------------------------------------------
    // LOGIN
    // --------------------------------------------------------
    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("test@test.com");
        request.setPassword("123456");

        AuthResponse responseMock = new AuthResponse();
        responseMock.setToken("fake-jwt-token");

        when(authService.login(request)).thenReturn(responseMock);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMock, response.getBody());
    }

    // --------------------------------------------------------
    // REGISTER
    // --------------------------------------------------------
    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setNombres("Juan");
        request.setApellidos("Perez");
        request.setUsername("juan@test.com");
        request.setPassword("123456");

        AuthResponse responseMock = new AuthResponse();
        responseMock.setToken("new-token");

        when(authService.register(request)).thenReturn(responseMock);

        ResponseEntity<AuthResponse> response = authController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMock, response.getBody());
    }
}
