package pe.edu.upeu.turismospringboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.MensajeDto;
import pe.edu.upeu.turismospringboot.model.enums.EstadoMensaje;
import pe.edu.upeu.turismospringboot.model.enums.TipoMensaje;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MensajeDtoTest {

    // 🔥 CORRECCIÓN CRUCIAL -> habilita LocalDateTime
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void testCrearMensajeDto() {
        MensajeDto dto = new MensajeDto();
        dto.setId(1L);
        dto.setEmisorUsername("userA");
        dto.setReceptorUsername("userB");
        dto.setContenidoTexto("hola");
        dto.setContenidoArchivo("imagen.png");
        dto.setTipo(TipoMensaje.DOCUMENTO);
        dto.setEstado(EstadoMensaje.ENVIADO);
        dto.setEditado(false);
        dto.setFechaEnvio(LocalDateTime.now());

        assertEquals(1L, dto.getId());
        assertEquals("userA", dto.getEmisorUsername());
        assertEquals("userB", dto.getReceptorUsername());
        assertEquals("hola", dto.getContenidoTexto());
    }

    @Test
    void testSerializacionJson() throws Exception {
        MensajeDto dto = new MensajeDto();
        dto.setId(10L);
        dto.setEmisorUsername("userX");
        dto.setReceptorUsername("userY");
        dto.setContenidoTexto("mensaje de prueba");
        dto.setContenidoArchivo("doc.pdf");
        dto.setTipo(TipoMensaje.DOCUMENTO);
        dto.setEstado(EstadoMensaje.LEIDO);
        dto.setEditado(true);
        dto.setFechaEnvio(LocalDateTime.of(2025, 1, 1, 12, 30));

        String json = objectMapper.writeValueAsString(dto);

        assertNotNull(json);
        assertTrue(json.contains("\"tipo\":\"DOCUMENTO\""));
    }

    @Test
    void testDeserializacionJson() throws Exception {
        String json = """
        {
          "id": 20,
          "emisorUsername": "userA",
          "receptorUsername": "userB",
          "contenidoTexto": "archivo enviado",
          "contenidoArchivo": "file.png",
          "tipo": "DOCUMENTO",
          "estado": "ENVIADO",
          "editado": true,
          "fechaEnvio": "2025-02-01T10:45:00"
        }
        """;

        MensajeDto dto = objectMapper.readValue(json, MensajeDto.class);

        assertNotNull(dto);
        assertEquals(20L, dto.getId());
        assertEquals("userA", dto.getEmisorUsername());
        assertEquals("userB", dto.getReceptorUsername());
        assertEquals("DOCUMENTO", dto.getTipo().name());
        assertEquals(EstadoMensaje.ENVIADO, dto.getEstado());
    }

    @Test
    void testEnumsValidos() {
        assertDoesNotThrow(() -> TipoMensaje.valueOf("TEXTO"));
        assertDoesNotThrow(() -> TipoMensaje.valueOf("IMAGEN"));
        assertDoesNotThrow(() -> TipoMensaje.valueOf("DOCUMENTO"));
        assertDoesNotThrow(() -> TipoMensaje.valueOf("VIDEO"));
        assertDoesNotThrow(() -> TipoMensaje.valueOf("TEXTO_ARCHIVO"));
        assertDoesNotThrow(() -> TipoMensaje.valueOf("AUDIO"));
    }
}
