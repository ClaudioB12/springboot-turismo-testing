package pe.edu.upeu.turismospringboot.controller.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.controller.admin.UsuarioCompletoController;
import pe.edu.upeu.turismospringboot.model.dto.UsuarioCompletoDto;
import pe.edu.upeu.turismospringboot.model.dto.UsuarioIdMensajeDtoResponse;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.service.UsuarioCompletoService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("UsuarioCompletoController - Pruebas Unitarias")
class UsuarioCompletoControllerTest {

    @Mock
    private UsuarioCompletoService usuarioCompletoService;

    @InjectMocks
    private UsuarioCompletoController usuarioCompletoController;

    private ObjectMapper objectMapper;
    private Usuario mockUsuario;
    private UsuarioCompletoDto mockUsuarioDto;
    private UsuarioIdMensajeDtoResponse mockUsuarioIdResponse;
    private List<Usuario> mockUsuariosList;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Mock Usuario Entity
        mockUsuario = mock(Usuario.class);
        when(mockUsuario.getIdUsuario()).thenReturn(1L);
        when(mockUsuario.getUsername()).thenReturn("testuser");

        // Mock UsuarioCompletoDto
        mockUsuarioDto = mock(UsuarioCompletoDto.class);

        // Mock UsuarioIdMensajeDtoResponse
        mockUsuarioIdResponse = mock(UsuarioIdMensajeDtoResponse.class);

        // Mock List de Usuarios
        Usuario usuario2 = mock(Usuario.class);
        when(usuario2.getIdUsuario()).thenReturn(2L);
        when(usuario2.getUsername()).thenReturn("user2");

        Usuario usuario3 = mock(Usuario.class);
        when(usuario3.getIdUsuario()).thenReturn(3L);
        when(usuario3.getUsername()).thenReturn("user3");

        mockUsuariosList = Arrays.asList(mockUsuario, usuario2, usuario3);

        // Mock MultipartFile
        mockFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    // ==========================================
    // TESTS: GET /admin/usuarioCompleto
    // ==========================================

    @Test
    @DisplayName("GET / - Debe obtener todos los usuarios completos")
    void testGetUsuarioCompleto_Success() {
        // Given
        when(usuarioCompletoService.listarUsuarioCompleto()).thenReturn(mockUsuariosList);

        // When
        ResponseEntity<List<Usuario>> response = usuarioCompletoController.getUsuarioCompleto();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());

