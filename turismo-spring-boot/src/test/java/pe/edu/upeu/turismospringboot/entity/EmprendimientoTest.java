package pe.edu.upeu.turismospringboot.entity;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmprendimientoTest {

    @Test
    void testGettersAndSetters() {
        Emprendimiento emp = new Emprendimiento();

        FamiliaCategoria familiaCategoria = new FamiliaCategoria();
        Usuario usuario = new Usuario();
        Reserva reserva = new Reserva();
        ServicioTuristico servicio = new ServicioTuristico();

        emp.setIdEmprendimiento(1L);
        emp.setNombre("Emprendimiento Test");
        emp.setDescripcion("Descripción del emprendimiento");
        emp.setImagenUrl("imagen.jpg");
        emp.setLatitud(-15.12345);
        emp.setLongitud(-70.56789);
        emp.setFamiliaCategoria(familiaCategoria);
        emp.setUsuario(usuario);
        emp.setReservas(List.of(reserva));
        emp.setServicioTuristicos(List.of(servicio));

        assertEquals(1L, emp.getIdEmprendimiento());
        assertEquals("Emprendimiento Test", emp.getNombre());
        assertEquals("Descripción del emprendimiento", emp.getDescripcion());
        assertEquals("imagen.jpg", emp.getImagenUrl());
        assertEquals(-15.12345, emp.getLatitud());
        assertEquals(-70.56789, emp.getLongitud());
        assertEquals(familiaCategoria, emp.getFamiliaCategoria());
        assertEquals(usuario, emp.getUsuario());
        assertEquals(1, emp.getReservas().size());
        assertEquals(1, emp.getServicioTuristicos().size());
    }

    @Test
    void testPrePersistSetsFechaCreacion() {
        Emprendimiento emp = new Emprendimiento();
        assertNull(emp.getFechaCreacionEmprendimiento());

        emp.onCreate();

        assertNotNull(emp.getFechaCreacionEmprendimiento());
        assertTrue(emp.getFechaCreacionEmprendimiento().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdateSetsFechaModificacion() {
        Emprendimiento emp = new Emprendimiento();
        assertNull(emp.getFechaModificacionEmprendimiento());

        emp.onUpdate();

        assertNotNull(emp.getFechaModificacionEmprendimiento());
        assertTrue(emp.getFechaModificacionEmprendimiento().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testEqualsAndHashCode() {
        Emprendimiento e1 = new Emprendimiento();
        e1.setIdEmprendimiento(5L);

        Emprendimiento e2 = new Emprendimiento();
        e2.setIdEmprendimiento(5L);

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void testNotEquals() {
        Emprendimiento e1 = new Emprendimiento();
        e1.setIdEmprendimiento(1L);

        Emprendimiento e2 = new Emprendimiento();
        e2.setIdEmprendimiento(2L);

        assertNotEquals(e1, e2);
    }

    @Test
    void testToStringContainsImportantFields() {
        Emprendimiento emp = new Emprendimiento();
        emp.setNombre("Turismo Lago");
        emp.setDescripcion("Visita guiada");

        String text = emp.toString();

        assertTrue(text.contains("Turismo Lago"));
        assertTrue(text.contains("Visita guiada"));
    }

    @Test
    void testEmptyListsAreAllowed() {
        Emprendimiento emp = new Emprendimiento();
        emp.setReservas(List.of());
        emp.setServicioTuristicos(List.of());

        assertNotNull(emp.getReservas());
        assertNotNull(emp.getServicioTuristicos());
        assertEquals(0, emp.getReservas().size());
        assertEquals(0, emp.getServicioTuristicos().size());
    }

    @Test
    void testRelationsCanBeNull() {
        Emprendimiento emp = new Emprendimiento();

        assertNull(emp.getUsuario());
        assertNull(emp.getFamiliaCategoria());
        assertNull(emp.getReservas());
        assertNull(emp.getServicioTuristicos());
    }
}
