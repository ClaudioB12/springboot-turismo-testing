package pe.edu.upeu.turismospringboot.entity;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Persona;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PersonaTest {

    @Test
    void testGettersAndSetters() {
        Persona persona = new Persona();

        Usuario usuario = new Usuario();
        LocalDate nacimiento = LocalDate.of(2000, 5, 10);

        persona.setIdPersona(1L);
        persona.setNombres("Juan");
        persona.setApellidos("Perez");
        persona.setTipoDocumento("DNI");
        persona.setNumeroDocumento("12345678");
        persona.setTelefono("987654321");
        persona.setDireccion("Av. Siempre Viva");
        persona.setCorreoElectronico("correo@example.com");
        persona.setFotoPerfil("foto.png");
        persona.setFechaNacimiento(nacimiento);
        persona.setUsuario(usuario);

        assertEquals(1L, persona.getIdPersona());
        assertEquals("Juan", persona.getNombres());
        assertEquals("Perez", persona.getApellidos());
        assertEquals("DNI", persona.getTipoDocumento());
        assertEquals("12345678", persona.getNumeroDocumento());
        assertEquals("987654321", persona.getTelefono());
        assertEquals("Av. Siempre Viva", persona.getDireccion());
        assertEquals("correo@example.com", persona.getCorreoElectronico());
        assertEquals("foto.png", persona.getFotoPerfil());
        assertEquals(nacimiento, persona.getFechaNacimiento());
        assertEquals(usuario, persona.getUsuario());
    }

    @Test
    void testPrePersistSetsCreationDate() {
        Persona persona = new Persona();
        assertNull(persona.getFechaCreacionPersona());

        persona.onCreate();

        assertNotNull(persona.getFechaCreacionPersona());
        assertTrue(persona.getFechaCreacionPersona().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdateSetsModificationDate() {
        Persona persona = new Persona();
        assertNull(persona.getFechaModificacionPersona());

        persona.onUpdate();

        assertNotNull(persona.getFechaModificacionPersona());
        assertTrue(persona.getFechaModificacionPersona().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testNullableFieldsAllowed() {
        Persona persona = new Persona();

        persona.setApellidos(null);
        persona.setTipoDocumento(null);
        persona.setNumeroDocumento(null);
        persona.setTelefono(null);
        persona.setDireccion(null);
        persona.setCorreoElectronico(null);
        persona.setFotoPerfil(null);
        persona.setUsuario(null);

        assertNull(persona.getApellidos());
        assertNull(persona.getTipoDocumento());
        assertNull(persona.getNumeroDocumento());
        assertNull(persona.getTelefono());
        assertNull(persona.getDireccion());
        assertNull(persona.getCorreoElectronico());
        assertNull(persona.getFotoPerfil());
        assertNull(persona.getUsuario());
    }

    @Test
    void testEqualsAndHashCode() {
        Persona p1 = new Persona();
        p1.setIdPersona(10L);

        Persona p2 = new Persona();
        p2.setIdPersona(10L);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testNotEquals() {
        Persona p1 = new Persona();
        p1.setIdPersona(1L);

        Persona p2 = new Persona();
        p2.setIdPersona(2L);

        assertNotEquals(p1, p2);
    }

    @Test
    void testToStringContainsImportantFields() {
        Persona persona = new Persona();
        persona.setNombres("Maria");
        persona.setApellidos("Lopez");

        String toStr = persona.toString();

        assertTrue(toStr.contains("Maria"));
        assertTrue(toStr.contains("Lopez"));
        assertTrue(toStr.contains("Persona"));
    }

    @Test
    void testFechaNacimiento() {
        Persona persona = new Persona();
        LocalDate fecha = LocalDate.of(1999, 1, 20);

        persona.setFechaNacimiento(fecha);

        assertEquals(fecha, persona.getFechaNacimiento());
    }
}
