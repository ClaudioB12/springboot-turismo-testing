package pe.edu.upeu.turismospringboot.entity;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.model.entity.ServicioTuristico;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ServicioTuristicoTest {

    @Test
    void testCrearServicioTuristico() {
        ServicioTuristico s = new ServicioTuristico();

        s.setNombre("Habitación doble");
        s.setDescripcion("Bonita habitación con vista al lago");
        s.setPrecioUnitario(120.50);
        s.setTipoServicio("Alojamiento");
        s.setImagenUrl("img1.jpg");

        assertEquals("Habitación doble", s.getNombre());
        assertEquals("Bonita habitación con vista al lago", s.getDescripcion());
        assertEquals(120.50, s.getPrecioUnitario());
        assertEquals("Alojamiento", s.getTipoServicio());
        assertEquals("img1.jpg", s.getImagenUrl());
    }

    @Test
    void testPrePersist() {
        ServicioTuristico s = new ServicioTuristico();
        s.onCreate();

        assertNotNull(s.getFechaCreacion());
    }

    @Test
    void testPreUpdate() {
        ServicioTuristico s = new ServicioTuristico();
        s.onUpdate();

        assertNotNull(s.getFechaModificacion());
    }

    @Test
    void testRelacionEmprendimiento() {
        ServicioTuristico s = new ServicioTuristico();
        Emprendimiento e = new Emprendimiento();

        s.setEmprendimiento(e);

        assertNotNull(s.getEmprendimiento());
        assertEquals(e, s.getEmprendimiento());
    }
}
