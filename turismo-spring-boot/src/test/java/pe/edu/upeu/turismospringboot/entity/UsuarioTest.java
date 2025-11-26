package pe.edu.upeu.turismospringboot.entity;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import pe.edu.upeu.turismospringboot.model.entity.*;
import pe.edu.upeu.turismospringboot.model.enums.EstadoCuenta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testGettersAndSetters() {
        Usuario u = new Usuario();
        u.setIdUsuario(1L);
        u.setUsername("claudio");
        u.setPassword("123456");
        u.setEstado(EstadoCuenta.ACTIVO);

        assertEquals(1L, u.getIdUsuario());
        assertEquals("claudio", u.getUsername());
        assertEquals("123456", u.getPassword());
        assertEquals(EstadoCuenta.ACTIVO, u.getEstado());
    }

    @Test
    void testRelaciones() {
        Usuario u = new Usuario();

        Rol rol = new Rol();
        rol.setNombre("ADMIN");

        Persona persona = new Persona();
        persona.setNombres("Claudio");

        Emprendimiento emp = new Emprendimiento();
        emp.setNombre("Turismo Capachica");

        u.setRol(rol);
        u.setPersona(persona);
        u.setEmprendimiento(emp);

        assertEquals("ADMIN", u.getRol().getNombre());
        assertEquals("Claudio", u.getPersona().getNombres());
        assertEquals("Turismo Capachica", u.getEmprendimiento().getNombre());
    }

    @Test
    void testListInitialization() {
        Usuario u = new Usuario();

        assertNotNull(u.getBitacoraAccesoList());
        assertNotNull(u.getNoticias());
        assertNotNull(u.getResenas());
        assertNotNull(u.getReservas());

        assertEquals(0, u.getBitacoraAccesoList().size());
        assertEquals(0, u.getNoticias().size());
        assertEquals(0, u.getResenas().size());
        assertEquals(0, u.getReservas().size());
    }

    @Test
    void testAddElementsToLists() {
        Usuario u = new Usuario();

        BitacoraAcceso b = new BitacoraAcceso();
        Reserva r = new Reserva();
        Noticia n = new Noticia();
        Resena re = new Resena();

        u.getBitacoraAccesoList().add(b);
        u.getReservas().add(r);
        u.getNoticias().add(n);
        u.getResenas().add(re);

        assertEquals(1, u.getBitacoraAccesoList().size());
        assertEquals(1, u.getReservas().size());
        assertEquals(1, u.getNoticias().size());
        assertEquals(1, u.getResenas().size());
    }

    @Test
    void testPrePersist() {
        Usuario u = new Usuario();

        assertNull(u.getFechaCreacionUsuario());

        u.onCreate();

        assertNotNull(u.getFechaCreacionUsuario());
        assertTrue(u.getFechaCreacionUsuario().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdate() {
        Usuario u = new Usuario();

        assertNull(u.getFechaModificacionUsuario());

        u.onUpdate();

        assertNotNull(u.getFechaModificacionUsuario());
        assertTrue(u.getFechaModificacionUsuario().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testGetAuthoritiesWithRol() {
        Usuario u = new Usuario();
        Rol rol = new Rol();
        rol.setNombre("ROLE_ADMIN");
        u.setRol(rol);

        Collection<? extends GrantedAuthority> authorities = u.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void testGetAuthoritiesWithoutRol() {
        Usuario u = new Usuario();
        u.setRol(null);

        assertTrue(u.getAuthorities().isEmpty());
    }

    @Test
    void testGetAuthoritiesWithRolSinNombre() {
        Usuario u = new Usuario();
        Rol rol = new Rol();
        rol.setNombre(null);
        u.setRol(rol);

        assertTrue(u.getAuthorities().isEmpty());
    }

    @Test
    void testUserDetailsMethods() {
        Usuario u = new Usuario();

        assertTrue(u.isAccountNonExpired());
        assertTrue(u.isAccountNonLocked());
        assertTrue(u.isCredentialsNonExpired());
        assertTrue(u.isEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        Usuario u1 = new Usuario();
        u1.setIdUsuario(10L);

        Usuario u2 = new Usuario();
        u2.setIdUsuario(10L);

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testNotEquals() {
        Usuario u1 = new Usuario();
        u1.setIdUsuario(1L);

        Usuario u2 = new Usuario();
        u2.setIdUsuario(2L);

        assertNotEquals(u1, u2);
    }
}
