package pe.edu.upeu.turismospringboot.entity;


import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.FamiliaCategoria;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FamiliaCategoriaTest {

    @Test
    void testGettersAndSetters() {
        FamiliaCategoria fc = new FamiliaCategoria();


        Familia familia = new Familia();
        Categoria categoria = new Categoria();
        Emprendimiento emprendimiento = new Emprendimiento();

        fc.setIdFamiliaCategoria(10L);
        fc.setFamilia(familia);
        fc.setCategoria(categoria);
        fc.setEmprendimientos(List.of(emprendimiento));

        assertEquals(10L, fc.getIdFamiliaCategoria());
        assertEquals(familia, fc.getFamilia());
        assertEquals(categoria, fc.getCategoria());
        assertEquals(1, fc.getEmprendimientos().size());
    }

    @Test
    void testPrePersistSetsCreationDate() {
        FamiliaCategoria fc = new FamiliaCategoria();
        assertNull(fc.getFechaCreacionFamiliaCategoria());

        fc.onCreate();

        assertNotNull(fc.getFechaCreacionFamiliaCategoria());
        assertTrue(fc.getFechaCreacionFamiliaCategoria().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdateSetsModificationDate() {
        FamiliaCategoria fc = new FamiliaCategoria();
        assertNull(fc.getFechaModificacionFamiliaCategoria());

        fc.onUpdate();

        assertNotNull(fc.getFechaModificacionFamiliaCategoria());
        assertTrue(fc.getFechaModificacionFamiliaCategoria().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testEqualsAndHashCode() {
        FamiliaCategoria fc1 = new FamiliaCategoria();
        fc1.setIdFamiliaCategoria(1L);

        FamiliaCategoria fc2 = new FamiliaCategoria();
        fc2.setIdFamiliaCategoria(1L);

        assertEquals(fc1, fc2);
        assertEquals(fc1.hashCode(), fc2.hashCode());
    }

    @Test
    void testNotEquals() {
        FamiliaCategoria fc1 = new FamiliaCategoria();
        fc1.setIdFamiliaCategoria(1L);

        FamiliaCategoria fc2 = new FamiliaCategoria();
        fc2.setIdFamiliaCategoria(2L);

        assertNotEquals(fc1, fc2);
    }

    @Test
    void testToStringContainsFields() {
        FamiliaCategoria fc = new FamiliaCategoria();
        fc.setIdFamiliaCategoria(3L);

        String result = fc.toString();

        assertTrue(result.contains("3"));
        assertTrue(result.contains("FamiliaCategoria"));
    }

    @Test
    void testEmprendimientosEmptyListAllowed() {
        FamiliaCategoria fc = new FamiliaCategoria();
        fc.setEmprendimientos(List.of());

        assertNotNull(fc.getEmprendimientos());
        assertTrue(fc.getEmprendimientos().isEmpty());
    }

    @Test
    void testRelationsCanBeNull() {
        FamiliaCategoria fc = new FamiliaCategoria();

        assertNull(fc.getFamilia());
        assertNull(fc.getCategoria());
        assertNull(fc.getEmprendimientos());
    }
}
