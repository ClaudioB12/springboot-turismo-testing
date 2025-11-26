package pe.edu.upeu.turismospringboot.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import pe.edu.upeu.turismospringboot.model.enums.EstadoReserva;

import static org.junit.jupiter.api.Assertions.*;

class EstadoReservaTest {

    @Test
    void testCantidadValores() {
        EstadoReserva[] valores = EstadoReserva.values();
        assertEquals(4, valores.length, "El enum debe tener 4 valores exactamente");
    }

    @Test
    void testOrdenValores() {
        EstadoReserva[] valores = EstadoReserva.values();

        assertEquals(EstadoReserva.PENDIENTE, valores[0]);
        assertEquals(EstadoReserva.CONFIRMADA, valores[1]);
        assertEquals(EstadoReserva.CANCELADA, valores[2]);
        assertEquals(EstadoReserva.RECHAZADA, valores[3]);
    }

    @ParameterizedTest
    @EnumSource(EstadoReserva.class)
    void testValoresNoSonNulos(EstadoReserva estado) {
        assertNotNull(estado);
    }

    @ParameterizedTest
    @EnumSource(EstadoReserva.class)
    void testToStringNoEsNulo(EstadoReserva estado) {
        assertNotNull(estado.toString());
    }

    @Test
    void testValueOf() {
        assertEquals(EstadoReserva.PENDIENTE, EstadoReserva.valueOf("PENDIENTE"));
        assertEquals(EstadoReserva.CONFIRMADA, EstadoReserva.valueOf("CONFIRMADA"));
        assertEquals(EstadoReserva.CANCELADA, EstadoReserva.valueOf("CANCELADA"));
        assertEquals(EstadoReserva.RECHAZADA, EstadoReserva.valueOf("RECHAZADA"));
    }

    @Test
    void testValueOfInvalido() {
        assertThrows(IllegalArgumentException.class, () -> EstadoReserva.valueOf("NO_EXISTE"));
    }
}
