package pe.edu.upeu.turismospringboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.ServicioTuristicoDto;

import static org.junit.jupiter.api.Assertions.*;

class ServicioTuristicoDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void debeAsignarYObtenerValoresCorrectamente() {
        ServicioTuristicoDto dto = new ServicioTuristicoDto();

        dto.setIdServicio(10L);
        dto.setNombre("Tour en Kayak");
        dto.setDescripcion("Recorrido guiado en el lago Titicaca");
        dto.setPrecioUnitario(150.0);
        dto.setTipoServicio("TURISMO");
        dto.setNombreEmprendimiento("Aventura Capachica");

        assertEquals(10L, dto.getIdServicio());
        assertEquals("Tour en Kayak", dto.getNombre());
        assertEquals("Recorrido guiado en el lago Titicaca", dto.getDescripcion());
        assertEquals(150.0, dto.getPrecioUnitario());
        assertEquals("TURISMO", dto.getTipoServicio());
        assertEquals("Aventura Capachica", dto.getNombreEmprendimiento());
    }

    @Test
    void debeSerializarAJsonCorrectamente() throws Exception {
        ServicioTuristicoDto dto = new ServicioTuristicoDto();
        dto.setIdServicio(5L);
        dto.setNombre("Desayuno Andino");
        dto.setDescripcion("Incluye quinua, kiwicha y pan artesanal");
        dto.setPrecioUnitario(25.5);
        dto.setTipoServicio("ALIMENTACIÓN");
        dto.setNombreEmprendimiento("Sabores de Capachica");

        String json = objectMapper.writeValueAsString(dto);

        assertTrue(json.contains("\"idServicio\":5"));
        assertTrue(json.contains("\"nombre\":\"Desayuno Andino\""));
        assertTrue(json.contains("\"descripcion\":\"Incluye quinua, kiwicha y pan artesanal\""));
        assertTrue(json.contains("\"precioUnitario\":25.5"));
        assertTrue(json.contains("\"tipoServicio\":\"ALIMENTACIÓN\""));
        assertTrue(json.contains("\"nombreEmprendimiento\":\"Sabores de Capachica\""));
    }

    @Test
    void debeDeserializarDesdeJsonCorrectamente() throws Exception {
        String json = """
                {
                    "idServicio": 20,
                    "nombre": "Habitación Doble",
                    "descripcion": "Con vista al lago",
                    "precioUnitario": 120.0,
                    "tipoServicio": "HOSPEDAJE",
                    "nombreEmprendimiento": "Hostal Titicaca"
                }
                """;

        ServicioTuristicoDto dto = objectMapper.readValue(json, ServicioTuristicoDto.class);

        assertNotNull(dto);
        assertEquals(20L, dto.getIdServicio());
        assertEquals("Habitación Doble", dto.getNombre());
        assertEquals("Con vista al lago", dto.getDescripcion());
        assertEquals(120.0, dto.getPrecioUnitario());
        assertEquals("HOSPEDAJE", dto.getTipoServicio());
        assertEquals("Hostal Titicaca", dto.getNombreEmprendimiento());
    }

    @Test
    void debeMantenerConsistenciaEntreObjetos() {
        ServicioTuristicoDto s1 = new ServicioTuristicoDto();
        ServicioTuristicoDto s2 = new ServicioTuristicoDto();

        s1.setNombre("Tour a las Islas");
        s2.setNombre("Tour a las Islas");

        assertEquals(s1.getNombre(), s2.getNombre());
    }
}
