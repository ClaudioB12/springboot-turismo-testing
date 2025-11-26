package pe.edu.upeu.turismospringboot.controller.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.upeu.turismospringboot.model.dto.UsuarioDtoUser;
import pe.edu.upeu.turismospringboot.model.dto.UsuarioIdMensajeDtoResponse;
import pe.edu.upeu.turismospringboot.model.entity.Persona;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.service.UsuarioCompletoService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioControllerUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioCompletoService usuarioCompletoService;

    private Usuario usuarioCompleto;

    @BeforeEach
    void setUp() {
        usuarioCompleto = new Usuario();
        usuarioCompleto.setIdUsuario(1L);
        usuarioCompleto.setUsername("testuser");

        Persona persona = new Persona();
        persona.setNombres("Juan");
        persona.setApellidos("Perez");

        usuarioCompleto.setPersona(persona);
    }

    // ===============================
    // GET /usuario/usuarioCompleto/{id}
    // ===============================
    @Test
    @WithMockUser(roles = "USUARIO")
    void debeObtenerUsuarioPorId() throws Exception {

        when(usuarioCompletoService.buscarUsuarioCompletoPorId(1L))
                .thenReturn(usuarioCompleto);

        mockMvc.perform(get("/usuario/usuarioCompleto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    // ===============================
    // PUT /usuario/usuarioCompleto/{id}
    // ===============================
    @Test
    @WithMockUser(roles = "USUARIO")
    void debeActualizarUsuarioCompleto() throws Exception {

        UsuarioDtoUser dto = new UsuarioDtoUser();
        dto.setUsername("nuevoUser");

        String json = objectMapper.writeValueAsString(dto);

        MockMultipartFile jsonPart = new MockMultipartFile(
                "usuario", "", "application/json", json.getBytes()
        );

        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "", "application/octet-stream", new byte[0]
        );

        when(usuarioCompletoService.actualizarUsuarioCompletoPorUsuario(
                eq(1L), any(UsuarioDtoUser.class), any(), any()))
                .thenReturn(usuarioCompleto);

        mockMvc.perform(multipart("/usuario/usuarioCompleto/1")
                        .file(jsonPart)
                        .file(emptyFile)
                        .with(request -> { request.setMethod("PUT"); return request; })
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    // ===============================
    // GET /usuario/usuarioCompleto/buscarIdPorUsername/{username}
    // ===============================
    @Test
    @WithMockUser(roles = "USUARIO")
    void debeBuscarIdPorUsername() throws Exception {

        UsuarioIdMensajeDtoResponse response = new UsuarioIdMensajeDtoResponse();
        response.setUsuarioId(15L);

        when(usuarioCompletoService.buscarIdUsuarioPorUsername("bustinza"))
                .thenReturn(response);

        mockMvc.perform(get("/usuario/usuarioCompleto/buscarIdPorUsername/bustinza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(15L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // ===============================
    // SEGURIDAD
    // ===============================
    @Test
    void debeDenegarAccesoSinAutenticacion() throws Exception {
        mockMvc.perform(get("/usuario/usuarioCompleto/1"))
                .andExpect(status().isForbidden());
    }
}
