package pe.edu.upeu.turismospringboot.entity;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.model.entity.FamiliaCategoria;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaTest {

    @Test
    void testGettersAndSetters() {
        Categoria categoria = new Categoria();

        categoria.setIdCategoria(1L);
        categoria.setNombre("Turismo");
        categoria.setDescripcion("Descripción de prueba");
        categoria.setImagenUrl("imagen.png");

        FamiliaCategoria fc = new FamiliaCategoria();
        categoria.setFamiliaCategorias(List.of(fc));

        assertEquals(1L, categoria.getIdCategoria());
        assertEquals("Turismo", categoria.getNombre());
        assertEquals("Descripción de prueba", categoria.getDescripcion());
        assertEquals("imagen.png", categoria.getImagenUrl());
        assertEquals(1, categoria.getFamiliaCategorias().size());
    }

    @Test
    void testPrePersistSetsFechaCreacion() {
        Categoria categoria = new Categoria();
        assertNull(categoria.getFechaCreacionCategoria());

        categoria.onCreate();

        assertNotNull(categoria.getFechaCreacionCategoria());
        assertTrue(categoria.getFechaCreacionCategoria().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdateSetsFechaModificacion() {
        Categoria categoria = new Categoria();
        assertNull(categoria.getFechaModificacionCategoria());

        categoria.onUpdate();

        assertNotNull(categoria.getFechaModificacionCategoria());
        assertTrue(categoria.getFechaModificacionCategoria().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testEqualsAndHashCode() {
        Categoria c1 = new Categoria();
        c1.setIdCategoria(10L);

        Categoria c2 = new Categoria();
        c2.setIdCategoria(10L);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testNotEquals() {
        Categoria c1 = new Categoria();
        c1.setIdCategoria(1L);

        Categoria c2 = new Categoria();
        c2.setIdCategoria(2L);

        assertNotEquals(c1, c2);
    }

    @Test
    void testToStringContainsImportantFields() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Cultura");
        categoria.setDescripcion("Sitios históricos");

        String txt = categoria.toString();

        assertTrue(txt.contains("Cultura"));
        assertTrue(txt.contains("Sitios históricos"));
    }
}
