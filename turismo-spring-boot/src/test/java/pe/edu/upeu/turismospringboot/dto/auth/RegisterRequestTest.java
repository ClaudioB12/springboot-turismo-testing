package pe.edu.upeu.turismospringboot.dto.auth;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.auth.RegisterRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void testConstructorVacio() {
        RegisterRequest req = new RegisterRequest();

        assertNull(req.getUsername());
        assertNull(req.getPassword());
        assertNull(req.getNombres());
        assertNull(req.getApellidos());
        assertNull(req.getTipoDocumento());
        assertNull(req.getNumeroDocumento());
        assertNull(req.getTelefono());
        assertNull(req.getDireccion());
        assertNull(req.getCorreoElectronico());
        assertNull(req.getFechaNacimiento());
    }

    @Test
    void testConstructorCompleto() {
        LocalDate fecha = LocalDate.of(2000, 5, 15);

        RegisterRequest req = new RegisterRequest(
                "usuario1",
                "pass123",
                "Juan",
                "Perez",
                "DNI",
                "12345678",
                "987654321",
                "Av. Lima",
                "correo@test.com",
                fecha
        );

        assertEquals("usuario1", req.getUsername());
        assertEquals("pass123", req.getPassword());
        assertEquals("Juan", req.getNombres());
        assertEquals("Perez", req.getApellidos());
        assertEquals("DNI", req.getTipoDocumento());
        assertEquals("12345678", req.getNumeroDocumento());
        assertEquals("987654321", req.getTelefono());
        assertEquals("Av. Lima", req.getDireccion());
        assertEquals("correo@test.com", req.getCorreoElectronico());
        assertEquals(fecha, req.getFechaNacimiento());
    }

    @Test
    void testBuilder() {
        LocalDate fecha = LocalDate.of(1999, 12, 1);

        RegisterRequest req = RegisterRequest.builder()
                .username("admin")
                .password("1234")
                .nombres("Ana")
                .apellidos("Lopez")
                .tipoDocumento("CE")
                .numeroDocumento("A12345")
                .telefono("900111222")
                .direccion("Calle 123")
                .correoElectronico("ana@test.com")
                .fechaNacimiento(fecha)
                .build();

        assertEquals("admin", req.getUsername());
        assertEquals("1234", req.getPassword());
        assertEquals("Ana", req.getNombres());
        assertEquals("Lopez", req.getApellidos());
        assertEquals("CE", req.getTipoDocumento());
        assertEquals("A12345", req.getNumeroDocumento());
        assertEquals("900111222", req.getTelefono());
        assertEquals("Calle 123", req.getDireccion());
        assertEquals("ana@test.com", req.getCorreoElectronico());
        assertEquals(fecha, req.getFechaNacimiento());
    }

    @Test
    void testSettersAndGetters() {
        RegisterRequest req = new RegisterRequest();
        LocalDate fecha = LocalDate.of(1980, 3, 20);

        req.setUsername("test");
        req.setPassword("pwd");
        req.setNombres("Jose");
        req.setApellidos("Gomez");
        req.setTipoDocumento("DNI");
        req.setNumeroDocumento("76543210");
        req.setTelefono("999888777");
        req.setDireccion("Las flores 123");
        req.setCorreoElectronico("test@test.com");
        req.setFechaNacimiento(fecha);

        assertEquals("test", req.getUsername());
        assertEquals("pwd", req.getPassword());
        assertEquals("Jose", req.getNombres());
        assertEquals("Gomez", req.getApellidos());
        assertEquals("DNI", req.getTipoDocumento());
        assertEquals("76543210", req.getNumeroDocumento());
        assertEquals("999888777", req.getTelefono());
        assertEquals("Las flores 123", req.getDireccion());
        assertEquals("test@test.com", req.getCorreoElectronico());
        assertEquals(fecha, req.getFechaNacimiento());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDate fecha = LocalDate.of(2000, 1, 1);

        RegisterRequest a = RegisterRequest.builder()
                .username("user")
                .password("pass")
                .nombres("Juan")
                .apellidos("Perez")
                .tipoDocumento("DNI")
                .numeroDocumento("11111111")
                .telefono("999999999")
                .direccion("Av. Siempre Viva")
                .correoElectronico("jp@test.com")
                .fechaNacimiento(fecha)
                .build();

        RegisterRequest b = RegisterRequest.builder()
                .username("user")
                .password("pass")
                .nombres("Juan")
                .apellidos("Perez")
                .tipoDocumento("DNI")
                .numeroDocumento("11111111")
                .telefono("999999999")
                .direccion("Av. Siempre Viva")
                .correoElectronico("jp@test.com")
                .fechaNacimiento(fecha)
                .build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToString() {
        RegisterRequest req = RegisterRequest.builder()
                .username("userx")
                .password("pwd")
                .nombres("Maria")
                .build();

        String str = req.toString();

        assertTrue(str.contains("userx"));
        assertTrue(str.contains("Maria"));
        assertTrue(str.contains("RegisterRequest"));
    }
}
