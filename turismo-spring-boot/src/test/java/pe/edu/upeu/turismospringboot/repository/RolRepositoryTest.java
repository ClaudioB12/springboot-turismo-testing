package pe.edu.upeu.turismospringboot.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.turismospringboot.model.entity.Rol;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RolRepositoryTest {

    @Autowired
    private RolRepository rolRepository;

    static Long rolId;

    @BeforeEach
    void setUp() {
        Rol rol = new Rol();
        rol.setNombre("ADMIN");
        Rol guardado = rolRepository.save(rol);

        rolId = guardado.getIdRol();
    }

    @Test
    @Order(1)
    void testGuardarRol() {
        Rol rol = new Rol();
        rol.setNombre("USER");

        Rol guardado = rolRepository.save(rol);

        assertNotNull(guardado.getIdRol());
        assertEquals("USER", guardado.getNombre());
    }

    @Test
    @Order(2)
    void testBuscarPorId() {
        Optional<Rol> rol = rolRepository.findById(rolId);

        assertTrue(rol.isPresent());
        assertEquals("ADMIN", rol.get().getNombre());
    }

    @Test
    @Order(3)
    void testActualizarRol() {
        Rol rol = rolRepository.findById(rolId).orElseThrow();
        rol.setNombre("ADMINISTRADOR");

        Rol actualizado = rolRepository.save(rol);

        assertEquals("ADMINISTRADOR", actualizado.getNombre());
    }

    @Test
    @Order(4)
    void testListarRoles() {
        List<Rol> lista = rolRepository.findAll();

        assertFalse(lista.isEmpty());
    }

    @Test
    @Order(5)
    void testFindByNombre() {
        Optional<Rol> rol = rolRepository.findByNombre("ADMIN");

        assertTrue(rol.isPresent());
        assertEquals("ADMIN", rol.get().getNombre());
    }

    @Test
    @Order(6)
    void testBuscarPorNombreParcial() {
        List<Rol> lista = rolRepository.buscarPorNombre("adm");

        assertFalse(lista.isEmpty());
        assertTrue(
                lista.stream().anyMatch(r -> r.getNombre().toLowerCase().contains("adm"))
        );
    }

    @Test
    @Order(7)
    void testBuscarPorNombreCaseInsensitive() {
        List<Rol> lista = rolRepository.buscarPorNombre("AdMi");

        assertFalse(lista.isEmpty());
    }

    @Test
    @Order(8)
    void testEliminarRol() {
        rolRepository.deleteById(rolId);

        Optional<Rol> rolEliminado = rolRepository.findById(rolId);

        assertFalse(rolEliminado.isPresent());
    }
}
