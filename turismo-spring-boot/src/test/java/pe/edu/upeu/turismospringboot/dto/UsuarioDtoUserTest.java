package pe.edu.upeu.turismospringboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.config.LocalDateDeserializer;
import pe.edu.upeu.turismospringboot.model.dto.UsuarioDtoUser;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDtoUserTest {

    @Test
    void debeCrearYLeerPropiedadesCorrectamente() {
        UsuarioDtoUser dto = new UsuarioDtoUser();

        dto.setUsername("claudio");
        dto.setPassword("123456");
        dto.setNombres("Claudio");
        dto.setApellidos("Bustinza");
        dto.setTipoDocumento("DNI");
        dto.setNumeroDocumento("12345678");
        dto.setTelefono("987654321");
        dto.setDireccion("Avenida Los Olivos");
        dto.setCorreoElectronico("claudio@upeu.edu.pe");
        dto.setFechaNacimiento(LocalDate.of(2000, 5, 20));

        assertEquals("claudio", dto.getUsername());
        assertEquals("123456", dto.getPassword());
        assertEquals("Claudio", dto.getNombres());
        assertEquals("Bustinza", dto.getApellidos());
        assertEquals("DNI", dto.getTipoDocumento());
        assertEquals("12345678", dto.getNumeroDocumento());
        assertEquals("987654321", dto.getTelefono());
        assertEquals("Avenida Los Olivos", dto.getDireccion());
        assertEquals("claudio@upeu.edu.pe", dto.getCorreoElectronico());
        assertEquals(LocalDate.of(2000, 5, 20), dto.getFechaNacimiento());
    }

    @Test
    void debeDeserializarJsonConFechaCorrectamente() throws Exception {
        String json = """
        {
            "username": "wilbert",
            "password": "abc123",
            "nombres": "Wilbert",
            "apellidos": "Mayta",
            "tipoDocumento": "DNI",
            "numeroDocumento": "87654321",
            "telefono": "945123789",
            "direccion": "Jr. Lima",
            "correoElectronico": "wilbert@upeu.edu.pe",
            "fechaNacimiento": "2001-08-15"
        }
        """;

        ObjectMapper mapper = new ObjectMapper();

        UsuarioDtoUser dto = mapper.readValue(json, UsuarioDtoUser.class);

        assertEquals("wilbert", dto.getUsername());
        assertEquals("abc123", dto.getPassword());
        assertEquals("Wilbert", dto.getNombres());
        assertEquals("Mayta", dto.getApellidos());
        assertEquals("DNI", dto.getTipoDocumento());
        assertEquals("87654321", dto.getNumeroDocumento());
        assertEquals("945123789", dto.getTelefono());
        assertEquals("Jr. Lima", dto.getDireccion());
        assertEquals("wilbert@upeu.edu.pe", dto.getCorreoElectronico());

        assertEquals(LocalDate.of(2001, 8, 15), dto.getFechaNacimiento());
    }
}
