package pe.edu.upeu.turismospringboot.controller.emprendedor;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import pe.edu.upeu.turismospringboot.model.dto.ChatResumenDto;
import pe.edu.upeu.turismospringboot.model.dto.MensajeDto;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.model.enums.EstadoMensaje;
import pe.edu.upeu.turismospringboot.model.enums.TipoMensaje;
import pe.edu.upeu.turismospringboot.service.MensajeService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class MensajeEmprendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MensajeService mensajeService;

    private Usuario usuarioAutenticado;
    private MensajeDto mensaje1;
    private MensajeDto mensaje2;
    private ChatResumenDto chat1;
    private ChatResumenDto chat2;

    @BeforeEach
    void setUp() {

        usuarioAutenticado = new Usuario();
        usuarioAutenticado.setIdUsuario(10L);
        usuarioAutenticado.setUsername("emprendedor1");

        mensaje1 = new MensajeDto();
        mensaje1.setId(1L);
        mensaje1.setEmisorUsername("emprendedor1");
        mensaje1.setReceptorUsername("turista1");
        mensaje1.setContenidoTexto("Hola");
        mensaje1.setTipo(TipoMensaje.TEXTO);
        mensaje1.setEstado(EstadoMensaje.LEIDO);
        mensaje1.setFechaEnvio(LocalDateTime.now());

        mensaje2 = new MensajeDto();
        mensaje2.setId(2L);
        mensaje2.setEmisorUsername("turista1");
        mensaje2.setReceptorUsername("emprendedor1");
        mensaje2.setContenidoTexto("Disponible");
        mensaje2.setTipo(TipoMensaje.TEXTO);
        mensaje2.setEstado(EstadoMensaje.ENVIADO);
        mensaje2.setFechaEnvio(LocalDateTime.now());

        chat1 = new ChatResumenDto();
        chat1.setUsername("turista1");
        chat1.setNombreCompleto("Uno");
        chat1.setUltimoMensaje("Hola");
        chat1.setEstadoUltimoMensaje(EstadoMensaje.ENVIADO);
        chat1.setHora(LocalDateTime.now());

        chat2 = new ChatResumenDto();
        chat2.setUsername("turista2");
        chat2.setNombreCompleto("Dos");
        chat2.setUltimoMensaje("Gracias");
        chat2.setEstadoUltimoMensaje(EstadoMensaje.LEIDO);
        chat2.setHora(LocalDateTime.now());
    }

    private void autenticar(Usuario u) {
        var auth = new UsernamePasswordAuthenticationToken(u, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // -------- HISTORIAL --------

    @Test
    void obtieneHistorial() throws Exception {

        autenticar(usuarioAutenticado);

        when(mensajeService.obtenerHistorialEntre(any(Usuario.class), eq(2L)))
                .thenReturn(Arrays.asList(mensaje1, mensaje2));

        mockMvc.perform(get("/emprendedor/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(mensajeService).obtenerHistorialEntre(any(Usuario.class), eq(2L));
    }

    @Test
    void historialVacio() throws Exception {

        autenticar(usuarioAutenticado);

        when(mensajeService.obtenerHistorialEntre(any(Usuario.class), eq(2L)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/emprendedor/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(mensajeService).obtenerHistorialEntre(any(Usuario.class), eq(2L));
    }

    // -------- RECIENTES --------

    @Test
    void obtieneChatsRecientes() throws Exception {

        autenticar(usuarioAutenticado);

        when(mensajeService.obtenerChatsRecientes(10L))
                .thenReturn(Arrays.asList(chat1, chat2));

        mockMvc.perform(get("/emprendedor/mensajes/recientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("turista1"))
                .andExpect(jsonPath("$[1].username").value("turista2"));

        verify(mensajeService).obtenerChatsRecientes(10L);
    }

    @Test
    void chatsRecientesVacio() throws Exception {

        autenticar(usuarioAutenticado);

        when(mensajeService.obtenerChatsRecientes(10L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/emprendedor/mensajes/recientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(mensajeService).obtenerChatsRecientes(10L);
    }

    // -------- SIN LOGIN --------
    
}
