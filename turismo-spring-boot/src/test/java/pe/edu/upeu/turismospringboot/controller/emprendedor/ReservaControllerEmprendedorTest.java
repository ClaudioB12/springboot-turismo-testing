package pe.edu.upeu.turismospringboot.controller.emprendedor;

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
import pe.edu.upeu.turismospringboot.model.dto.CrearReservaRequest;
import pe.edu.upeu.turismospringboot.model.dto.ReservaResponseDTO;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.model.entity.Reserva;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.model.enums.EstadoReserva;
import pe.edu.upeu.turismospringboot.service.ReservaService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReservaControllerEmprendedorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservaService reservaService;

    private Reserva reserva1;
    private Reserva reserva2;
    private ReservaResponseDTO reservaResponseDTO;
    private CrearReservaRequest crearReservaRequest;
    private Usuario usuario;
    private Emprendimiento emprendimiento;

    @BeforeEach
    void setUp() {
        // Crear usuario mock
        usuario = new Usuario();

        // Crear emprendimiento mock
        emprendimiento = new Emprendimiento();

        // Crear reserva1 con Mockito para evitar NullPointerException
        reserva1 = Mockito.mock(Reserva.class);
        when(reserva1.getIdReserva()).thenReturn(1L);
        when(reserva1.getEstado()).thenReturn(EstadoReserva.PENDIENTE);
        when(reserva1.getUsuario()).thenReturn(usuario);
        when(reserva1.getEmprendimiento()).thenReturn(emprendimiento);
        when(reserva1.getFechaHoraInicio()).thenReturn(LocalDateTime.now());
        when(reserva1.getFechaHoraFin()).thenReturn(LocalDateTime.now().plusHours(2));
        when(reserva1.getFechaHoraReserva()).thenReturn(LocalDateTime.now());

        // Crear reserva2 con Mockito
        reserva2 = Mockito.mock(Reserva.class);
        when(reserva2.getIdReserva()).thenReturn(2L);
        when(reserva2.getEstado()).thenReturn(EstadoReserva.CONFIRMADA);
        when(reserva2.getUsuario()).thenReturn(usuario);
        when(reserva2.getEmprendimiento()).thenReturn(emprendimiento);
        when(reserva2.getFechaHoraInicio()).thenReturn(LocalDateTime.now());
        when(reserva2.getFechaHoraFin()).thenReturn(LocalDateTime.now().plusHours(2));
        when(reserva2.getFechaHoraReserva()).thenReturn(LocalDateTime.now());

        // Crear ReservaResponseDTO con el constructor que requiere Reserva
        reservaResponseDTO = new ReservaResponseDTO(reserva1);

        crearReservaRequest = new CrearReservaRequest();
    }

    // ============================
    // POST /emprendedor/reserva
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeCrearReserva() throws Exception {
        when(reservaService.crearReserva(any(CrearReservaRequest.class), isNull()))
                .thenReturn(reservaResponseDTO);

        mockMvc.perform(post("/emprendedor/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearReservaRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reservaService, times(1))
                .crearReserva(any(CrearReservaRequest.class), isNull());
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeCrearMultiplesReservas() throws Exception {
        when(reservaService.crearReserva(any(CrearReservaRequest.class), isNull()))
                .thenReturn(reservaResponseDTO);

        mockMvc.perform(post("/emprendedor/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearReservaRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/emprendedor/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearReservaRequest)))
                .andExpect(status().isOk());

        verify(reservaService, times(2))
                .crearReserva(any(CrearReservaRequest.class), isNull());
    }

    // ============================
    // PUT /emprendedor/reserva/{id}/estado
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeActualizarEstadoReserva() throws Exception {
        when(reservaService.actualizarEstadoReserva(eq(1L), eq(EstadoReserva.CONFIRMADA), isNull()))
                .thenReturn(reservaResponseDTO);

        mockMvc.perform(put("/emprendedor/reserva/1/estado")
                        .param("nuevoEstado", "CONFIRMADA"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reservaService, times(1))
                .actualizarEstadoReserva(eq(1L), eq(EstadoReserva.CONFIRMADA), isNull());
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeActualizarEstadoACancelada() throws Exception {
        when(reservaService.actualizarEstadoReserva(eq(1L), eq(EstadoReserva.CANCELADA), isNull()))
                .thenReturn(reservaResponseDTO);

        mockMvc.perform(put("/emprendedor/reserva/1/estado")
                        .param("nuevoEstado", "CANCELADA"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reservaService, times(1))
                .actualizarEstadoReserva(eq(1L), eq(EstadoReserva.CANCELADA), isNull());
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeActualizarEstadoAPendiente() throws Exception {
        when(reservaService.actualizarEstadoReserva(eq(2L), eq(EstadoReserva.PENDIENTE), isNull()))
                .thenReturn(reservaResponseDTO);

        mockMvc.perform(put("/emprendedor/reserva/2/estado")
                        .param("nuevoEstado", "PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reservaService, times(1))
                .actualizarEstadoReserva(eq(2L), eq(EstadoReserva.PENDIENTE), isNull());
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeActualizarEstadoARechazada() throws Exception {
        when(reservaService.actualizarEstadoReserva(eq(1L), eq(EstadoReserva.RECHAZADA), isNull()))
                .thenReturn(reservaResponseDTO);

        mockMvc.perform(put("/emprendedor/reserva/1/estado")
                        .param("nuevoEstado", "RECHAZADA"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reservaService, times(1))
                .actualizarEstadoReserva(eq(1L), eq(EstadoReserva.RECHAZADA), isNull());
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeActualizarEstadoDeDiferentesReservas() throws Exception {
        when(reservaService.actualizarEstadoReserva(anyLong(), any(EstadoReserva.class), isNull()))
                .thenReturn(reservaResponseDTO);

        mockMvc.perform(put("/emprendedor/reserva/1/estado")
                        .param("nuevoEstado", "CONFIRMADA"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/emprendedor/reserva/2/estado")
                        .param("nuevoEstado", "CANCELADA"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/emprendedor/reserva/3/estado")
                        .param("nuevoEstado", "RECHAZADA"))
                .andExpect(status().isOk());

        verify(reservaService, times(1))
                .actualizarEstadoReserva(eq(1L), eq(EstadoReserva.CONFIRMADA), isNull());
        verify(reservaService, times(1))
                .actualizarEstadoReserva(eq(2L), eq(EstadoReserva.CANCELADA), isNull());
        verify(reservaService, times(1))
                .actualizarEstadoReserva(eq(3L), eq(EstadoReserva.RECHAZADA), isNull());
    }

    // ============================
    // GET /emprendedor/reserva/idEmprendimiento/{idEmprendimiento}
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeObtenerReservasPorIdEmprendimiento() throws Exception {
        List<Reserva> reservas = Arrays.asList(reserva1, reserva2);
        when(reservaService.obtenerReservasPorIdEmprendimiento(1L))
                .thenReturn(reservas);

        mockMvc.perform(get("/emprendedor/reserva/idEmprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(reservaService, times(1))
                .obtenerReservasPorIdEmprendimiento(1L);
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeRetornarListaVaciaCuandoNoHayReservasParaEmprendimiento() throws Exception {
        when(reservaService.obtenerReservasPorIdEmprendimiento(999L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/emprendedor/reserva/idEmprendimiento/999"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(reservaService, times(1))
                .obtenerReservasPorIdEmprendimiento(999L);
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeObtenerReservasDeDiferentesEmprendimientos() throws Exception {
        when(reservaService.obtenerReservasPorIdEmprendimiento(anyLong()))
                .thenReturn(Arrays.asList(reserva1));

        mockMvc.perform(get("/emprendedor/reserva/idEmprendimiento/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/emprendedor/reserva/idEmprendimiento/2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/emprendedor/reserva/idEmprendimiento/3"))
                .andExpect(status().isOk());

        verify(reservaService, times(1))
                .obtenerReservasPorIdEmprendimiento(1L);
        verify(reservaService, times(1))
                .obtenerReservasPorIdEmprendimiento(2L);
        verify(reservaService, times(1))
                .obtenerReservasPorIdEmprendimiento(3L);
    }

    // ============================
    // GET /emprendedor/reserva/{idReserva}
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeObtenerReservaPorId() throws Exception {
        when(reservaService.obtenerReservaPorId(1L))
                .thenReturn(reserva1);

        mockMvc.perform(get("/emprendedor/reserva/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reservaService, times(1))
                .obtenerReservaPorId(1L);
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeObtenerDiferentesReservasPorId() throws Exception {
        when(reservaService.obtenerReservaPorId(anyLong()))
                .thenReturn(reserva1);

        mockMvc.perform(get("/emprendedor/reserva/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/emprendedor/reserva/5"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/emprendedor/reserva/10"))
                .andExpect(status().isOk());

        verify(reservaService, times(1))
                .obtenerReservaPorId(1L);
        verify(reservaService, times(1))
                .obtenerReservaPorId(5L);
        verify(reservaService, times(1))
                .obtenerReservaPorId(10L);
    }

    // ============================
    // Seguridad
    // ============================

    @Test
    void debeDenegarAccesoSinAutenticacionAlCrearReserva() throws Exception {
        mockMvc.perform(post("/emprendedor/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearReservaRequest)))
                .andExpect(status().isForbidden());

        verify(reservaService, never())
                .crearReserva(any(), any());
    }

    @Test
    void debeDenegarAccesoSinAutenticacionAlActualizarEstado() throws Exception {
        mockMvc.perform(put("/emprendedor/reserva/1/estado")
                        .param("nuevoEstado", "CONFIRMADA"))
                .andExpect(status().isForbidden());

        verify(reservaService, never())
                .actualizarEstadoReserva(any(), any(), any());
    }

    @Test
    void debeDenegarAccesoSinAutenticacionAlObtenerReservasPorEmprendimiento() throws Exception {
        mockMvc.perform(get("/emprendedor/reserva/idEmprendimiento/1"))
                .andExpect(status().isForbidden());

        verify(reservaService, never())
                .obtenerReservasPorIdEmprendimiento(any());
    }

    @Test
    void debeDenegarAccesoSinAutenticacionAlObtenerReservaPorId() throws Exception {
        mockMvc.perform(get("/emprendedor/reserva/1"))
                .andExpect(status().isForbidden());

        verify(reservaService, never())
                .obtenerReservaPorId(any());
    }

    @Test
    @WithMockUser(username = "turista1", roles = {"TURISTA"})
    void debeDenegarAccesoConRolIncorrectoAlCrearReserva() throws Exception {
        mockMvc.perform(post("/emprendedor/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearReservaRequest)))
                .andExpect(status().isForbidden());

        verify(reservaService, never())
                .crearReserva(any(), any());
    }

    @Test
    @WithMockUser(username = "turista1", roles = {"TURISTA"})
    void debeDenegarAccesoConRolIncorrectoAlActualizarEstado() throws Exception {
        mockMvc.perform(put("/emprendedor/reserva/1/estado")
                        .param("nuevoEstado", "CONFIRMADA"))
                .andExpect(status().isForbidden());

        verify(reservaService, never())
                .actualizarEstadoReserva(any(), any(), any());
    }

    @Test
    @WithMockUser(username = "turista1", roles = {"TURISTA"})
    void debeDenegarAccesoConRolIncorrectoAlObtenerReservasPorEmprendimiento() throws Exception {
        mockMvc.perform(get("/emprendedor/reserva/idEmprendimiento/1"))
                .andExpect(status().isForbidden());

        verify(reservaService, never())
                .obtenerReservasPorIdEmprendimiento(any());
    }

    @Test
    @WithMockUser(username = "turista1", roles = {"TURISTA"})
    void debeDenegarAccesoConRolIncorrectoAlObtenerReservaPorId() throws Exception {
        mockMvc.perform(get("/emprendedor/reserva/1"))
                .andExpect(status().isForbidden());

        verify(reservaService, never())
                .obtenerReservaPorId(any());
    }

    // ============================
    // Casos adicionales
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeObtenerReservasConListaGrande() throws Exception {
        Reserva reserva3 = Mockito.mock(Reserva.class);
        when(reserva3.getIdReserva()).thenReturn(3L);
        when(reserva3.getEstado()).thenReturn(EstadoReserva.PENDIENTE);
        when(reserva3.getUsuario()).thenReturn(usuario);
        when(reserva3.getEmprendimiento()).thenReturn(emprendimiento);

        Reserva reserva4 = Mockito.mock(Reserva.class);
        when(reserva4.getIdReserva()).thenReturn(4L);
        when(reserva4.getEstado()).thenReturn(EstadoReserva.CONFIRMADA);
        when(reserva4.getUsuario()).thenReturn(usuario);
        when(reserva4.getEmprendimiento()).thenReturn(emprendimiento);

        Reserva reserva5 = Mockito.mock(Reserva.class);
        when(reserva5.getIdReserva()).thenReturn(5L);
        when(reserva5.getEstado()).thenReturn(EstadoReserva.CANCELADA);
        when(reserva5.getUsuario()).thenReturn(usuario);
        when(reserva5.getEmprendimiento()).thenReturn(emprendimiento);

        List<Reserva> reservasGrandes = Arrays.asList(
                reserva1, reserva2, reserva3, reserva4, reserva5
        );
        when(reservaService.obtenerReservasPorIdEmprendimiento(1L))
                .thenReturn(reservasGrandes);

        mockMvc.perform(get("/emprendedor/reserva/idEmprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)));

        verify(reservaService, times(1))
                .obtenerReservasPorIdEmprendimiento(1L);
    }
}