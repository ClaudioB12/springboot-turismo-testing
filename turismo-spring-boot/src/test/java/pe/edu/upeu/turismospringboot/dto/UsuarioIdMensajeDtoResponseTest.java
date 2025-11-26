package pe.edu.upeu.turismospringboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.UsuarioIdMensajeDtoResponse;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioIdMensajeDtoResponseTest {

    @Test
    void debeAsignarYLeerUsuarioIdCorrectamente() {
        UsuarioIdMensajeDtoResponse dto = new UsuarioIdMensajeDtoResponse();

        dto.setUsuarioId(10L);

        assertEquals(10L, dto.getUsuarioId());
    }

    @Test
    void debeSerializarYDeserializarJsonCorrectamente() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        UsuarioIdMensajeDtoResponse dto = new UsuarioIdMensajeDtoResponse();
        dto.setUsuarioId(25L);

        // Serializar a JSON
        String json = mapper.writeValueAsString(dto);
        assertTrue(json.contains("usuarioId"));

        // Deserializar desde JSON
        UsuarioIdMensajeDtoResponse dto2 =
                mapper.readValue("{\"usuarioId\": 25}", UsuarioIdMensajeDtoResponse.class);

        assertEquals(25L, dto2.getUsuarioId());
    }
}
