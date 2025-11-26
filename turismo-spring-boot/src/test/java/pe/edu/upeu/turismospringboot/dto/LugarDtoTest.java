package pe.edu.upeu.turismospringboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.LugarDto;

import static org.junit.jupiter.api.Assertions.*;

class LugarDtoTest {

    private LugarDto lugar;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        lugar = new LugarDto();
        objectMapper = new ObjectMapper();

        // Valores iniciales
        lugar.setNombre("Plaza de Armas");
        lugar.setDescripcion("Lugar turístico principal");
        lugar.setDireccion("Centro histórico");
        lugar.setCiudad("Puno");
        lugar.setProvincia("Puno");
        lugar.setPais("Perú");
        lugar.setLatitud(-15.8402);
        lugar.setLongitud(-70.0219);
    }

    // ==============================
    // Test de GETTERS y SETTERS
    // ==============================

    @Test
    void testGettersSetters() {
        assertEquals("Plaza de Armas", lugar.getNombre());
        assertEquals("Lugar turístico principal", lugar.getDescripcion());
        assertEquals("Centro histórico", lugar.getDireccion());
        assertEquals("Puno", lugar.getCiudad());
        assertEquals("Puno", lugar.getProvincia());
        assertEquals("Perú", lugar.getPais());
        assertEquals(-15.8402, lugar.getLatitud());
        assertEquals(-70.0219, lugar.getLongitud());
    }

    // ==============================
    // Test de serialización a JSON
    // ==============================

    @Test
    void testSerializacionJson() throws Exception {
        String json = objectMapper.writeValueAsString(lugar);

        assertTrue(json.contains("\"nombre\":\"Plaza de Armas\""));
        assertTrue(json.contains("\"descripcion\":\"Lugar turístico principal\""));
        assertTrue(json.contains("\"direccion\":\"Centro histórico\""));
        assertTrue(json.contains("\"ciudad\":\"Puno\""));
        assertTrue(json.contains("\"provincia\":\"Puno\""));
        assertTrue(json.contains("\"pais\":\"Perú\""));
        assertTrue(json.contains("\"latitud\":-15.8402"));
        assertTrue(json.contains("\"longitud\":-70.0219"));
    }

    // ==============================
    // Test de deserialización desde JSON
    // ==============================

    @Test
    void testDeserializacionJson() throws Exception {
        String json = """
                {
                  "nombre": "Isla Uros",
                  "descripcion": "Islas flotantes",
                  "direccion": "Lago Titicaca",
                  "ciudad": "Puno",
                  "provincia": "Puno",
                  "pais": "Perú",
                  "latitud": -15.76,
                  "longitud": -69.98
                }
                """;

        LugarDto dto = objectMapper.readValue(json, LugarDto.class);

        assertEquals("Isla Uros", dto.getNombre());
        assertEquals("Islas flotantes", dto.getDescripcion());
        assertEquals("Lago Titicaca", dto.getDireccion());
        assertEquals("Puno", dto.getCiudad());
        assertEquals("Puno", dto.getProvincia());
        assertEquals("Perú", dto.getPais());
        assertEquals(-15.76, dto.getLatitud());
        assertEquals(-69.98, dto.getLongitud());
    }

    // ==============================
    // Validar valores nulos
    // ==============================

    @Test
    void testValoresNulos() {
        LugarDto dto = new LugarDto();

        assertNull(dto.getNombre());
        assertNull(dto.getDescripcion());
        assertNull(dto.getDireccion());
        assertNull(dto.getCiudad());
        assertNull(dto.getProvincia());
        assertNull(dto.getPais());
        assertNull(dto.getLatitud());
        assertNull(dto.getLongitud());
    }
}
