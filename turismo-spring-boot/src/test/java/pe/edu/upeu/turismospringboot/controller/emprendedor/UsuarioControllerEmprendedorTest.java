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
import pe.edu.upeu.turismospringboot.model.dto.UsuarioDtoUser;
import pe.edu.upeu.turismospringboot.model.dto.UsuarioIdMensajeDtoResponse;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.service.UsuarioCompletoService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsuarioControllerEmprendedorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioCompletoService usuarioCompletoService;

    private Usuario usuario;
    private UsuarioDtoUser usuarioDtoUser;
    private UsuarioIdMensajeDtoResponse usuarioIdMensajeDto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuarioDtoUser = new UsuarioDtoUser();
        usuarioIdMensajeDto = new UsuarioIdMensajeDtoResponse();
    }

    // ============================
    // GET /emprendedor/usuarioCompleto/{idUsuario}
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeObtenerUsuarioPorId() throws Exception {
        when(usuarioCompletoService.buscarUsuarioCompletoPorId(1L))
                .thenReturn(usuario);

        mockMvc.perform(get("/emprendedor/usuarioCompleto/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(usuarioCompletoService, times(1))
                .buscarUsuarioCompletoPorId(1L);
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeObtenerUsuarioPorIdDiferente() throws Exception {
        when(usuarioCompletoService.buscarUsuarioCompletoPorId(5L))
                .thenReturn(usuario);

        mockMvc.perform(get("/emprendedor/usuarioCompleto/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(usuarioCompletoService, times(1))
                .buscarUsuarioCompletoPorId(5L);
    }

    // ============================
    // PUT /emprendedor/usuarioCompleto/{idUsuario}
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeActualizarUsuarioConArchivo() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "avatar de prueba".getBytes()
        );

        MockMultipartFile usuarioJson = new MockMultipartFile(
                "usuario",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(usuarioDtoUser)
        );

        when(usuarioCompletoService.actualizarUsuarioCompletoPorUsuario(
                eq(1L),
                any(UsuarioDtoUser.class),
                any(),
                isNull()))
                .thenReturn(usuario);

        mockMvc.perform(multipart("/emprendedor/usuarioCompleto/1")
                        .file(file)
                        .file(usuarioJson)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        verify(usuarioCompletoService, times(1))
                .actualizarUsuarioCompletoPorUsuario(
                        eq(1L),
                        any(UsuarioDtoUser.class),
                        any(),
                        isNull());
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeActualizarUsuarioSinArchivo() throws Exception {
        MockMultipartFile usuarioJson = new MockMultipartFile(
                "usuario",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(usuarioDtoUser)
        );

        when(usuarioCompletoService.actualizarUsuarioCompletoPorUsuario(
                eq(1L),
                any(UsuarioDtoUser.class),
                isNull(),
                isNull()))
                .thenReturn(usuario);

        mockMvc.perform(multipart("/emprendedor/usuarioCompleto/1")
                        .file(usuarioJson)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        verify(usuarioCompletoService, times(1))
                .actualizarUsuarioCompletoPorUsuario(
                        eq(1L),
                        any(UsuarioDtoUser.class),
                        isNull(),
                        isNull());
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeActualizarDiferentesUsuarios() throws Exception {
        MockMultipartFile usuarioJson = new MockMultipartFile(
                "usuario",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(usuarioDtoUser)
        );

        when(usuarioCompletoService.actualizarUsuarioCompletoPorUsuario(
                anyLong(),
                any(UsuarioDtoUser.class),
                any(),
                isNull()))
                .thenReturn(usuario);

        mockMvc.perform(multipart("/emprendedor/usuarioCompleto/1")
                        .file(usuarioJson)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        mockMvc.perform(multipart("/emprendedor/usuarioCompleto/2")
                        .file(usuarioJson)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        verify(usuarioCompletoService, times(1))
                .actualizarUsuarioCompletoPorUsuario(eq(1L), any(), any(), isNull());
        verify(usuarioCompletoService, times(1))
                .actualizarUsuarioCompletoPorUsuario(eq(2L), any(), any(), isNull());
    }

    // ============================
    // GET /emprendedor/usuarioCompleto/buscarIdPorUsername/{userName}
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeBuscarIdPorUsername() throws Exception {
        when(usuarioCompletoService.buscarIdUsuarioPorUsername("turista1"))
                .thenReturn(usuarioIdMensajeDto);

        mockMvc.perform(get("/emprendedor/usuarioCompleto/buscarIdPorUsername/turista1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(usuarioCompletoService, times(1))
                .buscarIdUsuarioPorUsername("turista1");
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeBuscarIdPorUsernameDiferente() throws Exception {
        when(usuarioCompletoService.buscarIdUsuarioPorUsername("emprendedor2"))
                .thenReturn(usuarioIdMensajeDto);

        mockMvc.perform(get("/emprendedor/usuarioCompleto/buscarIdPorUsername/emprendedor2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(usuarioCompletoService, times(1))
                .buscarIdUsuarioPorUsername("emprendedor2");
    }

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeBuscarMultiplesUsuariosPorUsername() throws Exception {
        when(usuarioCompletoService.buscarIdUsuarioPorUsername(anyString()))
                .thenReturn(usuarioIdMensajeDto);

        mockMvc.perform(get("/emprendedor/usuarioCompleto/buscarIdPorUsername/usuario1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/emprendedor/usuarioCompleto/buscarIdPorUsername/usuario2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/emprendedor/usuarioCompleto/buscarIdPorUsername/usuario3"))
                .andExpect(status().isOk());

        verify(usuarioCompletoService, times(1))
                .buscarIdUsuarioPorUsername("usuario1");
        verify(usuarioCompletoService, times(1))
                .buscarIdUsuarioPorUsername("usuario2");
        verify(usuarioCompletoService, times(1))
                .buscarIdUsuarioPorUsername("usuario3");
    }

    // ============================
    // Seguridad
    // ============================

    @Test
    void debeDenegarAccesoSinAutenticacionAlObtenerUsuario() throws Exception {
        mockMvc.perform(get("/emprendedor/usuarioCompleto/1"))
                .andExpect(status().isForbidden());

        verify(usuarioCompletoService, never())
                .buscarUsuarioCompletoPorId(any());
    }

    @Test
    void debeDenegarAccesoSinAutenticacionAlBuscarPorUsername() throws Exception {
        mockMvc.perform(get("/emprendedor/usuarioCompleto/buscarIdPorUsername/turista1"))
                .andExpect(status().isForbidden());

        verify(usuarioCompletoService, never())
                .buscarIdUsuarioPorUsername(any());
    }

    @Test
    @WithMockUser(username = "turista1", roles = {"TURISTA"})
    void debeDenegarAccesoConRolIncorrectoAlObtenerUsuario() throws Exception {
        mockMvc.perform(get("/emprendedor/usuarioCompleto/1"))
                .andExpect(status().isForbidden());

        verify(usuarioCompletoService, never())
                .buscarUsuarioCompletoPorId(any());
    }

    @Test
    @WithMockUser(username = "turista1", roles = {"TURISTA"})
    void debeDenegarAccesoConRolIncorrectoAlActualizar() throws Exception {
        MockMultipartFile usuarioJson = new MockMultipartFile(
                "usuario",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(usuarioDtoUser)
        );

        mockMvc.perform(multipart("/emprendedor/usuarioCompleto/1")
                        .file(usuarioJson)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isForbidden());

        verify(usuarioCompletoService, never())
                .actualizarUsuarioCompletoPorUsuario(any(), any(), any(), any());
    }

    @Test
    @WithMockUser(username = "turista1", roles = {"TURISTA"})
    void debeDenegarAccesoConRolIncorrectoAlBuscarPorUsername() throws Exception {
        mockMvc.perform(get("/emprendedor/usuarioCompleto/buscarIdPorUsername/turista1"))
                .andExpect(status().isForbidden());

        verify(usuarioCompletoService, never())
                .buscarIdUsuarioPorUsername(any());
    }

    // ============================
    // Casos adicionales
    // ============================

    @Test
    @WithMockUser(username = "emprendedor1", roles = {"EMPRENDEDOR"})
    void debeActualizarUsuarioConArchivoGrande() throws Exception {
        byte[] contenidoGrande = new byte[1024 * 1024]; // 1MB
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar_grande.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                contenidoGrande
        );

        MockMultipartFile usuarioJson = new MockMultipartFile(
                "usuario",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(usuarioDtoUser)
        );

        when(usuarioCompletoService.actualizarUsuarioCompletoPorUsuario(
                eq(1L),
                any(UsuarioDtoUser.class),
                any(),
                isNull()))
                .thenReturn(usuario);

        mockMvc.perform(multipart("/emprendedor/usuarioCompleto/1")
                        .file(file)
                        .file(usuarioJson)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        verify(usuarioCompletoService, times(1))
                .actualizarUsuarioCompletoPorUsuario(
                        eq(1L),
                        any(UsuarioDtoUser.class),
                        any(),
                        isNull());
    }
}