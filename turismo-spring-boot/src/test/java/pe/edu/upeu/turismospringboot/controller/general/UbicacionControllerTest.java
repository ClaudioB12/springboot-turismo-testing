package pe.edu.upeu.turismospringboot.controller.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.upeu.turismospringboot.model.dto.UbicacionDTO;
import pe.edu.upeu.turismospringboot.service.UbicacionService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UbicacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UbicacionService ubicacionService;

    private UbicacionDTO ubicacion1;
    private UbicacionDTO ubicacion2;
    private UbicacionDTO ubicacion3;

    @BeforeEach
    void setUp() {
        // Crear mocks de UbicacionDTO
        ubicacion1 = Mockito.mock(UbicacionDTO.class);
        ubicacion2 = Mockito.mock(UbicacionDTO.class);
        ubicacion3 = Mockito.mock(UbicacionDTO.class);
    }

    // ============================
    // GET /general/ubicaciones
    // ============================

    @Test
    void debeObtenerTodasLasUbicaciones() throws Exception {
        List<UbicacionDTO> ubicaciones = Arrays.asList(ubicacion1, ubicacion2, ubicacion3);
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(ubicaciones);

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeRetornarListaVaciaCuandoNoHayUbicaciones() throws Exception {
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeRetornarUnaUbicacion() throws Exception {
        List<UbicacionDTO> ubicaciones = Arrays.asList(ubicacion1);
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(ubicaciones);

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeRetornarDosUbicaciones() throws Exception {
        List<UbicacionDTO> ubicaciones = Arrays.asList(ubicacion1, ubicacion2);
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(ubicaciones);

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeRetornarMuchasUbicaciones() throws Exception {
        UbicacionDTO ubicacion4 = Mockito.mock(UbicacionDTO.class);
        UbicacionDTO ubicacion5 = Mockito.mock(UbicacionDTO.class);
        UbicacionDTO ubicacion6 = Mockito.mock(UbicacionDTO.class);
        UbicacionDTO ubicacion7 = Mockito.mock(UbicacionDTO.class);
        UbicacionDTO ubicacion8 = Mockito.mock(UbicacionDTO.class);
        UbicacionDTO ubicacion9 = Mockito.mock(UbicacionDTO.class);
        UbicacionDTO ubicacion10 = Mockito.mock(UbicacionDTO.class);

        List<UbicacionDTO> ubicaciones = Arrays.asList(
                ubicacion1, ubicacion2, ubicacion3,
                ubicacion4, ubicacion5, ubicacion6,
                ubicacion7, ubicacion8, ubicacion9,
                ubicacion10
        );
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(ubicaciones);

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(10)));

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeInvocarServicioUnaVez() throws Exception {
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(Arrays.asList(ubicacion1));

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk());

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
        verifyNoMoreInteractions(ubicacionService);
    }

    @Test
    void debeInvocarServicioMultiplesVeces() throws Exception {
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(Arrays.asList(ubicacion1, ubicacion2));

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk());

        verify(ubicacionService, times(3))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeRetornarJsonValidoCuandoHayUbicaciones() throws Exception {
        List<UbicacionDTO> ubicaciones = Arrays.asList(ubicacion1, ubicacion2);
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(ubicaciones);

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeRetornarJsonValidoCuandoNoHayUbicaciones() throws Exception {
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeRetornarStatus200() throws Exception {
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(Arrays.asList(ubicacion1));

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(status().is(200));

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeRetornarContentTypeJson() throws Exception {
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(Arrays.asList(ubicacion1));

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeRetornarArrayJson() throws Exception {
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(Arrays.asList(ubicacion1, ubicacion2, ubicacion3));

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeObtenerUbicacionesConDiferentesLlamadas() throws Exception {
        // Primera llamada: 3 ubicaciones
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(Arrays.asList(ubicacion1, ubicacion2, ubicacion3));

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        // Segunda llamada: 1 ubicación
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(Arrays.asList(ubicacion1));

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(ubicacionService, times(2))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeRetornarListaSinErrores() throws Exception {
        List<UbicacionDTO> ubicaciones = Arrays.asList(ubicacion1, ubicacion2);
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(ubicaciones);

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeVerificarQueNoSeInvocanOtrosMetodos() throws Exception {
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(Arrays.asList(ubicacion1));

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk());

        verify(ubicacionService, times(1))
                .obtenerTodasLasUbicaciones();
        verifyNoMoreInteractions(ubicacionService);
    }

    @Test
    void debeObtenerUbicacionesVariasVecesConsecutivas() throws Exception {
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(Arrays.asList(ubicacion1, ubicacion2, ubicacion3));

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/general/ubicaciones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)));
        }

        verify(ubicacionService, times(5))
                .obtenerTodasLasUbicaciones();
    }

    @Test
    void debeRetornarRespuestaConsistenteEnMultiplesLlamadas() throws Exception {
        List<UbicacionDTO> ubicaciones = Arrays.asList(ubicacion1, ubicacion2);
        when(ubicacionService.obtenerTodasLasUbicaciones())
                .thenReturn(ubicaciones);

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/general/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(ubicacionService, times(2))
                .obtenerTodasLasUbicaciones();
    }
}