        verify(usuarioCompletoService, times(1)).listarUsuarioCompleto();
    }

    @Test
    @DisplayName("GET / - Debe retornar lista vacía si no hay usuarios")
    void testGetUsuarioCompleto_EmptyList() {
        // Given
        when(usuarioCompletoService.listarUsuarioCompleto()).thenReturn(Arrays.asList());

        // When
        ResponseEntity<List<Usuario>> response = usuarioCompletoController.getUsuarioCompleto();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET / - Debe llamar al servicio exactamente una vez")
    void testGetUsuarioCompleto_CallsServiceOnce() {
        // Given
        when(usuarioCompletoService.listarUsuarioCompleto()).thenReturn(mockUsuariosList);

        // When
        usuarioCompletoController.getUsuarioCompleto();

        // Then
        verify(usuarioCompletoService, times(1)).listarUsuarioCompleto();
        verifyNoMoreInteractions(usuarioCompletoService);
    }

    // ==========================================
    // TESTS: GET /admin/usuarioCompleto/{id}
    // ==========================================

    @Test
    @DisplayName("GET /{id} - Debe obtener usuario por ID")
    void testGetUsuarioCompletoById_Success() {
        // Given
        Long id = 1L;
        when(usuarioCompletoService.buscarUsuarioCompletoPorId(id)).thenReturn(mockUsuario);

        // When
        ResponseEntity<Usuario> response = usuarioCompletoController.getUsuarioCompletoById(id);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getIdUsuario());

        verify(usuarioCompletoService, times(1)).buscarUsuarioCompletoPorId(id);
    }

    @Test
    @DisplayName("GET /{id} - Debe llamar al servicio con el ID correcto")
    void testGetUsuarioCompletoById_CallsServiceWithCorrectId() {
        // Given
        Long id = 5L;
        when(usuarioCompletoService.buscarUsuarioCompletoPorId(id)).thenReturn(mockUsuario);

        // When
        usuarioCompletoController.getUsuarioCompletoById(id);

        // Then
        verify(usuarioCompletoService).buscarUsuarioCompletoPorId(eq(id));
    }

    @Test
    @DisplayName("GET /{id} - Debe retornar el usuario correcto")
    void testGetUsuarioCompletoById_ReturnsCorrectUser() {
        // Given
        Long id = 1L;
        when(usuarioCompletoService.buscarUsuarioCompletoPorId(id)).thenReturn(mockUsuario);

        // When
        ResponseEntity<Usuario> response = usuarioCompletoController.getUsuarioCompletoById(id);

        // Then
        assertThat(response.getBody()).isEqualTo(mockUsuario);
    }

    // ==========================================
    // TESTS: POST /admin/usuarioCompleto
    // ==========================================

    @Test
    @DisplayName("POST / - Debe crear usuario con archivo")
    void testInsertUsuarioCompleto_WithFile_Success() throws Exception {
        // Given
        String usuarioJson = objectMapper.writeValueAsString(mockUsuarioDto);
        when(usuarioCompletoService.crearUsuarioCompleto(any(UsuarioCompletoDto.class), any(MultipartFile.class)))
                .thenReturn(mockUsuario);

        // When
        ResponseEntity<Usuario> response = usuarioCompletoController.insertUsuarioCompleto(
                usuarioJson,
                mockFile
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(usuarioCompletoService, times(1))
                .crearUsuarioCompleto(any(UsuarioCompletoDto.class), any(MultipartFile.class));
    }

    @Test
    @DisplayName("POST / - Debe crear usuario sin archivo")
    void testInsertUsuarioCompleto_WithoutFile_Success() throws Exception {
        // Given
        String usuarioJson = objectMapper.writeValueAsString(mockUsuarioDto);
        when(usuarioCompletoService.crearUsuarioCompleto(any(UsuarioCompletoDto.class), isNull()))
                .thenReturn(mockUsuario);

        // When
        ResponseEntity<Usuario> response = usuarioCompletoController.insertUsuarioCompleto(
                usuarioJson,
                null
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(usuarioCompletoService, times(1))
                .crearUsuarioCompleto(any(UsuarioCompletoDto.class), isNull());
    }

    @Test
    @DisplayName("POST / - Debe retornar 500 si ocurre un error de JSON")
    void testInsertUsuarioCompleto_JsonError() {
        // Given
        String invalidJson = "{invalid json";

        // When
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        ResponseEntity<Usuario> response = usuarioCompletoController.insertUsuarioCompleto(
                invalidJson,
                mockFile
        );

        System.setErr(originalErr);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        verify(usuarioCompletoService, never())
                .crearUsuarioCompleto(any(UsuarioCompletoDto.class), any(MultipartFile.class));
    }

    @Test
    @DisplayName("POST / - Debe manejar excepción del servicio")
    void testInsertUsuarioCompleto_ServiceException() throws Exception {
        // Given
        String usuarioJson = objectMapper.writeValueAsString(mockUsuarioDto);
        when(usuarioCompletoService.crearUsuarioCompleto(any(UsuarioCompletoDto.class), any(MultipartFile.class)))
                .thenThrow(new RuntimeException("Error al crear"));

        // When
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        ResponseEntity<Usuario> response = usuarioCompletoController.insertUsuarioCompleto(
                usuarioJson,
                mockFile
        );

        System.setErr(originalErr);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("POST / - Debe retornar status CREATED")
    void testInsertUsuarioCompleto_ReturnsCreatedStatus() throws Exception {
        // Given
        String usuarioJson = objectMapper.writeValueAsString(mockUsuarioDto);
        when(usuarioCompletoService.crearUsuarioCompleto(any(UsuarioCompletoDto.class), any(MultipartFile.class)))
                .thenReturn(mockUsuario);

        // When
        ResponseEntity<Usuario> response = usuarioCompletoController.insertUsuarioCompleto(
                usuarioJson,
                mockFile
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    // ==========================================
    // TESTS: PUT /admin/usuarioCompleto/{id}
    // ==========================================

    @Test
    @DisplayName("PUT /{id} - Debe actualizar usuario con archivo")
    void testUpdateUsuarioCompleto_WithFile_Success() throws Exception {
        // Given
        Long id = 1L;
        String usuarioJson = objectMapper.writeValueAsString(mockUsuarioDto);
        when(usuarioCompletoService.actualizarUsuarioCompleto(eq(id), any(UsuarioCompletoDto.class), any(MultipartFile.class)))
                .thenReturn(mockUsuario);

        // When
        ResponseEntity<Usuario> response = usuarioCompletoController.updateUsuarioCompleto(
                id,
                usuarioJson,
                mockFile
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(usuarioCompletoService, times(1))
                .actualizarUsuarioCompleto(eq(id), any(UsuarioCompletoDto.class), any(MultipartFile.class));
    }

    @Test
    @DisplayName("PUT /{id} - Debe actualizar usuario sin archivo")
    void testUpdateUsuarioCompleto_WithoutFile_Success() throws Exception {
        // Given
        Long id = 1L;
        String usuarioJson = objectMapper.writeValueAsString(mockUsuarioDto);
        when(usuarioCompletoService.actualizarUsuarioCompleto(eq(id), any(UsuarioCompletoDto.class), isNull()))
                .thenReturn(mockUsuario);

        // When
        ResponseEntity<Usuario> response = usuarioCompletoController.updateUsuarioCompleto(
                id,
                usuarioJson,
                null
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(usuarioCompletoService, times(1))
                .actualizarUsuarioCompleto(eq(id), any(UsuarioCompletoDto.class), isNull());
    }

    @Test
    @DisplayName("PUT /{id} - Debe retornar 500 si ocurre un error")
    void testUpdateUsuarioCompleto_Error() {
        // Given
        Long id = 1L;
        String invalidJson = "{invalid json";

        // When
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        ResponseEntity<Usuario> response = usuarioCompletoController.updateUsuarioCompleto(
                id,
                invalidJson,
                mockFile
        );

        System.setErr(originalErr);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("PUT /{id} - Debe manejar excepción del servicio")
    void testUpdateUsuarioCompleto_ServiceException() throws Exception {
        // Given
        Long id = 1L;
        String usuarioJson = objectMapper.writeValueAsString(mockUsuarioDto);
        when(usuarioCompletoService.actualizarUsuarioCompleto(eq(id), any(UsuarioCompletoDto.class), any(MultipartFile.class)))
                .thenThrow(new RuntimeException("Error al actualizar"));

        // When
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        ResponseEntity<Usuario> response = usuarioCompletoController.updateUsuarioCompleto(
                id,
                usuarioJson,
                mockFile
        );

        System.setErr(originalErr);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("PUT /{id} - Debe usar el ID correcto en el path")
    void testUpdateUsuarioCompleto_UsesCorrectId() throws Exception {
        // Given
        Long id = 99L;
        String usuarioJson = objectMapper.writeValueAsString(mockUsuarioDto);
        when(usuarioCompletoService.actualizarUsuarioCompleto(eq(id), any(UsuarioCompletoDto.class), any(MultipartFile.class)))
                .thenReturn(mockUsuario);

        // When
        usuarioCompletoController.updateUsuarioCompleto(id, usuarioJson, mockFile);

        // Then
        verify(usuarioCompletoService).actualizarUsuarioCompleto(eq(id), any(UsuarioCompletoDto.class), any(MultipartFile.class));
    }

    // ==========================================
    // TESTS: DELETE /admin/usuarioCompleto/{id}
    // ==========================================

    @Test
    @DisplayName("DELETE /{id} - Debe eliminar usuario correctamente")
    void testDeleteUsuarioCompleto_Success() {
        // Given
        Long id = 1L;
        doNothing().when(usuarioCompletoService).eliminarUsuarioCompleto(id);

        // When
        ResponseEntity<String> response = usuarioCompletoController.deleteUsuarioCompleto(id);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario eliminado correctamente", response.getBody());

        verify(usuarioCompletoService, times(1)).eliminarUsuarioCompleto(id);
    }

    @Test
    @DisplayName("DELETE /{id} - Debe retornar 404 si usuario no existe")
    void testDeleteUsuarioCompleto_NotFound() {
        // Given
        Long id = 999L;
        doThrow(new EntityNotFoundException("Usuario no encontrado"))
                .when(usuarioCompletoService).eliminarUsuarioCompleto(id);

        // When
        ResponseEntity<String> response = usuarioCompletoController.deleteUsuarioCompleto(id);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuario no encontrado", response.getBody());
    }

    @Test
    @DisplayName("DELETE /{id} - Debe retornar 500 si ocurre un error genérico")
    void testDeleteUsuarioCompleto_InternalError() {
        // Given
        Long id = 1L;
        doThrow(new RuntimeException("Error al eliminar"))
                .when(usuarioCompletoService).eliminarUsuarioCompleto(id);

        // When
        ResponseEntity<String> response = usuarioCompletoController.deleteUsuarioCompleto(id);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al eliminar usuario", response.getBody());
    }

    @Test
    @DisplayName("DELETE /{id} - Debe llamar al servicio con el ID correcto")
    void testDeleteUsuarioCompleto_CallsServiceWithCorrectId() {
        // Given
        Long id = 99L;
        doNothing().when(usuarioCompletoService).eliminarUsuarioCompleto(id);

        // When
        usuarioCompletoController.deleteUsuarioCompleto(id);

        // Then
        verify(usuarioCompletoService).eliminarUsuarioCompleto(eq(id));
    }

    // ==========================================
    // TESTS: GET /admin/usuarioCompleto/buscar
    // ==========================================

    @Test
    @DisplayName("GET /buscar - Debe buscar usuarios por username")
    void testBuscarPorUsername_Success() {
        // Given
        String username = "test";
        when(usuarioCompletoService.buscarUsuariosPorUsername(username)).thenReturn(mockUsuariosList);

        // When
        ResponseEntity<List<Usuario>> response = usuarioCompletoController.buscarPorUsername(username);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());

        verify(usuarioCompletoService, times(1)).buscarUsuariosPorUsername(username);
    }

    @Test
    @DisplayName("GET /buscar - Debe retornar lista vacía si no encuentra resultados")
    void testBuscarPorUsername_NoResults() {
        // Given
        String username = "noexiste";
        when(usuarioCompletoService.buscarUsuariosPorUsername(username)).thenReturn(Arrays.asList());

        // When
        ResponseEntity<List<Usuario>> response = usuarioCompletoController.buscarPorUsername(username);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /buscar - Debe llamar al servicio con el username correcto")
    void testBuscarPorUsername_CallsServiceWithCorrectUsername() {
        // Given
        String username = "testuser";
        when(usuarioCompletoService.buscarUsuariosPorUsername(username)).thenReturn(Arrays.asList());

        // When
        usuarioCompletoController.buscarPorUsername(username);

        // Then
        verify(usuarioCompletoService).buscarUsuariosPorUsername(eq(username));
    }

    // ==========================================
    // TESTS: GET /admin/usuarioCompleto/buscarIdPorUsername/{userName}
    // ==========================================

    @Test
    @DisplayName("GET /buscarIdPorUsername/{userName} - Debe buscar ID por username")
    void testBuscarIdPorUsername_Success() {
        // Given
        String userName = "testuser";
        when(usuarioCompletoService.buscarIdUsuarioPorUsername(userName)).thenReturn(mockUsuarioIdResponse);

        // When
        ResponseEntity<UsuarioIdMensajeDtoResponse> response =
                usuarioCompletoController.buscarIdPorUsername(userName);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(usuarioCompletoService, times(1)).buscarIdUsuarioPorUsername(userName);
    }

    @Test
    @DisplayName("GET /buscarIdPorUsername/{userName} - Debe llamar al servicio con username correcto")
    void testBuscarIdPorUsername_CallsServiceWithCorrectUsername() {
        // Given
        String userName = "admin";
        when(usuarioCompletoService.buscarIdUsuarioPorUsername(userName)).thenReturn(mockUsuarioIdResponse);

        // When
        usuarioCompletoController.buscarIdPorUsername(userName);

        // Then
        verify(usuarioCompletoService).buscarIdUsuarioPorUsername(eq(userName));
    }

    @Test
    @DisplayName("GET /buscarIdPorUsername/{userName} - Debe retornar el DTO correcto")
    void testBuscarIdPorUsername_ReturnsCorrectDto() {
        // Given
        String userName = "testuser";
        when(usuarioCompletoService.buscarIdUsuarioPorUsername(userName)).thenReturn(mockUsuarioIdResponse);

        // When
        ResponseEntity<UsuarioIdMensajeDtoResponse> response =
                usuarioCompletoController.buscarIdPorUsername(userName);

        // Then
        assertThat(response.getBody()).isEqualTo(mockUsuarioIdResponse);
    }

    // ==========================================
    // TESTS: Validación General
    // ==========================================

    @Test
    @DisplayName("Debe tener el servicio inyectado correctamente")
    void testServiceInjection() {
        // Then
        assertNotNull(usuarioCompletoService);
    }

    @Test
    @DisplayName("Debe retornar ResponseEntity no nulo en todas las operaciones")
    void testAllEndpointsReturnNonNullResponse() throws Exception {
        // Given
        when(usuarioCompletoService.listarUsuarioCompleto()).thenReturn(mockUsuariosList);
        when(usuarioCompletoService.buscarUsuarioCompletoPorId(anyLong())).thenReturn(mockUsuario);
        when(usuarioCompletoService.crearUsuarioCompleto(any(), any())).thenReturn(mockUsuario);
        when(usuarioCompletoService.buscarUsuariosPorUsername(anyString())).thenReturn(mockUsuariosList);
        when(usuarioCompletoService.buscarIdUsuarioPorUsername(anyString())).thenReturn(mockUsuarioIdResponse);
        doNothing().when(usuarioCompletoService).eliminarUsuarioCompleto(anyLong());

        String usuarioJson = objectMapper.writeValueAsString(mockUsuarioDto);

        // When & Then
        assertNotNull(usuarioCompletoController.getUsuarioCompleto());
        assertNotNull(usuarioCompletoController.getUsuarioCompletoById(1L));
        assertNotNull(usuarioCompletoController.insertUsuarioCompleto(usuarioJson, mockFile));
        assertNotNull(usuarioCompletoController.updateUsuarioCompleto(1L, usuarioJson, mockFile));
        assertNotNull(usuarioCompletoController.deleteUsuarioCompleto(1L));
        assertNotNull(usuarioCompletoController.buscarPorUsername("test"));
        assertNotNull(usuarioCompletoController.buscarIdPorUsername("test"));
    }
}