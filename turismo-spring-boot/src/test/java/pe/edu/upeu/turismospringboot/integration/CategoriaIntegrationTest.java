package pe.edu.upeu.turismospringboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.turismospringboot.model.dto.CategoriaDto;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.repository.CategoriaRepository;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Tests de Integración de Categoría")
class CategoriaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoriaDto categoriaDto;

    @BeforeEach
    void setUp() {
        categoriaDto = new CategoriaDto();
        categoriaDto.setNombre("Test Categoría");
        categoriaDto.setDescripcion("Descripción de prueba");
    }

    @Test
    @Order(1)
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Integración: Debe crear una categoría completa")
    @Transactional
    void testCrearCategoria_Completo() throws Exception {
        String categoriaJson = objectMapper.writeValueAsString(categoriaDto);
        MockMultipartFile categoriaPart = new MockMultipartFile(
                "categoria",
                "",
                "application/json",
                categoriaJson.getBytes(StandardCharsets.UTF_8) // ← UTF-8
        );

        mockMvc.perform(multipart("/admin/categoria")
                        .file(categoriaPart)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCategoria").exists())
                .andExpect(jsonPath("$.nombre", is("Test Categoría")))
                .andExpect(jsonPath("$.descripcion", is("Descripción de prueba")))
                .andExpect(jsonPath("$.fechaCreacionCategoria").exists());
    }

    @Test
    @Order(2)
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Integración: Debe obtener todas las categorías")
    void testObtenerTodasLasCategorias() throws Exception {
        mockMvc.perform(get("/admin/categoria")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    @Order(3)
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Integración: Debe buscar categoría por nombre")
    void testBuscarCategoriaPorNombre() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNombre("Categoría Búsqueda");
        categoria.setDescripcion("Para prueba de búsqueda");
        categoriaRepository.save(categoria);

        mockMvc.perform(get("/admin/categoria/buscar")
                        .param("nombre", "Búsqueda")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre", containsString("Búsqueda")));
    }

    @Test
    @Order(4)
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Integración: Ciclo completo CRUD")
    @Transactional
    void testCicloCompletoCRUD() throws Exception {
        // 1. CREATE
        String categoriaJson = objectMapper.writeValueAsString(categoriaDto);
        MockMultipartFile categoriaPart = new MockMultipartFile(
                "categoria",
                "",
                "application/json",
                categoriaJson.getBytes(StandardCharsets.UTF_8) // ← UTF-8
        );

        String responseCreate = mockMvc.perform(multipart("/admin/categoria")
                        .file(categoriaPart)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Categoria createdCategoria = objectMapper.readValue(responseCreate, Categoria.class);
        Long id = createdCategoria.getIdCategoria();

        // 2. READ
        mockMvc.perform(get("/admin/categoria/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria", is(id.intValue())))
                .andExpect(jsonPath("$.nombre", is("Test Categoría")));

        // 3. UPDATE
        CategoriaDto updateDto = new CategoriaDto();
        updateDto.setNombre("Test Categoría Actualizada");
        updateDto.setDescripcion("Descripción actualizada");

        String updateJson = objectMapper.writeValueAsString(updateDto);
        MockMultipartFile updatePart = new MockMultipartFile(
                "categoria",
                "",
                "application/json",
                updateJson.getBytes(StandardCharsets.UTF_8) // ← UTF-8
        );

        mockMvc.perform(multipart("/admin/categoria/" + id)
                        .file(updatePart)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Test Categoría Actualizada")))
                .andExpect(jsonPath("$.fechaModificacionCategoria").exists());

        // 4. DELETE
        mockMvc.perform(delete("/admin/categoria/" + id)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Integración: Debe manejar búsqueda sin resultados")
    void testBusquedaSinResultados() throws Exception {
        mockMvc.perform(get("/admin/categoria/buscar")
                        .param("nombre", "NoExisteEstaCategoria12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
