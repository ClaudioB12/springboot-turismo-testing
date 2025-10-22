package pe.edu.upeu.turismospringboot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.UsuarioCompletoDto;
import pe.edu.upeu.turismospringboot.model.entity.Persona;
import pe.edu.upeu.turismospringboot.model.entity.Rol;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.model.enums.EstadoCuenta;
import pe.edu.upeu.turismospringboot.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioCompletoServiceImplTest {

    @InjectMocks
    private UsuarioCompletoServiceImpl usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private EmprendimientoRepository emprendimientoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarUsuarioCompleto_retornaLista() {
        List<Usuario> usuarios = Arrays.asList(new Usuario(), new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.listarUsuarioCompleto();

        assertEquals(2, result.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarUsuarioCompletoPorId_existeUsuario_retornaUsuario() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.buscarUsuarioCompletoPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdUsuario());
    }

    @Test
    void buscarUsuarioCompletoPorId_noExisteUsuario_lanzaExcepcion() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                usuarioService.buscarUsuarioCompletoPorId(1L));

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void crearUsuarioCompleto_conRolExistente_guardaUsuario() {
        UsuarioCompletoDto dto = new UsuarioCompletoDto();
        dto.setUsername("usuario1");
        dto.setPassword("pass123");
        dto.setNombreRol("ROLE_USER");
        dto.setEstadoCuenta("ACTIVO");

        Rol rol = new Rol();
        rol.setNombre("ROLE_USER");

        when(rolRepository.findByNombre("ROLE_USER")).thenReturn(Optional.of(rol));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(personaRepository.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario result = usuarioService.crearUsuarioCompleto(dto, null);

        assertNotNull(result);
        assertEquals("usuario1", result.getUsername());
        assertEquals(EstadoCuenta.ACTIVO, result.getEstado());
        assertEquals("ROLE_USER", result.getRol().getNombre());
        verify(personaRepository, times(1)).save(any(Persona.class));
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void buscarIdUsuarioPorUsername_existeUsuario_retornaDto() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(10L);
        usuario.setUsername("usuario10");

        when(usuarioRepository.findByUsername("usuario10")).thenReturn(Optional.of(usuario));

        var dtoResponse = usuarioService.buscarIdUsuarioPorUsername("usuario10");

        assertEquals(10L, dtoResponse.getUsuarioId());
    }

    @Test
    void buscarIdUsuarioPorUsername_noExisteUsuario_lanzaExcepcion() {
        when(usuarioRepository.findByUsername("usuario10")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                usuarioService.buscarIdUsuarioPorUsername("usuario10"));

        assertTrue(exception.getMessage().contains("usuario10 no encontrado"));
    }
}
