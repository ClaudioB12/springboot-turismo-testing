package pe.edu.upeu.turismospringboot.controller.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import pe.edu.upeu.turismospringboot.controller.webSocket.ChatController;
import pe.edu.upeu.turismospringboot.model.dto.MensajeDto;
import pe.edu.upeu.turismospringboot.model.entity.Mensaje;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.model.enums.EstadoMensaje;
import pe.edu.upeu.turismospringboot.repository.MensajeRepository;
import pe.edu.upeu.turismospringboot.repository.UsuarioRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ChatControllerTest {

    @InjectMocks
    private ChatController chatController;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MensajeRepository mensajeRepository;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ============================================================
    // 📌 TEST 1 — Enviar mensaje correctamente
    // ============================================================
    @Test
    void testProcesarMensaje_EnvioCorrecto() {

        MensajeDto dto = new MensajeDto();
        dto.setEmisorUsername("juan");
        dto.setReceptorUsername("pedro");
        dto.setContenidoTexto("Hola Pedro");
        dto.setEstado(EstadoMensaje.PENDIENTE);

        Usuario emisor = new Usuario();
        emisor.setUsername("juan");

        Usuario receptor = new Usuario();
        receptor.setUsername("pedro");

        Mensaje mensajeGuardado = new Mensaje();
        mensajeGuardado.setId(1L);

        when(principal.getName()).thenReturn("juan");
        when(usuarioRepository.findByUsername("juan")).thenReturn(Optional.of(emisor));
        when(usuarioRepository.findByUsername("pedro")).thenReturn(Optional.of(receptor));
        when(mensajeRepository.save(any(Mensaje.class))).thenReturn(mensajeGuardado);

        chatController.procesarMensaje(dto, principal);

        verify(mensajeRepository, times(1)).save(any(Mensaje.class));
        verify(messagingTemplate, times(1))
                .convertAndSendToUser(eq("pedro"), eq("/queue/mensajes"), any(MensajeDto.class));
        verify(messagingTemplate, times(1))
                .convertAndSendToUser(eq("juan"), eq("/queue/mensajes"), any(MensajeDto.class));
    }

    // ============================================================
    // 📌 TEST 2 — Envío denegado porque el usuario intenta suplantar identidad
    // ============================================================
    @Test
    void testProcesarMensaje_UsuarioIncorrecto() {
        MensajeDto dto = new MensajeDto();
        dto.setEmisorUsername("juan");
        dto.setReceptorUsername("pedro");

        when(principal.getName()).thenReturn("otroUsuario");

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.security.access.AccessDeniedException.class,
                () -> chatController.procesarMensaje(dto, principal)
        );
    }

    // ============================================================
    // 📌 TEST 3 — Marcar mensajes como ENTREGADO
    // ============================================================
    @Test
    void testMarcarComoEntregado() {

        Usuario emisor = new Usuario();
        emisor.setUsername("juan");

        Usuario receptor = new Usuario();
        receptor.setUsername("pedro");

        Mensaje mensaje = new Mensaje();
        mensaje.setId(10L);
        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);
        mensaje.setEstado(EstadoMensaje.ENVIADO);
        mensaje.setFechaEnvio(LocalDateTime.now());

        MensajeDto dto = new MensajeDto();
        dto.setEmisorUsername("juan");

        when(principal.getName()).thenReturn("pedro");
        when(mensajeRepository.findAllByEmisor_UsernameAndReceptor_UsernameAndEstado(
                "juan", "pedro", EstadoMensaje.ENVIADO
        )).thenReturn(List.of(mensaje));

        chatController.marcarComoEntregado(dto, principal);

        verify(mensajeRepository, times(1)).save(mensaje);
        verify(messagingTemplate, times(1))
                .convertAndSendToUser(eq("juan"), eq("/queue/estado"), any(MensajeDto.class));
    }

    // ============================================================
    // 📌 TEST 4 — Marcar mensajes como LEÍDO
    // ============================================================
    @Test
    void testMarcarComoLeido() {

        Usuario emisor = new Usuario();
        emisor.setUsername("juan");

        Usuario receptor = new Usuario();
        receptor.setUsername("pedro");

        Mensaje mensaje = new Mensaje();
        mensaje.setId(20L);
        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);
        mensaje.setEstado(EstadoMensaje.ENTREGADO);
        mensaje.setFechaEnvio(LocalDateTime.now());

        MensajeDto dto = new MensajeDto();
        dto.setEmisorUsername("juan");

        when(principal.getName()).thenReturn("pedro");
        when(mensajeRepository.findAllByEmisor_UsernameAndReceptor_UsernameAndEstadoNot(
                "juan", "pedro", EstadoMensaje.LEIDO
        )).thenReturn(List.of(mensaje));

        chatController.marcarComoLeido(dto, principal);

        verify(mensajeRepository, times(1)).save(mensaje);
        verify(messagingTemplate, times(1))
                .convertAndSendToUser(eq("juan"), eq("/queue/estado"), any(MensajeDto.class));
    }

    // ============================================================
    // 📌 TEST 5 — Editar mensaje correctamente
    // ============================================================
    @Test
    void testEditarMensaje() {

        Usuario emisor = new Usuario();
        emisor.setUsername("juan");

        Usuario receptor = new Usuario();
        receptor.setUsername("pedro");

        Mensaje mensaje = new Mensaje();
        mensaje.setId(5L);
        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);

        MensajeDto dto = new MensajeDto();
        dto.setId(5L);
        dto.setEmisorUsername("juan");
        dto.setContenidoTexto("Nuevo texto");

        when(principal.getName()).thenReturn("juan");
        when(mensajeRepository.findById(5L)).thenReturn(Optional.of(mensaje));

        chatController.editarMensaje(dto, principal);

        verify(mensajeRepository, times(1)).save(mensaje);
        verify(messagingTemplate, times(1))
                .convertAndSendToUser(eq("pedro"), eq("/queue/mensaje-editado"), any(MensajeDto.class));
        verify(messagingTemplate, times(1))
                .convertAndSendToUser(eq("juan"), eq("/queue/mensaje-editado"), any(MensajeDto.class));
    }
}
