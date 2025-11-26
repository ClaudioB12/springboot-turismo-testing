package pe.edu.upeu.turismospringboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import pe.edu.upeu.turismospringboot.service.impl.MensajeServiceImpl;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MensajeServiceTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MensajeServiceImpl mensajeService;

    private Usuario usuarioAuth;
    private Usuario usuarioDestino;
    private Mensaje mensaje1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuarioAuth = new Usuario();
        usuarioAuth.setIdUsuario(1L);
        usuarioAuth.setUsername("user1");

        usuarioDestino = new Usuario();
        usuarioDestino.setIdUsuario(2L);
        usuarioDestino.setUsername("user2");

        mensaje1 = new Mensaje();
        mensaje1.setId(10L);
        mensaje1.setEmisor(usuarioAuth);
        mensaje1.setReceptor(usuarioDestino);
        mensaje1.setContenidoTexto("Hola");
        mensaje1.setTipo(TipoMensaje.TEXTO);
        mensaje1.setEstado(EstadoMensaje.ENVIADO);
        mensaje1.setFechaEnvio(LocalDateTime.now());
    }

    // ============================================================
    // obtenerHistorialEntre()
    // ============================================================

    @Test
    void testObtenerHistorialEntre_Exitoso() {
        when(usuarioRepository.findById(2L))
                .thenReturn(Optional.of(usuarioDestino));

        when(mensajeRepository
                .findByEmisor_IdUsuarioAndReceptor_IdUsuarioOrEmisor_IdUsuarioAndReceptor_IdUsuarioOrderByFechaEnvioAsc(
                        1L, 2L, 2L, 1L
                ))
                .thenReturn(List.of(mensaje1));

        List<MensajeDto> result =
                mensajeService.obtenerHistorialEntre(usuarioAuth, 2L);

        assertEquals(1, result.size());
        assertEquals("Hola", result.get(0).getContenidoTexto());
        verify(mensajeRepository).findByEmisor_IdUsuarioAndReceptor_IdUsuarioOrEmisor_IdUsuarioAndReceptor_IdUsuarioOrderByFechaEnvioAsc(
                1L, 2L, 2L, 1L
        );
    }

    @Test
    void testObtenerHistorialEntre_UsuarioDestinoNoExiste() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> mensajeService.obtenerHistorialEntre(usuarioAuth, 2L));
    }

    @Test
    void testObtenerHistorialEntre_NoAutorizado() {
        when(usuarioRepository.findById(2L))
                .thenReturn(Optional.of(usuarioDestino));

        // Mensaje entre usuarios 3 y 4 → auth no está involucrado
        Usuario otro1 = new Usuario(); otro1.setIdUsuario(3L);
        Usuario otro2 = new Usuario(); otro2.setIdUsuario(4L);

        Mensaje m = new Mensaje();
        m.setEmisor(otro1);
        m.setReceptor(otro2);

        when(mensajeRepository
                .findByEmisor_IdUsuarioAndReceptor_IdUsuarioOrEmisor_IdUsuarioAndReceptor_IdUsuarioOrderByFechaEnvioAsc(
                        1L, 2L, 2L, 1L
                ))
                .thenReturn(List.of(m));

        assertThrows(SecurityException.class,
                () -> mensajeService.obtenerHistorialEntre(usuarioAuth, 2L));
    }

    // ============================================================
    // obtenerChatsRecientes()
    // ============================================================

    @Test
    void testObtenerChatsRecientes_ConPersona() {
        Persona persona = new Persona();
        persona.setNombres("Juan");
        persona.setApellidos("Perez");
        persona.setFotoPerfil("foto.jpg");

        usuarioDestino.setPersona(persona);

        when(mensajeRepository.findMensajesRecientesPorUsuario(1L))
                .thenReturn(List.of(mensaje1));

        List<ChatResumenDto> result =
                mensajeService.obtenerChatsRecientes(1L);

        assertEquals(1, result.size());
        ChatResumenDto dto = result.get(0);

        assertEquals("user2", dto.getUsername());
        assertEquals("Juan Perez", dto.getNombreCompleto());
        assertEquals("Hola", dto.getUltimoMensaje());
        assertEquals("foto.jpg", dto.getAvatarUrl());
    }

    @Test
    void testObtenerChatsRecientes_SinPersona() {
        usuarioDestino.setPersona(null);

        when(mensajeRepository.findMensajesRecientesPorUsuario(1L))
                .thenReturn(List.of(mensaje1));

        List<ChatResumenDto> result =
                mensajeService.obtenerChatsRecientes(1L);

        ChatResumenDto dto = result.get(0);

        assertEquals("user2", dto.getUsername());
        assertEquals("user2", dto.getNombreCompleto());
        assertNull(dto.getAvatarUrl());
    }

    @Test
    void testObtenerChatsRecientes_ArchivoSinTexto() {
        mensaje1.setContenidoTexto(null);
        mensaje1.setContenidoArchivo("archivo.pdf");

        when(mensajeRepository.findMensajesRecientesPorUsuario(1L))
                .thenReturn(List.of(mensaje1));

        List<ChatResumenDto> result =
                mensajeService.obtenerChatsRecientes(1L);

        assertEquals("[Archivo]", result.get(0).getUltimoMensaje());
    }

    @Test
    void testObtenerChatsRecientes_AgrupaConversaciones() {
        // Usuario autenticado = 1
        // Conversaciones: 1-2 y 1-3

        Usuario u3 = new Usuario();
        u3.setIdUsuario(3L);
        u3.setUsername("otro");

        Mensaje m2 = new Mensaje();
        m2.setEmisor(u3);
        m2.setReceptor(usuarioAuth);
        m2.setContenidoTexto("Hola 3");
        m2.setFechaEnvio(LocalDateTime.now());

        when(mensajeRepository.findMensajesRecientesPorUsuario(1L))
                .thenReturn(List.of(m2, mensaje1)); // orden descendente

        List<ChatResumenDto> result =
                mensajeService.obtenerChatsRecientes(1L);

        assertEquals(2, result.size());
    }
}
