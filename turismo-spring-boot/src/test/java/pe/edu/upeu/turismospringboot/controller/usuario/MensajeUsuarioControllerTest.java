package pe.edu.upeu.turismospringboot.controller.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.upeu.turismospringboot.model.dto.MensajeDto;
import pe.edu.upeu.turismospringboot.service.MensajeService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MensajeUsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MensajeService mensajeService;

    private MensajeDto mensaje1;
    private MensajeDto mensaje2;

    @BeforeEach
    void setUp() {
        // Crear mocks de DTOs
        mensaje1 = Mockito.mock(MensajeDto.class);
        mensaje2 = Mockito.mock(MensajeDto.class);
    }

    // ============================
    // GET /usuario/mensajes/historial
    // ============================

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeObtenerHistorialDeMensajes() throws Exception {
        List<MensajeDto> historial = Arrays.asList(mensaje1, mensaje2);
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(2L)))
                .thenReturn(historial);

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(2L));
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeRetornarHistorialVacioCuandoNoHayMensajes() throws Exception {
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(2L)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(2L));
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeObtenerHistorialConUnSoloMensaje() throws Exception {
        List<MensajeDto> historial = Arrays.asList(mensaje1);
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(3L)))
                .thenReturn(historial);

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(3L));
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeObtenerHistorialConDiferentesUsuarios() throws Exception {
        when(mensajeService.obtenerHistorialEntre(isNull(), anyLong()))
                .thenReturn(Arrays.asList(mensaje1));

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "3"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "4"))
                .andExpect(status().isOk());

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(2L));
        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(3L));
        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(4L));
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeObtenerHistorialConMuchosMensajes() throws Exception {
        MensajeDto mensaje3 = Mockito.mock(MensajeDto.class);
        MensajeDto mensaje4 = Mockito.mock(MensajeDto.class);
        MensajeDto mensaje5 = Mockito.mock(MensajeDto.class);

        List<MensajeDto> historial = Arrays.asList(mensaje1, mensaje2, mensaje3, mensaje4, mensaje5);
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(2L)))
                .thenReturn(historial);

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)));

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(2L));
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeObtenerHistorialMultiplesVeces() throws Exception {
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(2L)))
                .thenReturn(Arrays.asList(mensaje1, mensaje2));

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(mensajeService, times(2))
                .obtenerHistorialEntre(isNull(), eq(2L));
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeRetornarJsonValidoParaHistorial() throws Exception {
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(2L)))
                .thenReturn(Arrays.asList(mensaje1));

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(2L));
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeObtenerHistorialConUsuarioIdCero() throws Exception {
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(0L)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(0L));
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeObtenerHistorialConUsuarioIdGrande() throws Exception {
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(999999L)))
                .thenReturn(Arrays.asList(mensaje1));

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(999999L));
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeRetornarStatus200ParaHistorial() throws Exception {
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(2L)))
                .thenReturn(Arrays.asList(mensaje1));

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk())
                .andExpect(status().is(200));

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(2L));
    }

    // ============================
    // Seguridad
    // ============================

    @Test
    void debeDenegarAccesoSinAutenticacionAlObtenerHistorial() throws Exception {
        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isForbidden());

        verify(mensajeService, never())
                .obtenerHistorialEntre(any(), any());
    }

    @Test
    void debeDenegarAccesoSinAutenticacionAlObtenerChatsRecientes() throws Exception {
        mockMvc.perform(get("/usuario/mensajes/recientes"))
                .andExpect(status().isForbidden());

        verify(mensajeService, never())
                .obtenerChatsRecientes(any());
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeDenegarAccesoConRolIncorrectoAlObtenerHistorial() throws Exception {
        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isForbidden());

        verify(mensajeService, never())
                .obtenerHistorialEntre(any(), any());
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeDenegarAccesoConRolIncorrectoAlObtenerChatsRecientes() throws Exception {
        mockMvc.perform(get("/usuario/mensajes/recientes"))
                .andExpect(status().isForbidden());

        verify(mensajeService, never())
                .obtenerChatsRecientes(any());
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeInvocarServicioHistorialUnaVez() throws Exception {
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(2L)))
                .thenReturn(Arrays.asList(mensaje1));

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk());

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(2L));
        verifyNoMoreInteractions(mensajeService);
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeObtenerHistorialConTresUsuariosDiferentes() throws Exception {
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(5L)))
                .thenReturn(Arrays.asList(mensaje1, mensaje2));
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(6L)))
                .thenReturn(Arrays.asList(mensaje1));
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(7L)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(5L));
        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(6L));
        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(7L));
    }

    @Test
    @WithMockUser(username = "usuario1", roles = {"USUARIO"})
    void debeRetornarArrayJsonParaHistorial() throws Exception {
        when(mensajeService.obtenerHistorialEntre(isNull(), eq(2L)))
                .thenReturn(Arrays.asList(mensaje1, mensaje2));

        mockMvc.perform(get("/usuario/mensajes/historial")
                        .param("usuarioId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(isNull(), eq(2L));
    }
}