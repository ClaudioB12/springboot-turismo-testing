package pe.edu.upeu.turismospringboot.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import pe.edu.upeu.turismospringboot.model.enums.TipoMensaje;

import static org.junit.jupiter.api.Assertions.*;

class TipoMensajeTest {

    @Test
    void testCantidadValores() {
        TipoMensaje[] valores = TipoMensaje.values();
        assertEquals(7, valores.length, "El enum debe tener 7 valores exactamente");
    }

    @Test
    void testOrdenValores() {
        TipoMensaje[] v = TipoMensaje.values();

        assertEquals(TipoMensaje.TEXTO, v[0]);
        assertEquals(TipoMensaje.DOCUMENTO, v[1]);
        assertEquals(TipoMensaje.AUDIO, v[2]);
        assertEquals(TipoMensaje.IMAGEN, v[3]);
        assertEquals(TipoMensaje.VIDEO, v[4]);
        assertEquals(TipoMensaje.EMOJI, v[5]);
        assertEquals(TipoMensaje.TEXTO_ARCHIVO, v[6]);
    }

    @ParameterizedTest
    @EnumSource(TipoMensaje.class)
    void testValoresNoNulos(TipoMensaje tipo) {
        assertNotNull(tipo);
    }

    @ParameterizedTest
    @EnumSource(TipoMensaje.class)
    void testToStringNoEsNulo(TipoMensaje tipo) {
        assertNotNull(tipo.toString());
    }

    @Test
    void testValueOf() {
        assertEquals(TipoMensaje.TEXTO, TipoMensaje.valueOf("TEXTO"));
        assertEquals(TipoMensaje.DOCUMENTO, TipoMensaje.valueOf("DOCUMENTO"));
        assertEquals(TipoMensaje.AUDIO, TipoMensaje.valueOf("AUDIO"));
        assertEquals(TipoMensaje.IMAGEN, TipoMensaje.valueOf("IMAGEN"));
        assertEquals(TipoMensaje.VIDEO, TipoMensaje.valueOf("VIDEO"));
        assertEquals(TipoMensaje.EMOJI, TipoMensaje.valueOf("EMOJI"));
        assertEquals(TipoMensaje.TEXTO_ARCHIVO, TipoMensaje.valueOf("TEXTO_ARCHIVO"));
    }

    @Test
    void testValueOfInvalido() {
        assertThrows(IllegalArgumentException.class, () -> TipoMensaje.valueOf("NO_EXISTE"));
    }
}
