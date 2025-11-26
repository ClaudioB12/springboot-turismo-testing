package pe.edu.upeu.turismospringboot.entity;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Rol;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    @Test
    void testGettersAndSetters() {
        Rol rol = new Rol();

        rol.setIdRol(1L);
        rol.setNombre("ADMIN");

        assertEquals(1L, rol.getIdRol());
        assertEquals("ADMIN", rol.getNombre());
    }

    @Test
    void testUsuariosListInitialization() {
        Rol rol = new Rol();
        assertNotNull(rol.getUsuarios());
        assertEquals(0, rol.getUsuarios().size());
    }

    @Test
    void testAddUsuarios() {
        Rol rol = new Rol();
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();

        rol.getUsuarios().add(u1);
        rol.getUsuarios().add(u2);

        assertEquals(2, rol.getUsuarios().size());
    }

    @Test
    void testRemoveUsuarios() {
        Rol rol = new Rol();
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();

        rol.getUsuarios().add(u1);
        rol.getUsuarios().add(u2);
        rol.getUsuarios().remove(u1);

        assertEquals(1, rol.getUsuarios().size());
        assertTrue(rol.getUsuarios().contains(u2));
    }

    @Test
    void testPrePersistSetsCreationDate() {
        Rol rol = new Rol();
        assertNull(rol.getFechaCreacionRol());

        rol.onCreate();

        assertNotNull(rol.getFechaCreacionRol());
        assertTrue(rol.getFechaCreacionRol().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdateSetsModificationDate() {
        Rol rol = new Rol();
        assertNull(rol.getFechaModificacionRol());

        rol.onUpdate();

        assertNotNull(rol.getFechaModificacionRol());
        assertTrue(rol.getFechaModificacionRol().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testEqualsAndHashCode() {
        Rol r1 = new Rol();
        r1.setIdRol(10L);

        Rol r2 = new Rol();
        r2.setIdRol(10L);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testNotEquals() {
        Rol r1 = new Rol();
        r1.setIdRol(1L);

        Rol r2 = new Rol();
        r2.setIdRol(2L);

        assertNotEquals(r1, r2);
    }

    @Test
    void testToStringContainsNombre() {
        Rol rol = new Rol();
        rol.setNombre("USER");

        String str = rol.toString();

        assertTrue(str.contains("USER"));
        assertTrue(str.contains("Rol"));
    }

    @Test
    void testNullableFields() {
        Rol rol = new Rol();

        rol.setNombre(null);
        rol.setUsuarios(null);

        assertNull(rol.getNombre());
        assertNull(rol.getUsuarios());
    }

    @Test
    void testSetUsuariosList() {
        Rol rol = new Rol();

        ArrayList<Usuario> lista = new ArrayList<>();
        Usuario u = new Usuario();
        lista.add(u);

        rol.setUsuarios(lista);

        assertEquals(1, rol.getUsuarios().size());
        assertEquals(u, rol.getUsuarios().get(0));
    }
}
