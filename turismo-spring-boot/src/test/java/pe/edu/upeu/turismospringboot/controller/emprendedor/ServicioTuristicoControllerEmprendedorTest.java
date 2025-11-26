package pe.edu.upeu.turismospringboot.controller.emprendedor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.upeu.turismospringboot.model.dto.ServicioTuristicoDto;
import pe.edu.upeu.turismospringboot.model.entity.ServicioTuristico;
import pe.edu.upeu.turismospringboot.service.ServicioTuristicoService;

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
class ServicioTuristicoControllerEmprendedorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServicioTuristicoService servicioTuristicoService;

    private ServicioTuristico servicio1;
    private ServicioTuristico servicio2;
    private ServicioTuristicoDto servicioDto;

    @BeforeEach
    void setUp() {
        // NO usamos setters de la entidad, solo instancias vacías
        servicio1 = new ServicioTuristico();
        servicio2 = new ServicioTuristico();

        // El DTO sí suele tener setters, lo dejamos igual
        servicioDto = new ServicioTuristicoDto();
        // si tu dto no tiene setters, también puedes dejarlo vacío.
        // servicioDto.setNombre("Tour Machu Picchu");
        // ...
    }

    // ============================
    // GET /emprendedor/servicioTuristico/emprendimiento/{idEmprendimiento}
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeObtenerServiciosPorIdEmprendimiento() throws Exception {
        List<ServicioTuristico> servicios = Arrays.asList(servicio1, servicio2);
        when(servicioTuristicoService.getServicioTuristicosPorIdEmprendimiento(1L))
                .thenReturn(servicios);

        mockMvc.perform(get("/emprendedor/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // solo validamos tamaño; no validamos campos internos para no depender de getters/setters
                .andExpect(jsonPath("$", hasSize(2)));

        verify(servicioTuristicoService, times(1))
                .getServicioTuristicosPorIdEmprendimiento(1L);
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeRetornarListaVaciaCuandoNoHayServiciosParaEmprendimiento() throws Exception {
        when(servicioTuristicoService.getServicioTuristicosPorIdEmprendimiento(999L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/emprendedor/servicioTuristico/emprendimiento/999"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(servicioTuristicoService, times(1))
                .getServicioTuristicosPorIdEmprendimiento(999L);
    }

    // ============================
    // GET /emprendedor/servicioTuristico/{idServicio}
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeObtenerServicioTuristicoPorId() throws Exception {
        when(servicioTuristicoService.getServicioTuristicoById(1L))
                .thenReturn(servicio1);

        mockMvc.perform(get("/emprendedor/servicioTuristico/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(servicioTuristicoService, times(1))
                .getServicioTuristicoById(1L);
    }

    // ============================
    // POST /emprendedor/servicioTuristico
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeCrearServicioTuristicoConArchivo() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "imagen.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "imagen de prueba".getBytes()
        );

        MockMultipartFile servicioJson = new MockMultipartFile(
                "servicioTuristico",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(servicioDto)
        );

        when(servicioTuristicoService.postServicioTuristico(any(ServicioTuristicoDto.class), any()))
                .thenReturn(servicio1);

        mockMvc.perform(multipart("/emprendedor/servicioTuristico")
                        .file(file)
                        .file(servicioJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(servicioTuristicoService, times(1))
                .postServicioTuristico(any(ServicioTuristicoDto.class), any());
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeCrearServicioTuristicoSinArchivo() throws Exception {
        MockMultipartFile servicioJson = new MockMultipartFile(
                "servicioTuristico",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(servicioDto)
        );

        when(servicioTuristicoService.postServicioTuristico(any(ServicioTuristicoDto.class), isNull()))
                .thenReturn(servicio1);

        mockMvc.perform(multipart("/emprendedor/servicioTuristico")
                        .file(servicioJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(servicioTuristicoService, times(1))
                .postServicioTuristico(any(ServicioTuristicoDto.class), isNull());
    }

    // ============================
    // PUT /emprendedor/servicioTuristico/{idServicio}
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeActualizarServicioTuristico() throws Exception {
        MockMultipartFile servicioJson = new MockMultipartFile(
                "servicioTuristico",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(servicioDto)
        );

        when(servicioTuristicoService.putServicioTuristico(eq(1L), any(ServicioTuristicoDto.class), any()))
                .thenReturn(servicio1);

        mockMvc.perform(multipart("/emprendedor/servicioTuristico/1")
                        .file(servicioJson)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(servicioTuristicoService, times(1))
                .putServicioTuristico(eq(1L), any(ServicioTuristicoDto.class), any());
    }

    // ============================
    // DELETE /emprendedor/servicioTuristico/{idServicio}
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeEliminarServicioTuristico() throws Exception {
        doNothing().when(servicioTuristicoService).deleteServicioTuristico(1L);

        mockMvc.perform(delete("/emprendedor/servicioTuristico/1"))
                .andExpect(status().isNoContent());

        verify(servicioTuristicoService, times(1))
                .deleteServicioTuristico(1L);
    }

    // ============================
    // GET /emprendedor/servicioTuristico/buscar
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeBuscarServiciosPorNombre() throws Exception {
        when(servicioTuristicoService.buscarServicioTuristicoPorNombre("Machu"))
                .thenReturn(Arrays.asList(servicio1));

        mockMvc.perform(get("/emprendedor/servicioTuristico/buscar")
                        .param("nombre", "Machu"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(servicioTuristicoService, times(1))
                .buscarServicioTuristicoPorNombre("Machu");
    }

    // ============================
    // Seguridad básica
    // ============================

    @Test
    void debeDenegarAccesoSinAutenticacion() throws Exception {
        mockMvc.perform(get("/emprendedor/servicioTuristico/emprendimiento/1"))
                .andExpect(status().isForbidden());

        verify(servicioTuristicoService, never())
                .getServicioTuristicosPorIdEmprendimiento(any());
    }
}