package pe.edu.upeu.turismospringboot.dto;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.CategoriaDto;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaDtoTest {

    @Test
    void testConstructorVacio() {
        CategoriaDto dto = new CategoriaDto();

        assertNull(dto.getNombre());
        assertNull(dto.getDescripcion());
    }

    @Test
    void testSettersAndGetters() {
        CategoriaDto dto = new CategoriaDto();

        dto.setNombre("Aventura");
        dto.setDescripcion("Actividades extremas y al aire libre");

        assertEquals("Aventura", dto.getNombre());
        assertEquals("Actividades extremas y al aire libre", dto.getDescripcion());
    }

    @Test
    void testEqualsAndHashCode() {
        CategoriaDto dto1 = new CategoriaDto();
        dto1.setNombre("Gastronomía");
        dto1.setDescripcion("Comidas típicas");

        CategoriaDto dto2 = new CategoriaDto();
        dto2.setNombre("Gastronomía");
        dto2.setDescripcion("Comidas típicas");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEquals() {
        CategoriaDto dto1 = new CategoriaDto();
        dto1.setNombre("Cultura");
        dto1.setDescripcion("Museos y artesanías");

        CategoriaDto dto2 = new CategoriaDto();
        dto2.setNombre("Naturaleza");
        dto2.setDescripcion("Bosques y lagos");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CategoriaDto dto = new CategoriaDto();
        dto.setNombre("Relajación");
        dto.setDescripcion("Spas y descansos");

        String str = dto.toString();

        assertTrue(str.contains("Relajación"));
        assertTrue(str.contains("Spas y descansos"));
        assertTrue(str.contains("CategoriaDto"));
    }

    @Test
    void testModificacionCampos() {
        CategoriaDto dto = new CategoriaDto();

        dto.setNombre("Original");
        dto.setDescripcion("Descripción original");

        dto.setNombre("Modificado");
        dto.setDescripcion("Nueva descripción");

        assertEquals("Modificado", dto.getNombre());
        assertEquals("Nueva descripción", dto.getDescripcion());
    }
}
