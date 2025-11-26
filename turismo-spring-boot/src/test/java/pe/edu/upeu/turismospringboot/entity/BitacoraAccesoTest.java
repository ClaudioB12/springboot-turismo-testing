package pe.edu.upeu.turismospringboot.entity;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.BitacoraAcceso;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BitacoraAccesoTest {

    @Test
    void testGettersAndSetters() {
        BitacoraAcceso bitacora = new BitacoraAcceso();
        Usuario usuario = new Usuario();

        bitacora.setIdBitacora(1L);
        bitacora.setUsuario(usuario);
        bitacora.setFechaHora(LocalDateTime.of(2025, 1, 20, 10, 30));
        bitacora.setDireccionIp("192.168.1.10");
        bitacora.setExito(true);

        assertEquals(1L, bitacora.getIdBitacora());
        assertEquals(usuario, bitacora.getUsuario());
        assertEquals(LocalDateTime.of(2025, 1, 20, 10, 30), bitacora.getFechaHora());
        assertEquals("192.168.1.10", bitacora.getDireccionIp());
        assertTrue(bitacora.isExito());
    }

    @Test
    void testPrePersistSetsFechaCreacion() {
        BitacoraAcceso bitacora = new BitacoraAcceso();

        assertNull(bitacora.getFechaCreacionBitacoraAcceso());

        bitacora.onCreate();

        assertNotNull(bitacora.getFechaCreacionBitacoraAcceso());
        assertTrue(bitacora.getFechaCreacionBitacoraAcceso().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdateSetsFechaModificacion() {
        BitacoraAcceso bitacora = new BitacoraAcceso();

        assertNull(bitacora.getFechaModificacionBitacoraAcceso());

        bitacora.onUpdate();

        assertNotNull(bitacora.getFechaModificacionBitacoraAcceso());
        assertTrue(bitacora.getFechaModificacionBitacoraAcceso().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testEqualsAndHashCode() {
        BitacoraAcceso b1 = new BitacoraAcceso();
        b1.setIdBitacora(10L);

        BitacoraAcceso b2 = new BitacoraAcceso();
        b2.setIdBitacora(10L);

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void testNotEquals() {
        BitacoraAcceso b1 = new BitacoraAcceso();
        b1.setIdBitacora(1L);

        BitacoraAcceso b2 = new BitacoraAcceso();
        b2.setIdBitacora(2L);

        assertNotEquals(b1, b2);
    }

    @Test
    void testToStringContainsImportantFields() {
        BitacoraAcceso bitacora = new BitacoraAcceso();
        bitacora.setDireccionIp("10.0.0.1");
        bitacora.setExito(false);

        String txt = bitacora.toString();

        assertTrue(txt.contains("10.0.0.1"));
        assertTrue(txt.contains("false"));
    }
}
