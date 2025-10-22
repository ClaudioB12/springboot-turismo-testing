package pe.edu.upeu.turismospringboot.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import pe.edu.upeu.turismospringboot.model.dto.FamiliaDto;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.service.FamiliaService;


import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class FamiliaControllerTest {


    private MockMvc mockMvc;


    @Mock
    private FamiliaService familiaService;


    @InjectMocks
    private FamiliaController familiaController;


    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(familiaController).build();
    }


    /** 🔹 Test: GET /admin/familia */
    @Test
    void testObtenerFamilias() throws Exception {
        Familia familia = new Familia();
        familia.setNombre("Familia Test");


        when(familiaService.getFamilias()).thenReturn(List.of(familia));


        mockMvc.perform(get("/admin/familia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Familia Test"));


        verify(familiaService).getFamilias();
    }


    /** 🔹 Test: POST /admin/familia */
    @Test
    void testGuardarFamilia() throws Exception {
        FamiliaDto dto = new FamiliaDto();
        dto.setNombre("Nueva Familia");
        dto.setDescripcion("Descripción de prueba");
        dto.setNombreLugar("Arequipa");


        String jsonDto = objectMapper.writeValueAsString(dto);


        MockMultipartFile familiaPart = new MockMultipartFile(
                "familia",
                "",
                "application/json",
                jsonDto.getBytes()
        );


        MockMultipartFile file = new MockMultipartFile(
                "file",
                "imagen.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image".getBytes()
        );


        Familia familiaGuardada = new Familia();
        familiaGuardada.setIdFamilia(1L);
        familiaGuardada.setNombre("Nueva Familia");


        when(familiaService.postFamilia(any(FamiliaDto.class), any())).thenReturn(familiaGuardada);


        mockMvc.perform(multipart("/admin/familia")
                        .file(familiaPart)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Nueva Familia"));


        verify(familiaService).postFamilia(any(FamiliaDto.class), any());
    }


    /** 🔹 Test: PUT /admin/familia/{id} */
    @Test
    void testActualizarFamilia() throws Exception {
        Long id = 1L;
        FamiliaDto dto = new FamiliaDto();
        dto.setNombre("Familia Actualizada");
        dto.setDescripcion("Nueva descripción");
        dto.setNombreLugar("Cusco");


        String jsonDto = objectMapper.writeValueAsString(dto);


        MockMultipartFile familiaPart = new MockMultipartFile(
                "familia",
                "",
                "application/json",
                jsonDto.getBytes()
        );


        MockMultipartFile file = new MockMultipartFile(
                "file",
                "nueva.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image".getBytes()
        );


        Familia familiaActualizada = new Familia();
        familiaActualizada.setIdFamilia(id);
        familiaActualizada.setNombre("Familia Actualizada");


        when(familiaService.putFamilia(eq(id), any(FamiliaDto.class), any())).thenReturn(familiaActualizada);


        mockMvc.perform(multipart("/admin/familia/{idFamilia}", id)
                        .file(familiaPart)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT"); // importante para simular PUT con multipart
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Familia Actualizada"));


        verify(familiaService).putFamilia(eq(id), any(FamiliaDto.class), any());
    }


    /** 🔹 Test: DELETE /admin/familia/{id} */
    @Test
    void testEliminarFamilia() throws Exception {
        doNothing().when(familiaService).deleteFamilia(1L);


        mockMvc.perform(delete("/admin/familia/{idFamilia}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Familia eliminada con exito"));


        verify(familiaService).deleteFamilia(1L);
    }


    /** 🔹 Test: GET /admin/familia/buscar?nombre= */
    @Test
    void testBuscarPorNombre() throws Exception {
        Familia familia = new Familia();
        familia.setIdFamilia(2L);
        familia.setNombre("Familia Andina");


        when(familiaService.buscarPorNombre("Andina")).thenReturn(List.of(familia));


        mockMvc.perform(get("/admin/familia/buscar")
                        .param("nombre", "Andina"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Familia Andina"));


        verify(familiaService).buscarPorNombre("Andina");
    }
}
