package pe.edu.upeu.turismospringboot.dto;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.FamiliaCategoriaDto;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FamiliaCategoriaDtoTest {

    @Test
    void testConstructorVacio() {
        FamiliaCategoriaDto dto = new FamiliaCategoriaDto();

        assertNull(dto.getIdFamiliaCategoria());
        assertNull(dto.getIdFamilia());
        assertNull(dto.getNombreFamilia());
        assertNull(dto.getIdCategoria());
        assertNull(dto.getNombreCategoria());
        assertNull(dto.getEmprendimientos());
        assertNull(dto.getFechaCreacionFamiliaCategoria());
        assertNull(dto.getFechaModificacionFamiliaCategoria());
    }

    @Test
    void testSettersAndGetters() {
        FamiliaCategoriaDto dto = new FamiliaCategoriaDto();

        LocalDateTime ahora = LocalDateTime.now();
        Emprendimiento emp = new Emprendimiento();

        dto.setIdFamiliaCategoria(1L);
        dto.setIdFamilia(2L);
        dto.setNombreFamilia("Familia Turismo");
        dto.setIdCategoria(3L);
        dto.setNombreCategoria("Aventura");
        dto.setEmprendimientos(List.of(emp));
        dto.setFechaCreacionFamiliaCategoria(ahora);
        dto.setFechaModificacionFamiliaCategoria(ahora.plusDays(1));

        assertEquals(1L, dto.getIdFamiliaCategoria());
        assertEquals(2L, dto.getIdFamilia());
        assertEquals("Familia Turismo", dto.getNombreFamilia());
        assertEquals(3L, dto.getIdCategoria());
        assertEquals("Aventura", dto.getNombreCategoria());
        assertEquals(1, dto.getEmprendimientos().size());
        assertEquals(ahora, dto.getFechaCreacionFamiliaCategoria());
        assertEquals(ahora.plusDays(1), dto.getFechaModificacionFamiliaCategoria());
    }

    @Test
    void testEqualsAndHashCode() {
        FamiliaCategoriaDto dto1 = new FamiliaCategoriaDto();
        dto1.setIdFamiliaCategoria(10L);
        dto1.setNombreFamilia("Cultura");

        FamiliaCategoriaDto dto2 = new FamiliaCategoriaDto();
        dto2.setIdFamiliaCategoria(10L);
        dto2.setNombreFamilia("Cultura");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEquals() {
        FamiliaCategoriaDto dto1 = new FamiliaCategoriaDto();
        dto1.setIdFamiliaCategoria(10L);

        FamiliaCategoriaDto dto2 = new FamiliaCategoriaDto();
        dto2.setIdFamiliaCategoria(20L);

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        FamiliaCategoriaDto dto = new FamiliaCategoriaDto();
        dto.setNombreCategoria("Gastronomía");

        String text = dto.toString();

        assertTrue(text.contains("Gastronomía"));
        assertTrue(text.contains("FamiliaCategoriaDto"));
    }

    @Test
    void testListaEmprendimientos() {
        FamiliaCategoriaDto dto = new FamiliaCategoriaDto();

        Emprendimiento e1 = new Emprendimiento();
        Emprendimiento e2 = new Emprendimiento();

        dto.setEmprendimientos(List.of(e1, e2));

        assertEquals(2, dto.getEmprendimientos().size());
    }

    @Test
    void testFechas() {
        FamiliaCategoriaDto dto = new FamiliaCategoriaDto();

        LocalDateTime now = LocalDateTime.now();
        dto.setFechaCreacionFamiliaCategoria(now);
        dto.setFechaModificacionFamiliaCategoria(now.plusHours(5));

        assertEquals(now, dto.getFechaCreacionFamiliaCategoria());
        assertEquals(now.plusHours(5), dto.getFechaModificacionFamiliaCategoria());
    }
}
