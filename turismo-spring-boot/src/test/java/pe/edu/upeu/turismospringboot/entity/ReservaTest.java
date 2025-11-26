package pe.edu.upeu.turismospringboot.entity;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.model.entity.Reserva;
import pe.edu.upeu.turismospringboot.model.entity.ReservaDetalle;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.model.enums.EstadoReserva;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    @Test
    void testGettersAndSetters() {
        Reserva reserva = new Reserva();

        Usuario usuario = new Usuario();
        Emprendimiento emp = new Emprendimiento();
        List<ReservaDetalle> detalles = new ArrayList<>();

        LocalDateTime fechaReserva = LocalDateTime.now();
        LocalDateTime inicio = fechaReserva.plusHours(1);
        LocalDateTime fin = inicio.plusHours(2);

        reserva.setIdReserva(1L);
        reserva.setFechaHoraReserva(fechaReserva);
        reserva.setFechaHoraInicio(inicio);
        reserva.setFechaHoraFin(fin);
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setTotalGeneral(150.0);
        reserva.setUsuario(usuario);
        reserva.setEmprendimiento(emp);
        reserva.setReservaDetalles(detalles);

        assertEquals(1L, reserva.getIdReserva());
        assertEquals(fechaReserva, reserva.getFechaHoraReserva());
        assertEquals(inicio, reserva.getFechaHoraInicio());
        assertEquals(fin, reserva.getFechaHoraFin());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        assertEquals(150.0, reserva.getTotalGeneral());
        assertEquals(usuario, reserva.getUsuario());
        assertEquals(emp, reserva.getEmprendimiento());
        assertEquals(detalles, reserva.getReservaDetalles());
    }

    @Test
    void testPrePersistSetsCreationDate() {
        Reserva reserva = new Reserva();
        assertNull(reserva.getFechaCreacionReserva());

        reserva.onCreate();

        assertNotNull(reserva.getFechaCreacionReserva());
        assertTrue(reserva.getFechaCreacionReserva().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdateSetsModificationDate() {
        Reserva reserva = new Reserva();
        assertNull(reserva.getFechaModificacionReserva());

        reserva.onUpdate();

        assertNotNull(reserva.getFechaModificacionReserva());
        assertTrue(reserva.getFechaModificacionReserva().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testNullableFields() {
        Reserva reserva = new Reserva();

        reserva.setTotalGeneral(null);
        reserva.setReservaDetalles(null);

        assertNull(reserva.getTotalGeneral());
        assertNull(reserva.getReservaDetalles());
    }

    @Test
    void testEstadoReservaValues() {
        Reserva reserva = new Reserva();

        reserva.setEstado(EstadoReserva.CONFIRMADA);
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());

        reserva.setEstado(EstadoReserva.CANCELADA);
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
    }

    @Test
    void testEqualsAndHashCode() {
        Reserva r1 = new Reserva();
        r1.setIdReserva(5L);

        Reserva r2 = new Reserva();
        r2.setIdReserva(5L);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testNotEquals() {
        Reserva r1 = new Reserva();
        r1.setIdReserva(1L);

        Reserva r2 = new Reserva();
        r2.setIdReserva(2L);

        assertNotEquals(r1, r2);
    }

    @Test
    void testReservaDetallesList() {
        Reserva reserva = new Reserva();

        ReservaDetalle d1 = new ReservaDetalle();
        ReservaDetalle d2 = new ReservaDetalle();

        List<ReservaDetalle> lista = new ArrayList<>();
        lista.add(d1);
        lista.add(d2);

        reserva.setReservaDetalles(lista);

        assertEquals(2, reserva.getReservaDetalles().size());
    }

    @Test
    void testToStringContainsFields() {
        Reserva reserva = new Reserva();
        reserva.setEstado(EstadoReserva.PENDIENTE);

        String str = reserva.toString();

        assertTrue(str.contains("PENDIENTE"));
        assertTrue(str.contains("Reserva"));
    }
}
