package pe.edu.upeu.turismospringboot.entity;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Mensaje;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.model.enums.EstadoMensaje;
import pe.edu.upeu.turismospringboot.model.enums.TipoMensaje;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MensajeTest {

    @Test
    void testGettersAndSetters() {
        Mensaje mensaje = new Mensaje();

        Usuario emisor = new Usuario();
        Usuario receptor = new Usuario();
        LocalDateTime now = LocalDateTime.now();

        mensaje.setId(1L);
        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);
        mensaje.setContenidoTexto("Hola mundo");
        mensaje.setContenidoArchivo("archivo.png");
        mensaje.setTipo(TipoMensaje.IMAGEN);
        mensaje.setEstado(EstadoMensaje.ENTREGADO);
        mensaje.setEditado(true);
        mensaje.setFechaEnvio(now);

        assertEquals(1L, mensaje.getId());
        assertEquals(emisor, mensaje.getEmisor());
        assertEquals(receptor, mensaje.getReceptor());
        assertEquals("Hola mundo", mensaje.getContenidoTexto());
        assertEquals("archivo.png", mensaje.getContenidoArchivo());
        assertEquals(TipoMensaje.IMAGEN, mensaje.getTipo());
        assertEquals(EstadoMensaje.ENTREGADO, mensaje.getEstado());
        assertTrue(mensaje.isEditado());
        assertEquals(now, mensaje.getFechaEnvio());
    }

    @Test
    void testDefaultEstadoEsEnviado() {
        Mensaje mensaje = new Mensaje();
        assertEquals(EstadoMensaje.ENVIADO, mensaje.getEstado());
    }

    @Test
    void testDefaultEditadoEsFalse() {
        Mensaje mensaje = new Mensaje();
        assertFalse(mensaje.isEditado());
    }

    @Test
    void testEqualsAndHashCode() {
        Mensaje m1 = new Mensaje();
        m1.setId(5L);

        Mensaje m2 = new Mensaje();
        m2.setId(5L);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void testNotEquals() {
        Mensaje m1 = new Mensaje();
        m1.setId(1L);

        Mensaje m2 = new Mensaje();
        m2.setId(2L);

        assertNotEquals(m1, m2);
    }

    @Test
    void testToStringContainsFields() {
        Mensaje mensaje = new Mensaje();
        mensaje.setContenidoTexto("Prueba");

        String result = mensaje.toString();

        assertTrue(result.contains("Prueba")); // contenidoTexto
        assertTrue(result.contains("Mensaje")); // nombre de clase
    }

    @Test
    void testNullableFieldsAllowed() {
        Mensaje mensaje = new Mensaje();

        mensaje.setContenidoTexto(null);
        mensaje.setContenidoArchivo(null);
        mensaje.setFechaEnvio(null);
        mensaje.setEmisor(null);
        mensaje.setReceptor(null);

        assertNull(mensaje.getContenidoTexto());
        assertNull(mensaje.getContenidoArchivo());
        assertNull(mensaje.getFechaEnvio());
        assertNull(mensaje.getEmisor());
        assertNull(mensaje.getReceptor());
    }

    @Test
    void testEnumsAreAssignable() {
        Mensaje mensaje = new Mensaje();

        mensaje.setTipo(TipoMensaje.TEXTO);
        mensaje.setEstado(EstadoMensaje.LEIDO);

        assertEquals(TipoMensaje.TEXTO, mensaje.getTipo());
        assertEquals(EstadoMensaje.LEIDO, mensaje.getEstado());
    }
}
