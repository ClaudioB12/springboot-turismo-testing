package pe.edu.upeu.turismospringboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.RolDto;

import static org.junit.jupiter.api.Assertions.*;

class RolDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void debeAsignarYObtenerNombreCorrectamente() {
        RolDto dto = new RolDto();
        dto.setNombre("ADMIN");

        assertEquals("ADMIN", dto.getNombre());
    }

    @Test
    void debeSerializarAJsonCorrectamente() throws Exception {
        RolDto dto = new RolDto();
        dto.setNombre("USUARIO");

        String json = objectMapper.writeValueAsString(dto);

        assertTrue(json.contains("\"nombre\":\"USUARIO\""));
    }

    @Test
    void debeDeserializarDesdeJsonCorrectamente() throws Exception {
        String json = "{\"nombre\":\"EMPRENDEDOR\"}";

        RolDto dto = objectMapper.readValue(json, RolDto.class);

        assertNotNull(dto);
        assertEquals("EMPRENDEDOR", dto.getNombre());
    }

    @Test
    void debeMantenerConsistenciaEntreObjetos() {
        RolDto r1 = new RolDto();
        RolDto r2 = new RolDto();

        r1.setNombre("ADMIN");
        r2.setNombre("ADMIN");

        assertEquals(r1.getNombre(), r2.getNombre());
    }
}
