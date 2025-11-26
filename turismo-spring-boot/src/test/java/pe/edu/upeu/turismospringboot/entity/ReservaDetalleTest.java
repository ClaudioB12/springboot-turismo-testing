package pe.edu.upeu.turismospringboot.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Reserva;
import pe.edu.upeu.turismospringboot.model.entity.ReservaDetalle;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservaDetalleTest {

    @Test
    void debeAsignarYLeerAtributosBasicos() {
        ReservaDetalle detalle = new ReservaDetalle();

        detalle.setIdReservaDetalle(1L);
        detalle.setDescripcion("Habitación doble");
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(150.0);
        detalle.setTotal(300.0);
        detalle.setTipoServicio("habitacion");
        detalle.setObservaciones("Vista al mar");

        assertEquals(1L, detalle.getIdReservaDetalle());
        assertEquals("Habitación doble", detalle.getDescripcion());
        assertEquals(2, detalle.getCantidad());
        assertEquals(150.0, detalle.getPrecioUnitario());
        assertEquals(300.0, detalle.getTotal());
        assertEquals("habitacion", detalle.getTipoServicio());
        assertEquals("Vista al mar", detalle.getObservaciones());
    }

    @Test
    void debeAsignarReservaCorrectamente() {
        ReservaDetalle detalle = new ReservaDetalle();

        Reserva reserva = new Reserva();
        reserva.setIdReserva(10L);

        detalle.setReserva(reserva);

        assertNotNull(detalle.getReserva());
        assertEquals(10L, detalle.getReserva().getIdReserva());
    }

    @Test
    void debeEjecutarPrePersistCorrectamente() {
        ReservaDetalle detalle = new ReservaDetalle();
        detalle.onCreate();

        assertNotNull(detalle.getFechaCreacionReservaDetalle());
        assertTrue(detalle.getFechaCreacionReservaDetalle().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void debeEjecutarPreUpdateCorrectamente() {
        ReservaDetalle detalle = new ReservaDetalle();
        detalle.onUpdate();

        assertNotNull(detalle.getFechaModificacionReservaDetalle());
        assertTrue(detalle.getFechaModificacionReservaDetalle().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void debeSerializarSinReservaPorJsonBackReference() throws Exception {
        ReservaDetalle detalle = new ReservaDetalle();
        detalle.setIdReservaDetalle(33L);
        detalle.setDescripcion("Tour en kayak");
        detalle.setCantidad(1);

        Reserva reserva = new Reserva();
        reserva.setIdReserva(99L);

        detalle.setReserva(reserva);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(detalle);

        // Debe incluir los atributos propios
        assertTrue(json.contains("Tour en kayak"));
        assertTrue(json.contains("idReservaDetalle"));

        // NO debe aparecer la reserva por JsonBackReference
        assertFalse(json.contains("reserva"));
    }
}
