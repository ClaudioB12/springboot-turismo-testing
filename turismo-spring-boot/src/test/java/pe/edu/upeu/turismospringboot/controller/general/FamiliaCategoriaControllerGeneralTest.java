package pe.edu.upeu.turismospringboot.controller.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pe.edu.upeu.turismospringboot.model.dto.FamiliaCategoriaDto;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.service.FamiliaCategoriaService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - FamiliaCategoriaControllerGeneral")
class FamiliaCategoriaControllerGeneralTest {

    @Mock
    private FamiliaCategoriaService familiaCategoriaService;

    @InjectMocks
    private FamiliaCategoriaControllerGeneral controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Listar relaciones - Debe retornar lista de FamiliaCategoriaDto")
    void listarRelaciones_DebeRetornarListaDeRelaciones() throws Exception {
        // Arrange
        FamiliaCategoriaDto dto1 = new FamiliaCategoriaDto();
        dto1.setIdFamiliaCategoria(1L);
        dto1.setIdFamilia(1L);
        dto1.setIdCategoria(1L);

        FamiliaCategoriaDto dto2 = new FamiliaCategoriaDto();
        dto2.setIdFamiliaCategoria(2L);
        dto2.setIdFamilia(2L);
        dto2.setIdCategoria(2L);

        List<FamiliaCategoriaDto> relaciones = Arrays.asList(dto1, dto2);
        when(familiaCategoriaService.listarRelaciones()).thenReturn(relaciones);

        // Act & Assert
        mockMvc.perform(get("/general/familiaCategoria")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idFamiliaCategoria").value(1L))
                .andExpect(jsonPath("$[1].idFamiliaCategoria").value(2L));
    }

    @Test
    @DisplayName("Listar relaciones - Debe retornar lista vacía cuando no hay datos")
    void listarRelaciones_DebeRetornarListaVacia() throws Exception {
        // Arrange
        when(familiaCategoriaService.listarRelaciones()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/general/familiaCategoria")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Obtener por ID Familia - Debe retornar relaciones de la familia")
    void obtenerPorIdFamilia_DebeRetornarRelacionesDeFamilia() throws Exception {
        // Arrange
        Long idFamilia = 1L;
        FamiliaCategoriaDto dto = new FamiliaCategoriaDto();
        dto.setIdFamiliaCategoria(1L);
        dto.setIdFamilia(idFamilia);
        dto.setIdCategoria(1L);

        List<FamiliaCategoriaDto> relaciones = Collections.singletonList(dto);
        when(familiaCategoriaService.obtenerFamiliaCategoriaPorIdFamilia(idFamilia))
                .thenReturn(relaciones);

        // Act & Assert
        mockMvc.perform(get("/general/familiaCategoria/familia/{idFamilia}", idFamilia)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idFamilia").value(idFamilia));
    }

    @Test
    @DisplayName("Obtener por ID Categoría - Debe retornar relaciones de la categoría")
    void obtenerPorIdCategoria_DebeRetornarRelacionesDeCategoria() throws Exception {
        // Arrange
        Long idCategoria = 1L;
        FamiliaCategoriaDto dto = new FamiliaCategoriaDto();
        dto.setIdFamiliaCategoria(1L);
        dto.setIdFamilia(1L);
        dto.setIdCategoria(idCategoria);

        List<FamiliaCategoriaDto> relaciones = Collections.singletonList(dto);
        when(familiaCategoriaService.obtenerFamiliaCategoriaPorIdCategoria(idCategoria))
                .thenReturn(relaciones);

        // Act & Assert
        mockMvc.perform(get("/general/familiaCategoria/categoria/{idCategoria}", idCategoria)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idCategoria").value(idCategoria));
    }

    @Test
    @DisplayName("Obtener emprendimientos - Debe retornar lista sin filtro de nombre")
    void obtenerEmprendimientos_SinFiltroNombre_DebeRetornarLista() throws Exception {
        // Arrange
        Long idFamiliaCategoria = 1L;
        Emprendimiento emp1 = new Emprendimiento();
        emp1.setIdEmprendimiento(1L);
        emp1.setNombre("Emprendimiento 1");

        Emprendimiento emp2 = new Emprendimiento();
        emp2.setIdEmprendimiento(2L);
        emp2.setNombre("Emprendimiento 2");

        List<Emprendimiento> emprendimientos = Arrays.asList(emp1, emp2);
        when(familiaCategoriaService.getEmprendimientosPorFamiliaCategoria(idFamiliaCategoria, null))
                .thenReturn(emprendimientos);

        // Act & Assert
        mockMvc.perform(get("/general/familiaCategoria/{idFamiliaCategoria}/emprendimientos",
                        idFamiliaCategoria)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idEmprendimiento").value(1L))
                .andExpect(jsonPath("$[1].idEmprendimiento").value(2L));
    }

    @Test
    @DisplayName("Obtener emprendimientos - Debe retornar lista con filtro de nombre")
    void obtenerEmprendimientos_ConFiltroNombre_DebeRetornarListaFiltrada() throws Exception {
        // Arrange
        Long idFamiliaCategoria = 1L;
        String nombreFiltro = "Artesanía";

        Emprendimiento emp = new Emprendimiento();
        emp.setIdEmprendimiento(1L);
        emp.setNombre("Artesanía Local");

        List<Emprendimiento> emprendimientos = Collections.singletonList(emp);
        when(familiaCategoriaService.getEmprendimientosPorFamiliaCategoria(idFamiliaCategoria, nombreFiltro))
                .thenReturn(emprendimientos);

        // Act & Assert
        mockMvc.perform(get("/general/familiaCategoria/{idFamiliaCategoria}/emprendimientos",
                        idFamiliaCategoria)
                        .param("nombre", nombreFiltro)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Artesanía Local"));
    }

    @Test
    @DisplayName("Obtener emprendimientos - Debe retornar lista vacía")
    void obtenerEmprendimientos_DebeRetornarListaVacia() throws Exception {
        // Arrange
        Long idFamiliaCategoria = 1L;
        when(familiaCategoriaService.getEmprendimientosPorFamiliaCategoria(idFamiliaCategoria, null))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/general/familiaCategoria/{idFamiliaCategoria}/emprendimientos",
                        idFamiliaCategoria)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}