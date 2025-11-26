package pe.edu.upeu.turismospringboot.service.impl.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upeu.turismospringboot.model.dto.auth.AuthResponse;
import pe.edu.upeu.turismospringboot.model.dto.auth.LoginRequest;
import pe.edu.upeu.turismospringboot.model.dto.auth.RegisterRequest;
import pe.edu.upeu.turismospringboot.model.entity.Persona;
import pe.edu.upeu.turismospringboot.model.entity.Rol;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.model.enums.EstadoCuenta;
import pe.edu.upeu.turismospringboot.repository.PersonaRepository;
import pe.edu.upeu.turismospringboot.repository.RolRepository;
import pe.edu.upeu.turismospringboot.repository.UsuarioRepository;
import pe.edu.upeu.turismospringboot.service.auth.AuthService;
import pe.edu.upeu.turismospringboot.service.auth.JwtService;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private pe.edu.upeu.turismospringboot.service.auth.JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private RolRepository rolRepository;

    private Usuario usuario;
    private Persona persona;
    private Rol rol;

    @BeforeEach
    void setUp() {
        // Set up mock data for testing

        persona = new Persona();
        persona.setNombres("Juan");
        persona.setApellidos("Perez");
        persona.setTipoDocumento("DNI");
        persona.setNumeroDocumento("12345678");
        persona.setTelefono("123456789");
        persona.setDireccion("Calle Ficticia 123");
        persona.setCorreoElectronico("juan.perez@email.com");

        rol = new Rol();
        rol.setNombre("ROLE_USUARIO");

        usuario = new Usuario();
        usuario.setUsername("juanperez");
        usuario.setPassword("password");
        usuario.setEstado(EstadoCuenta.ACTIVO);
        usuario.setRol(rol);
        usuario.setPersona(persona);
    }

    @Test
    @DisplayName("Debe registrar un nuevo usuario correctamente")
    void testRegister() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("juanperez");
        registerRequest.setPassword("password");
        registerRequest.setNombres("Juan");
        registerRequest.setApellidos("Perez");
        registerRequest.setTipoDocumento("DNI");
        registerRequest.setNumeroDocumento("12345678");
        registerRequest.setTelefono("123456789");
        registerRequest.setDireccion("Calle Ficticia 123");
        registerRequest.setCorreoElectronico("juan.perez@email.com");
        registerRequest.setFechaNacimiento(LocalDate.of(1990, 5, 15));

        when(rolRepository.findByNombre("ROLE_USUARIO")).thenReturn(java.util.Optional.of(rol));
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtService.getToken(any(UserDetails.class))).thenReturn("mock-token");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mock-token", response.getToken());
        verify(rolRepository).findByNombre("ROLE_USUARIO");
        verify(personaRepository).save(any(Persona.class));
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe iniciar sesión correctamente")
    void testLogin() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("juanperez");
        loginRequest.setPassword("password");

        when(usuarioRepository.findByUsername("juanperez")).thenReturn(java.util.Optional.of(usuario));
        when(jwtService.getToken(any(UserDetails.class))).thenReturn("mock-token");

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mock-token", response.getToken());
        verify(usuarioRepository).findByUsername("juanperez");
    }

    @Test
    @DisplayName("Debe lanzar excepción si el rol no se encuentra al registrar")
    void testRegister_ThrowsException_WhenRoleNotFound() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("juanperez");
        registerRequest.setPassword("password");

        when(rolRepository.findByNombre("ROLE_USUARIO")).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
    }

    @Test
    @DisplayName("Debe lanzar excepción si las credenciales son incorrectas al iniciar sesión")
    void testLogin_ThrowsException_WhenInvalidCredentials() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("juanperez");
        loginRequest.setPassword("wrongpassword");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }


}
