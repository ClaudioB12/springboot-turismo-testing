package pe.edu.upeu.turismospringboot.service.impl.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.model.enums.EstadoCuenta;
import pe.edu.upeu.turismospringboot.repository.UsuarioRepository;
import pe.edu.upeu.turismospringboot.service.auth.UsuarioDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioDetailsServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioDetailsService usuarioDetailsService;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        usuarioDetailsService = new UsuarioDetailsService(usuarioRepository);
    }

    @Test
    @DisplayName("Debe retornar UserDetails cuando el usuario existe")
    void testLoadUserByUsername_UsuarioExiste() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setUsername("carlos");
        usuario.setPassword("1234");
        usuario.setEstado(EstadoCuenta.ACTIVO);

        when(usuarioRepository.findByUsername("carlos"))
                .thenReturn(Optional.of(usuario));

        // Act
        var userDetails = usuarioDetailsService.loadUserByUsername("carlos");

        // Assert
        assertNotNull(userDetails);
        assertEquals("carlos", userDetails.getUsername());
        verify(usuarioRepository, times(1)).findByUsername("carlos");
    }

    @Test
    @DisplayName("Debe lanzar UsernameNotFoundException cuando el usuario no existe")
    void testLoadUserByUsername_NoExiste() {
        // Arrange
        when(usuarioRepository.findByUsername("desconocido"))
                .thenReturn(Optional.empty());

        // Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername("desconocido")
        );

        verify(usuarioRepository, times(1)).findByUsername("desconocido");
    }
}
