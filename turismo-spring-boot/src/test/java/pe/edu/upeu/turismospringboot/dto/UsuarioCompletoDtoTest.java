package pe.edu.upeu.turismospringboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.config.LocalDateDeserializer;
import pe.edu.upeu.turismospringboot.model.dto.UsuarioCompletoDto;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioCompletoDtoTest {

    @Test
    void testConstructorVacio() {
        UsuarioCompletoDto dto = new UsuarioCompletoDto();

        assertNull(dto.getUsername());
        assertNull(dto.getPassword());
        assertNull(dto.getEstadoCuenta());
        assertNull(dto.getNombreRol());
        assertNull(dto.getNombreEmprendimiento());
        assertNull(dto.getNombres());
        assertNull(dto.getApellidos());
        assertNull(dto.getTipoDocumento());
        assertNull(dto.getNumeroDocumento());
        assertNull(dto.getTelefono());
        assertNull(dto.getDireccion());
        assertNull(dto.getCorreoElectronico());
        assertNull(dto.getFotoPerfil());
        assertNull(dto.getFechaNacimiento());
    }

    @Test
    void testSettersAndGetters() {
        UsuarioCompletoDto dto = new UsuarioCompletoDto();

        dto.setUsername("claudio");
        dto.setPassword("123456");
        dto.setEstadoCuenta("ACTIVO");
        dto.setNombreRol("ADMIN");
        dto.setNombreEmprendimiento("Turismo Puno");
        dto.setNombres("Claudio Antonio");
        dto.setApellidos("Bustinza Inofuente");
        dto.setTipoDocumento("DNI");
        dto.setNumeroDocumento("12345678");
        dto.setTelefono("987654321");
        dto.setDireccion("Capachica");
        dto.setCorreoElectronico("claudio@gmail.com");
        dto.setFotoPerfil("foto.jpg");
        dto.setFechaNacimiento(LocalDate.of(2000, 5, 10));

        assertEquals("claudio", dto.getUsername());
        assertEquals("123456", dto.getPassword());
        assertEquals("ACTIVO", dto.getEstadoCuenta());
        assertEquals("ADMIN", dto.getNombreRol());
        assertEquals("Turismo Puno", dto.getNombreEmprendimiento());
        assertEquals("Claudio Antonio", dto.getNombres());
        assertEquals("Bustinza Inofuente", dto.getApellidos());
        assertEquals("DNI", dto.getTipoDocumento());
        assertEquals("12345678", dto.getNumeroDocumento());
        assertEquals("987654321", dto.getTelefono());
        assertEquals("Capachica", dto.getDireccion());
        assertEquals("claudio@gmail.com", dto.getCorreoElectronico());
        assertEquals("foto.jpg", dto.getFotoPerfil());
        assertEquals(LocalDate.of(2000, 5, 10), dto.getFechaNacimiento());
    }

    @Test
    void testEqualsAndHashCode() {
        UsuarioCompletoDto dto1 = new UsuarioCompletoDto();
        dto1.setUsername("user1");

        UsuarioCompletoDto dto2 = new UsuarioCompletoDto();
        dto2.setUsername("user1");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEquals() {
        UsuarioCompletoDto dto1 = new UsuarioCompletoDto();
        dto1.setUsername("user1");

        UsuarioCompletoDto dto2 = new UsuarioCompletoDto();
        dto2.setUsername("user2");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        UsuarioCompletoDto dto = new UsuarioCompletoDto();
        dto.setUsername("claudio");
        dto.setCorreoElectronico("correo@example.com");

        String texto = dto.toString();
        assertTrue(texto.contains("claudio"));
        assertTrue(texto.contains("correo@example.com"));
    }

    @Test
    void testJsonDeserializationWithLocalDate() throws Exception {
        String json = """
                {
                    "username": "claudio",
                    "fechaNacimiento": "2000-05-10"
                }
                """;

        ObjectMapper mapper = new ObjectMapper();
        UsuarioCompletoDto dto = mapper.readValue(json, UsuarioCompletoDto.class);

        assertEquals("claudio", dto.getUsername());
        assertEquals(LocalDate.of(2000, 5, 10), dto.getFechaNacimiento());
    }
}
