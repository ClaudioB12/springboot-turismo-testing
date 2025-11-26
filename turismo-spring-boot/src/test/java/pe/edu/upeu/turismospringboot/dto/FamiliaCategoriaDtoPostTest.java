package pe.edu.upeu.turismospringboot.dto;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.FamiliaCategoriaDtoPost;

import static org.junit.jupiter.api.Assertions.*;

class FamiliaCategoriaDtoPostTest {

    @Test
    void testConstructorVacio() {
        FamiliaCategoriaDtoPost dto = new FamiliaCategoriaDtoPost();

        assertNull(dto.getIdFamilia());
        assertNull(dto.getIdCategoria());
    }

    @Test
    void testSettersAndGetters() {
        FamiliaCategoriaDtoPost dto = new FamiliaCategoriaDtoPost();

        dto.setIdFamilia(5L);
        dto.setIdCategoria(8L);

        assertEquals(5L, dto.getIdFamilia());
        assertEquals(8L, dto.getIdCategoria());
    }

    @Test
    void testEqualsAndHashCode() {
        FamiliaCategoriaDtoPost dto1 = new FamiliaCategoriaDtoPost();
        dto1.setIdFamilia(1L);
        dto1.setIdCategoria(2L);

        FamiliaCategoriaDtoPost dto2 = new FamiliaCategoriaDtoPost();
        dto2.setIdFamilia(1L);
        dto2.setIdCategoria(2L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEquals() {
        FamiliaCategoriaDtoPost dto1 = new FamiliaCategoriaDtoPost();
        dto1.setIdFamilia(1L);
        dto1.setIdCategoria(2L);

        FamiliaCategoriaDtoPost dto2 = new FamiliaCategoriaDtoPost();
        dto2.setIdFamilia(3L);
        dto2.setIdCategoria(4L);

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        FamiliaCategoriaDtoPost dto = new FamiliaCategoriaDtoPost();
        dto.setIdFamilia(10L);
        dto.setIdCategoria(20L);

        String toString = dto.toString();

        assertTrue(toString.contains("10"));
        assertTrue(toString.contains("20"));
        assertTrue(toString.contains("FamiliaCategoriaDtoPost"));
    }
}
