package pe.edu.upeu.turismospringboot.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.UbicacionDTO;

import static org.junit.jupiter.api.Assertions.*;

class UbicacionDTOTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void debeDeserializarDesdeJsonCorrectamente() throws Exception {

        String json = """
                {
                  "lat": -14.98,
                  "lng": -70.01,
                  "titulo": "Puerto de Capachica",
                  "tipo": "Turismo",
                  "descripcion": "Zona de embarcaciones y tours",
                  "imagen": "puerto.jpg"
                }
                """;

        // Usa el constructor con argumentos explícitamente
        objectMapper.addMixIn(UbicacionDTO.class, UbicacionDTOMixin.class);

        UbicacionDTO dto = objectMapper.readValue(json, UbicacionDTO.class);

        assertNotNull(dto);
        assertEquals(-14.98, dto.getLat());
        assertEquals(-70.01, dto.getLng());
        assertEquals("Puerto de Capachica", dto.getTitulo());
        assertEquals("Turismo", dto.getTipo());
        assertEquals("Zona de embarcaciones y tours", dto.getDescripcion());
        assertEquals("puerto.jpg", dto.getImagen());
    }

    // MIXIN: le dice a Jackson cómo usar tu constructor existente
    abstract static class UbicacionDTOMixin {

        @JsonCreator
        public UbicacionDTOMixin(
                @JsonProperty("lat") double lat,
                @JsonProperty("lng") double lng,
                @JsonProperty("titulo") String titulo,
                @JsonProperty("tipo") String tipo,
                @JsonProperty("descripcion") String descripcion,
                @JsonProperty("imagen") String imagen
        ) {}
    }
}
