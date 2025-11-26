package pe.edu.upeu.turismospringboot.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.turismospringboot.model.entity.Rol;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.model.enums.EstadoCuenta;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    static Long usuarioId;
    static Rol rol;

    @BeforeEach
    void setUp() {
        // 1. Crear rol obligatorio
        rol = new Rol();
        rol.setNombre("USER");
        rol = rolRepository.save(rol);

        // 2. Crear usuario base
        Usuario usuario = new Usuario();
        usuario.setUsername("carlos");
        usuario.setPassword("123456");
        usuario.setRol(rol);
        usuario.setEstado(EstadoCuenta.ACTIVO);

        Usuario guardado = usuarioRepository.save(usuario);
        usuarioId = guardado.getIdUsuario();
    }

    @Test
    @Order(1)
    void testGuardarUsuario() {
        Usuario u = new Usuario();
        u.setUsername("maria");
        u.setPassword("pass");
        u.setRol(rol);
        u.setEstado(EstadoCuenta.ACTIVO);

        Usuario guardado = usuarioRepository.save(u);

        assertNotNull(guardado.getIdUsuario());
        assertEquals("maria", guardado.getUsername());
    }

    @Test
    @Order(2)
    void testBuscarPorId() {
        Optional<Usuario> u = usuarioRepository.findById(usuarioId);

        assertTrue(u.isPresent());
        assertEquals("carlos", u.get().getUsername());
    }

    @Test
    @Order(3)
    void testActualizarUsuario() {
        Usuario u = usuarioRepository.findById(usuarioId).orElseThrow();
        u.setUsername("carlos_updated");

        Usuario actualizado = usuarioRepository.save(u);

        assertEquals("carlos_updated", actualizado.getUsername());
    }

    @Test
    @Order(4)
    void testListarUsuarios() {
        List<Usuario> lista = usuarioRepository.findAll();
        assertFalse(lista.isEmpty());
    }

    @Test
    @Order(5)
    void testFindByUsername() {
        Optional<Usuario> u = usuarioRepository.findByUsername("carlos");

        assertTrue(u.isPresent());
        assertEquals("carlos", u.get().getUsername());
    }

    @Test
    @Order(6)
    void testBuscarPorUsernameParcial() {
        List<Usuario> lista = usuarioRepository.buscarPorUsername("car");

        assertFalse(lista.isEmpty());
        assertTrue(lista.stream().anyMatch(u -> u.getUsername().toLowerCase().contains("car")));
    }

    @Test
    @Order(7)
    void testBuscarPorUsernameCaseInsensitive() {
        List<Usuario> lista = usuarioRepository.buscarPorUsername("CarLoS");

        assertFalse(lista.isEmpty());
    }

    @Test
    @Order(8)
    void testCountUsuarios() {
        long total = usuarioRepository.count();

        assertTrue(total >= 1);
    }

    @Test
    @Order(9)
    void testEliminarUsuario() {
        usuarioRepository.deleteById(usuarioId);

        boolean existe = usuarioRepository.findById(usuarioId).isPresent();

        assertFalse(existe);
    }
}
