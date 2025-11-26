package pe.edu.upeu.turismospringboot.service.impl;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.upeu.turismospringboot.model.dto.ChatResumenDto;
import pe.edu.upeu.turismospringboot.model.dto.MensajeDto;
import pe.edu.upeu.turismospringboot.model.entity.Mensaje;
import pe.edu.upeu.turismospringboot.model.entity.Persona;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.model.enums.EstadoMensaje;
import pe.edu.upeu.turismospringboot.model.enums.TipoMensaje;
import pe.edu.upeu.turismospringboot.repository.MensajeRepository;
import pe.edu.upeu.turismospringboot.repository.UsuarioRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MensajeServiceImplTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MensajeServiceImpl service;

    private Usuario u1, u2;
    private Mensaje m1, m2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // ========= Usuarios =========
        u1 = new Usuario();
        u1.setIdUsuario(1L);
        u1.setUsername("carlos");

        u2 = new Usuario();
        u2.setIdUsuario(2L);
        u2.setUsername("maria");

        // ========= Mensajes =========
        m1 = new Mensaje();
        m1.setId(10L);
        m1.setEmisor(u1);
        m1.setReceptor(u2);
        m1.setContenidoTexto("Hola");
        m1.setTipo(TipoMensaje.TEXTO);
        m1.setEstado(EstadoMensaje.ENVIADO);
        m1.setFechaEnvio(LocalDateTime.now().minusMinutes(5));

        m2 = new Mensaje();
        m2.setId(11L);
        m2.setEmisor(u2);
        m2.setReceptor(u1);
        m2.setContenidoTexto("¿Qué tal?");
        m2.setTipo(TipoMensaje.TEXTO);
        m2.setEstado(EstadoMensaje.LEIDO);
        m2.setFechaEnvio(LocalDateTime.now());
    }

    // ============================================================
    // obtenerHistorialEntre
    // ============================================================

    @Test
    void testObtenerHistorialEntre_Exito() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(u2));
        when(mensajeRepository
                .findByEmisor_IdUsuarioAndReceptor_IdUsuarioOrEmisor_IdUsuarioAndReceptor_IdUsuarioOrderByFechaEnvioAsc(
                        1L, 2L, 2L, 1L
                )
        ).thenReturn(List.of(m1, m2));

        List<MensajeDto> historial = service.obtenerHistorialEntre(u1, 2L);

        assertEquals(2, historial.size());
        assertEquals("Hola", historial.get(0).getContenidoTexto());
        assertEquals("¿Qué tal?", historial.get(1).getContenidoTexto());
    }

    @Test
    void testObtenerHistorialEntre_UsuarioReceptorNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.obtenerHistorialEntre(u1, 99L)
        );
    }

    @Test
    void testObtenerHistorialEntre_NoAutorizado() {
        // Mensajes NO relacionados con el user autenticado
        Usuario otro = new Usuario();
        otro.setIdUsuario(99L);

        Mensaje mNoAuth = new Mensaje();
        mNoAuth.setEmisor(otro);
        mNoAuth.setReceptor(otro);

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(u2));
        when(mensajeRepository
                .findByEmisor_IdUsuarioAndReceptor_IdUsuarioOrEmisor_IdUsuarioAndReceptor_IdUsuarioOrderByFechaEnvioAsc(
                        1L, 2L, 2L, 1L
                )
        ).thenReturn(List.of(mNoAuth));

        assertThrows(
                SecurityException.class,
                () -> service.obtenerHistorialEntre(u1, 2L)
        );
    }

    // ============================================================
    // obtenerChatsRecientes
    // ============================================================

    @Test
    void testObtenerChatsRecientes_ConPersona() {
        // Persona con nombres
        Persona personaMaria = new Persona();
        personaMaria.setNombres("María");
        personaMaria.setApellidos("Gomez");
        personaMaria.setFotoPerfil("avatar.png");
        u2.setPersona(personaMaria);

        when(mensajeRepository.findMensajesRecientesPorUsuario(1L))
                .thenReturn(List.of(m2, m1)); // m2 es el más reciente

        List<ChatResumenDto> chats = service.obtenerChatsRecientes(1L);

        assertEquals(1, chats.size());

        ChatResumenDto dto = chats.get(0);

        assertEquals("maria", dto.getUsername());
        assertEquals("María Gomez", dto.getNombreCompleto());
        assertEquals("¿Qué tal?", dto.getUltimoMensaje());
        assertEquals("avatar.png", dto.getAvatarUrl());
        assertEquals(EstadoMensaje.LEIDO, dto.getEstadoUltimoMensaje());
    }

    @Test
    void testObtenerChatsRecientes_SinPersona() {
        u2.setPersona(null);

        when(mensajeRepository.findMensajesRecientesPorUsuario(1L))
                .thenReturn(List.of(m2));

        List<ChatResumenDto> chats = service.obtenerChatsRecientes(1L);

        ChatResumenDto dto = chats.get(0);

        assertEquals("maria", dto.getUsername());
        assertEquals("maria", dto.getNombreCompleto()); // fallback
        assertNull(dto.getAvatarUrl());
    }

    @Test
    void testObtenerChatsRecientes_ArchivoEnLugarDeTexto() {
        m2.setContenidoTexto(null);
        m2.setContenidoArchivo("archivo.pdf");

        when(mensajeRepository.findMensajesRecientesPorUsuario(1L))
                .thenReturn(List.of(m2));

        List<ChatResumenDto> chats = service.obtenerChatsRecientes(1L);

        assertEquals("[Archivo]", chats.get(0).getUltimoMensaje());
    }

    @Test
    void testObtenerChatsRecientes_AgrupacionConversaciones() {
        // Simula mensajes de dos conversaciones distintas
        Usuario u3 = new Usuario();
        u3.setIdUsuario(3L);
        u3.setUsername("juan");

        Mensaje m3 = new Mensaje();
        m3.setEmisor(u3);
        m3.setReceptor(u1);
        m3.setContenidoTexto("Hola soy Juan");
        m3.setFechaEnvio(LocalDateTime.now());

        when(mensajeRepository.findMensajesRecientesPorUsuario(1L))
                .thenReturn(List.of(m2, m3)); // dos conversaciones

        List<ChatResumenDto> chats = service.obtenerChatsRecientes(1L);

        assertEquals(2, chats.size()); // 2 conversaciones diferentes
    }
}
