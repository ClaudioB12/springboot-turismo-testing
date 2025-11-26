package pe.edu.upeu.turismospringboot.dto;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.EmprendimientoDto;

import static org.junit.jupiter.api.Assertions.*;

class EmprendimientoDtoTest {

    @Test
    void testConstructorVacio() {
        EmprendimientoDto dto = new EmprendimientoDto();

        assertNull(dto.getNombre());
        assertNull(dto.getDescripcion());
        assertNull(dto.getLatitud());
        assertNull(dto.getLongitud());
        assertNull(dto.getIdFamiliaCategoria());
    }

    @Test
    void testSettersAndGetters() {
        EmprendimientoDto dto = new EmprendimientoDto();

        dto.setNombre("Aventura Andina");
        dto.setDescripcion("Turismo vivencial");
        dto.setLatitud(-15.8401);
        dto.setLongitud(-70.0212);
        dto.setIdFamiliaCategoria(5L);

        assertEquals("Aventura Andina", dto.getNombre());
        assertEquals("Turismo vivencial", dto.getDescripcion());
        assertEquals(-15.8401, dto.getLatitud());
        assertEquals(-70.0212, dto.getLongitud());
        assertEquals(5L, dto.getIdFamiliaCategoria());
    }

    @Test
    void testEqualsAndHashCode() {
        EmprendimientoDto dto1 = new EmprendimientoDto();
        dto1.setNombre("Eco Lodge");
        dto1.setDescripcion("Hospedaje ecológico");
        dto1.setLatitud(-15.0);
        dto1.setLongitud(-70.0);
        dto1.setIdFamiliaCategoria(2L);

        EmprendimientoDto dto2 = new EmprendimientoDto();
        dto2.setNombre("Eco Lodge");
        dto2.setDescripcion("Hospedaje ecológico");
        dto2.setLatitud(-15.0);
        dto2.setLongitud(-70.0);
        dto2.setIdFamiliaCategoria(2L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEquals() {
        EmprendimientoDto dto1 = new EmprendimientoDto();
        dto1.setNombre("A");

        EmprendimientoDto dto2 = new EmprendimientoDto();
        dto2.setNombre("B");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        EmprendimientoDto dto = new EmprendimientoDto();
        dto.setNombre("Casa Rural");

        String output = dto.toString();
        assertTrue(output.contains("Casa Rural"));
        assertTrue(output.contains("EmprendimientoDto"));
    }

    @Test
    void testValoresNumericos() {
        EmprendimientoDto dto = new EmprendimientoDto();

        dto.setLatitud(0.0);
        dto.setLongitud(0.0);

        assertEquals(0.0, dto.getLatitud());
        assertEquals(0.0, dto.getLongitud());
    }
}
