package pe.edu.upeu.turismospringboot.dto;


import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.ChatResumenDto;
import pe.edu.upeu.turismospringboot.model.enums.EstadoMensaje;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ChatResumenDtoTest {

    @Test
    void testConstructorVacio() {
        ChatResumenDto dto = new ChatResumenDto();

        assertNull(dto.getUsername());
        assertNull(dto.getNombreCompleto());
        assertNull(dto.getUltimoMensaje());
        assertNull(dto.getHora());
        assertNull(dto.getAvatarUrl());
        assertNull(dto.getEstadoUltimoMensaje());
    }

    @Test
    void testSettersAndGetters() {
        ChatResumenDto dto = new ChatResumenDto();
        LocalDateTime fecha = LocalDateTime.now();

        dto.setUsername("claudio");
        dto.setNombreCompleto("Claudio Bustinza");
        dto.setUltimoMensaje("Hola, ¿cómo estás?");
        dto.setHora(fecha);
        dto.setAvatarUrl("foto.png");
        dto.setEstadoUltimoMensaje(EstadoMensaje.ENVIADO);

        assertEquals("claudio", dto.getUsername());
        assertEquals("Claudio Bustinza", dto.getNombreCompleto());
        assertEquals("Hola, ¿cómo estás?", dto.getUltimoMensaje());
        assertEquals(fecha, dto.getHora());
        assertEquals("foto.png", dto.getAvatarUrl());
        assertEquals(EstadoMensaje.ENVIADO, dto.getEstadoUltimoMensaje());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime fecha = LocalDateTime.now();

        ChatResumenDto dto1 = new ChatResumenDto();
        dto1.setUsername("usuario1");
        dto1.setNombreCompleto("Usuario Uno");
        dto1.setUltimoMensaje("Hola");
        dto1.setHora(fecha);
        dto1.setAvatarUrl("avatar1.png");
        dto1.setEstadoUltimoMensaje(EstadoMensaje.LEIDO);

        ChatResumenDto dto2 = new ChatResumenDto();
        dto2.setUsername("usuario1");
        dto2.setNombreCompleto("Usuario Uno");
        dto2.setUltimoMensaje("Hola");
        dto2.setHora(fecha);
        dto2.setAvatarUrl("avatar1.png");
        dto2.setEstadoUltimoMensaje(EstadoMensaje.LEIDO);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEquals() {
        ChatResumenDto dto1 = new ChatResumenDto();
        dto1.setUsername("userA");

        ChatResumenDto dto2 = new ChatResumenDto();
        dto2.setUsername("userB");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ChatResumenDto dto = new ChatResumenDto();
        dto.setUsername("alex");
        dto.setUltimoMensaje("Probando");

        String str = dto.toString();

        assertTrue(str.contains("alex"));
        assertTrue(str.contains("Probando"));
        assertTrue(str.contains("ChatResumenDto"));
    }

    @Test
    void testModificacionCampos() {
        ChatResumenDto dto = new ChatResumenDto();

        dto.setUltimoMensaje("Mensaje inicial");
        dto.setEstadoUltimoMensaje(EstadoMensaje.ENVIADO);

        dto.setUltimoMensaje("Mensaje modificado");
        dto.setEstadoUltimoMensaje(EstadoMensaje.ENTREGADO);

        assertEquals("Mensaje modificado", dto.getUltimoMensaje());
        assertEquals(EstadoMensaje.ENTREGADO, dto.getEstadoUltimoMensaje());
    }
}
