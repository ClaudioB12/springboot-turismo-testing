package pe.edu.upeu.turismospringboot.enums;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.enums.EstadoMensaje;

import static org.junit.jupiter.api.Assertions.*;

class EstadoMensajeTest {

    @Test
    void testCantidadDeEstados() {
        EstadoMensaje[] values = EstadoMensaje.values();
        assertEquals(5, values.length);
    }

    @Test
    void testOrdenDeEstados() {
        assertEquals(EstadoMensaje.PENDIENTE, EstadoMensaje.values()[0]);
        assertEquals(EstadoMensaje.ENVIADO, EstadoMensaje.values()[1]);
        assertEquals(EstadoMensaje.ENTREGADO, EstadoMensaje.values()[2]);
        assertEquals(EstadoMensaje.LEIDO, EstadoMensaje.values()[3]);
        assertEquals(EstadoMensaje.ERROR_ENVIO, EstadoMensaje.values()[4]);
    }

    @Test
    void testValueOfValido() {
        assertEquals(EstadoMensaje.PENDIENTE, EstadoMensaje.valueOf("PENDIENTE"));
        assertEquals(EstadoMensaje.ENVIADO, EstadoMensaje.valueOf("ENVIADO"));
        assertEquals(EstadoMensaje.ENTREGADO, EstadoMensaje.valueOf("ENTREGADO"));
        assertEquals(EstadoMensaje.LEIDO, EstadoMensaje.valueOf("LEIDO"));
        assertEquals(EstadoMensaje.ERROR_ENVIO, EstadoMensaje.valueOf("ERROR_ENVIO"));
    }

    @Test
    void testValueOfInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            EstadoMensaje.valueOf("NO_EXISTE");
        });
    }

    @Test
    void testToString() {
        assertEquals("PENDIENTE", EstadoMensaje.PENDIENTE.toString());
        assertEquals("ENVIADO", EstadoMensaje.ENVIADO.toString());
        assertEquals("ENTREGADO", EstadoMensaje.ENTREGADO.toString());
        assertEquals("LEIDO", EstadoMensaje.LEIDO.toString());
        assertEquals("ERROR_ENVIO", EstadoMensaje.ERROR_ENVIO.toString());
    }
}
