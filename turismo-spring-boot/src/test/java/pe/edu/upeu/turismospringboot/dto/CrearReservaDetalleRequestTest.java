package pe.edu.upeu.turismospringboot.dto;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.CrearReservaDetalleRequest;

import static org.junit.jupiter.api.Assertions.*;

class CrearReservaDetalleRequestTest {

    @Test
    void testConstructorVacio() {
        CrearReservaDetalleRequest dto = new CrearReservaDetalleRequest();

        assertNull(dto.getIdServicioTuristico());
        assertNull(dto.getCantidad());
        assertNull(dto.getObservaciones());
    }

    @Test
    void testSettersAndGetters() {
        CrearReservaDetalleRequest dto = new CrearReservaDetalleRequest();

        dto.setIdServicioTuristico(10L);
        dto.setCantidad(5);
        dto.setObservaciones("Requiere guía bilingüe");

        assertEquals(10L, dto.getIdServicioTuristico());
        assertEquals(5, dto.getCantidad());
        assertEquals("Requiere guía bilingüe", dto.getObservaciones());
    }

    @Test
    void testEqualsAndHashCode() {
        CrearReservaDetalleRequest dto1 = new CrearReservaDetalleRequest();
        dto1.setIdServicioTuristico(1L);
        dto1.setCantidad(2);
        dto1.setObservaciones("Notas A");

        CrearReservaDetalleRequest dto2 = new CrearReservaDetalleRequest();
        dto2.setIdServicioTuristico(1L);
        dto2.setCantidad(2);
        dto2.setObservaciones("Notas A");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEquals() {
        CrearReservaDetalleRequest dto1 = new CrearReservaDetalleRequest();
        dto1.setIdServicioTuristico(1L);

        CrearReservaDetalleRequest dto2 = new CrearReservaDetalleRequest();
        dto2.setIdServicioTuristico(2L);

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CrearReservaDetalleRequest dto = new CrearReservaDetalleRequest();
        dto.setIdServicioTuristico(99L);
        dto.setObservaciones("Algo");

        String text = dto.toString();

        assertTrue(text.contains("99"));
        assertTrue(text.contains("Algo"));
        assertTrue(text.contains("CrearReservaDetalleRequest"));
    }

    @Test
    void testModificarCampos() {
        CrearReservaDetalleRequest dto = new CrearReservaDetalleRequest();

        dto.setCantidad(1);
        assertEquals(1, dto.getCantidad());

        dto.setCantidad(3);
        assertEquals(3, dto.getCantidad());
    }
}
