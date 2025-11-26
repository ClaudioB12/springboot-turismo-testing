package pe.edu.upeu.turismospringboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.FamiliaDto;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;

import static org.junit.jupiter.api.Assertions.*;

class FamiliaDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // ===========================================
    // 1. TEST: JSON -> DTO
    // ===========================================
    @Test
    void debeConvertirJsonAFamiliaDto() throws Exception {
        String json = """
                {
                  "nombre": "Familia Andina",
                  "descripcion": "Conjunto cultural tradicional",
                  "nombreLugar": "Capachica"
                }
                """;

        FamiliaDto dto = objectMapper.readValue(json, FamiliaDto.class);

        assertEquals("Familia Andina", dto.getNombre());
        assertEquals("Conjunto cultural tradicional", dto.getDescripcion());
        assertEquals("Capachica", dto.getNombreLugar());
    }

    // ===========================================
    // 2. TEST: DTO -> JSON
    // ===========================================
    @Test
    void debeConvertirFamiliaDtoAJson() throws Exception {
        FamiliaDto dto = new FamiliaDto();
        dto.setNombre("Familia Sur");
        dto.setDescripcion("Descripción prueba");
        dto.setNombreLugar("Puno");

        String json = objectMapper.writeValueAsString(dto);

        assertTrue(json.contains("Familia Sur"));
        assertTrue(json.contains("Puno"));
        assertTrue(json.contains("Descripción prueba"));
    }

    // ===========================================
    // 3. TEST: DTO desde ENTIDAD Familia
    // ===========================================
    @Test
    void debeCrearDtoDesdeEntidadFamilia() {

        Lugar lugar = new Lugar();
        lugar.setNombre("Isla Ticonata");

        Familia familia = new Familia();
        familia.setNombre("Familia Ticonata");
        familia.setDescripcion("Descripción cultural");
        familia.setLugar(lugar);

        // Crear DTO manualmente como en tu proyecto
        FamiliaDto dto = new FamiliaDto();
        dto.setNombre(familia.getNombre());
        dto.setDescripcion(familia.getDescripcion());
        dto.setNombreLugar(familia.getLugar().getNombre());

        assertEquals("Familia Ticonata", dto.getNombre());
        assertEquals("Descripción cultural", dto.getDescripcion());
        assertEquals("Isla Ticonata", dto.getNombreLugar());
    }

    // ===========================================
    // 4. TEST: Campos nulos
    // ===========================================
    @Test
    void debeAceptarCamposNulos() {
        FamiliaDto dto = new FamiliaDto();

        dto.setNombre(null);
        dto.setDescripcion(null);
        dto.setNombreLugar(null);

        assertNull(dto.getNombre());
        assertNull(dto.getDescripcion());
        assertNull(dto.getNombreLugar());
    }

    // ===========================================
    // 5. TEST: Crear DTO cuando Lugar es null
    // ===========================================
    @Test
    void debeCrearDtoConLugarNulo() {
        Familia familia = new Familia();
        familia.setNombre("Familia Sin Lugar");
        familia.setDescripcion("Descripción simple");
        familia.setLugar(null); // Lugar nulo

        FamiliaDto dto = new FamiliaDto();
        dto.setNombre(familia.getNombre());
        dto.setDescripcion(familia.getDescripcion());
        dto.setNombreLugar(familia.getLugar() != null ? familia.getLugar().getNombre() : null);

        assertEquals("Familia Sin Lugar", dto.getNombre());
        assertEquals("Descripción simple", dto.getDescripcion());
        assertNull(dto.getNombreLugar());
    }
}
