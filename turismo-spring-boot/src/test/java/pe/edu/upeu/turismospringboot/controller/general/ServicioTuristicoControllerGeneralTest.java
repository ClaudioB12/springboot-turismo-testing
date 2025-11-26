package pe.edu.upeu.turismospringboot.controller.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.upeu.turismospringboot.model.entity.ServicioTuristico;
import pe.edu.upeu.turismospringboot.service.ServicioTuristicoService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ServicioTuristicoControllerGeneralTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServicioTuristicoService servicioTuristicoService;

    private ServicioTuristico servicioTuristico1;
    private ServicioTuristico servicioTuristico2;

    @BeforeEach
    void setUp() {
        servicioTuristico1 = new ServicioTuristico();
        servicioTuristico2 = new ServicioTuristico();
    }

    // ============================
    // GET /general/servicioTuristico/emprendimiento/{idEmprendimiento}
    // ============================

    @Test
    void debeBuscarServiciosTuristicosPorIdEmprendimiento() throws Exception {
        List<ServicioTuristico> servicios = Arrays.asList(servicioTuristico1, servicioTuristico2);
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(1L))
                .thenReturn(servicios);

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(1L);
    }

    @Test
    void debeRetornarListaVaciaCuandoNoHayServiciosParaEmprendimiento() throws Exception {
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(999L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/999"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(999L);
    }

    @Test
    void debeBuscarServiciosDeDiferentesEmprendimientos() throws Exception {
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(anyLong()))
                .thenReturn(Arrays.asList(servicioTuristico1));

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(1L);
        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(2L);
        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(3L);
    }

    @Test
    void debeBuscarServiciosConIdEmprendimientoGrande() throws Exception {
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(999999L))
                .thenReturn(Arrays.asList(servicioTuristico1));

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/999999"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(999999L);
    }

    @Test
    void debeRetornarListaConMultiplesServicios() throws Exception {
        ServicioTuristico servicio3 = new ServicioTuristico();
        ServicioTuristico servicio4 = new ServicioTuristico();
        ServicioTuristico servicio5 = new ServicioTuristico();

        List<ServicioTuristico> servicios = Arrays.asList(
                servicioTuristico1, servicioTuristico2, servicio3, servicio4, servicio5
        );

        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(1L))
                .thenReturn(servicios);

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)));

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(1L);
    }

    @Test
    void debeBuscarServiciosConIdCero() throws Exception {
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(0L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(0L);
    }

    @Test
    void debeInvocarServicioUnaVezPorCadaBusqueda() throws Exception {
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(1L))
                .thenReturn(Arrays.asList(servicioTuristico1));

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isOk());

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(1L);
        verify(servicioTuristicoService, never())
                .buscarServicioTuristicoPorIdEmprendimiento(2L);
    }

    @Test
    void debeBuscarServiciosMultiplesVecesParaMismoEmprendimiento() throws Exception {
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(1L))
                .thenReturn(Arrays.asList(servicioTuristico1, servicioTuristico2));

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(servicioTuristicoService, times(2))
                .buscarServicioTuristicoPorIdEmprendimiento(1L);
    }

    @Test
    void debeRetornarJsonValidoCuandoHayServicios() throws Exception {
        List<ServicioTuristico> servicios = Arrays.asList(servicioTuristico1);
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(1L))
                .thenReturn(servicios);

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(1L);
    }

    @Test
    void debeRetornarJsonValidoCuandoNoHayServicios() throws Exception {
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(1L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(1L);
    }

    // ============================
    // Casos adicionales
    // ============================

    @Test
    void debeManejarIdEmprendimientoNegativo() throws Exception {
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(-1L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(-1L);
    }

    @Test
    void debeRetornarUnSoloServicio() throws Exception {
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(1L))
                .thenReturn(Arrays.asList(servicioTuristico1));

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(1L);
    }

    @Test
    void debeBuscarServiciosParaVariosEmprendimientosEnSecuencia() throws Exception {
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(1L))
                .thenReturn(Arrays.asList(servicioTuristico1));
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(2L))
                .thenReturn(Arrays.asList(servicioTuristico1, servicioTuristico2));
        when(servicioTuristicoService.buscarServicioTuristicoPorIdEmprendimiento(3L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/general/servicioTuristico/emprendimiento/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(1L);
        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(2L);
        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorIdEmprendimiento(3L);
    }
}