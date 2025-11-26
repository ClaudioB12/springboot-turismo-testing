package pe.edu.upeu.turismospringboot.controller.emprendedor;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import pe.edu.upeu.turismospringboot.controller.emprendedor.EmprendimientoControllerEmprendedor;
import pe.edu.upeu.turismospringboot.model.dto.EmprendimientoDto;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.service.EmprendimientoService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("EmprendimientoControllerEmprendedor - Pruebas Unitarias")
class EmprendimientoControllerEmprendedorTest {

    @Mock
    private EmprendimientoService emprendimientoService;

    @InjectMocks
    private EmprendimientoControllerEmprendedor emprendimientoControllerEmprendedor;

    private ObjectMapper objectMapper;
    private Emprendimiento mockEmprendimiento;
    private EmprendimientoDto mockEmprendimientoDto;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Mock Emprendimiento Entity
        mockEmprendimiento = mock(Emprendimiento.class);
        when(mockEmprendimiento.getIdEmprendimiento()).thenReturn(1L);
        when(mockEmprendimiento.getNombre()).thenReturn("Emprendimiento Test");

        // Mock EmprendimientoDto
        mockEmprendimientoDto = mock(EmprendimientoDto.class);

        // Mock MultipartFile
        mockFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    // ==========================================
    // TESTS: GET /emprendedor/emprendimiento/usuario/{idUsuario}
    // ==========================================

    @Test
    @DisplayName("GET /usuario/{idUsuario} - Debe obtener emprendimiento por ID de usuario")
    void testGetEmprendimientoByIdUsuario_Success() {
        // Given
        Long idUsuario = 1L;
        when(emprendimientoService.buscarPorIdUsuario(idUsuario)).thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response =
                emprendimientoControllerEmprendedor.getEmprendimientoByIdUsuario(idUsuario);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockEmprendimiento, response.getBody());

        verify(emprendimientoService, times(1)).buscarPorIdUsuario(idUsuario);
    }

    @Test
    @DisplayName("GET /usuario/{idUsuario} - Debe llamar al servicio con el ID correcto")
    void testGetEmprendimientoByIdUsuario_CallsServiceWithCorrectId() {
        // Given
        Long idUsuario = 5L;
        when(emprendimientoService.buscarPorIdUsuario(idUsuario)).thenReturn(mockEmprendimiento);

        // When
        emprendimientoControllerEmprendedor.getEmprendimientoByIdUsuario(idUsuario);

        // Then
        verify(emprendimientoService).buscarPorIdUsuario(eq(idUsuario));
    }

    @Test
    @DisplayName("GET /usuario/{idUsuario} - Debe retornar status OK")
    void testGetEmprendimientoByIdUsuario_ReturnsOkStatus() {
        // Given
        Long idUsuario = 1L;
        when(emprendimientoService.buscarPorIdUsuario(idUsuario)).thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response =
                emprendimientoControllerEmprendedor.getEmprendimientoByIdUsuario(idUsuario);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("GET /usuario/{idUsuario} - Debe retornar el emprendimiento correcto")
    void testGetEmprendimientoByIdUsuario_ReturnsCorrectEmprendimiento() {
        // Given
        Long idUsuario = 1L;
        when(emprendimientoService.buscarPorIdUsuario(idUsuario)).thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response =
                emprendimientoControllerEmprendedor.getEmprendimientoByIdUsuario(idUsuario);

        // Then
        assertThat(response.getBody()).isEqualTo(mockEmprendimiento);
    }

    @Test
    @DisplayName("GET /usuario/{idUsuario} - Debe llamar al servicio exactamente una vez")
    void testGetEmprendimientoByIdUsuario_CallsServiceOnce() {
        // Given
        Long idUsuario = 1L;
        when(emprendimientoService.buscarPorIdUsuario(idUsuario)).thenReturn(mockEmprendimiento);

        // When
        emprendimientoControllerEmprendedor.getEmprendimientoByIdUsuario(idUsuario);

        // Then
        verify(emprendimientoService, times(1)).buscarPorIdUsuario(anyLong());
        verifyNoMoreInteractions(emprendimientoService);
    }

    @Test
    @DisplayName("GET /usuario/{idUsuario} - Debe manejar diferentes IDs de usuario")
    void testGetEmprendimientoByIdUsuario_DifferentUserIds() {
        // Given
        Long idUsuario1 = 1L;
        Long idUsuario2 = 2L;

        Emprendimiento emprendimiento2 = mock(Emprendimiento.class);
        when(emprendimiento2.getIdEmprendimiento()).thenReturn(2L);

        when(emprendimientoService.buscarPorIdUsuario(idUsuario1)).thenReturn(mockEmprendimiento);
        when(emprendimientoService.buscarPorIdUsuario(idUsuario2)).thenReturn(emprendimiento2);

        // When
        ResponseEntity<Emprendimiento> response1 =
                emprendimientoControllerEmprendedor.getEmprendimientoByIdUsuario(idUsuario1);
        ResponseEntity<Emprendimiento> response2 =
                emprendimientoControllerEmprendedor.getEmprendimientoByIdUsuario(idUsuario2);

        // Then
        assertNotNull(response1.getBody());
        assertNotNull(response2.getBody());
        assertNotEquals(response1.getBody(), response2.getBody());

        verify(emprendimientoService, times(1)).buscarPorIdUsuario(idUsuario1);
        verify(emprendimientoService, times(1)).buscarPorIdUsuario(idUsuario2);
    }

    // ==========================================
    // TESTS: PUT /emprendedor/emprendimiento/{idEmprendimiento}
    // ==========================================

    @Test
    @DisplayName("PUT /{idEmprendimiento} - Debe actualizar emprendimiento con archivo")
    void testUpdateEmprendimiento_WithFile_Success() throws Exception {
        // Given
        Long idEmprendimiento = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response = emprendimientoControllerEmprendedor.updateEmprendimiento(
                idEmprendimiento,
                emprendimientoJson,
                mockFile
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockEmprendimiento, response.getBody());

        verify(emprendimientoService, times(1))
                .putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class));
    }

    @Test
    @DisplayName("PUT /{idEmprendimiento} - Debe actualizar emprendimiento sin archivo")
    void testUpdateEmprendimiento_WithoutFile_Success() throws Exception {
        // Given
        Long idEmprendimiento = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), isNull()))
                .thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response = emprendimientoControllerEmprendedor.updateEmprendimiento(
                idEmprendimiento,
                emprendimientoJson,
                null
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(emprendimientoService, times(1))
                .putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), isNull());
    }

    @Test
    @DisplayName("PUT /{idEmprendimiento} - Debe retornar 500 si ocurre un error de JSON")
    void testUpdateEmprendimiento_JsonError() {
        // Given
        Long idEmprendimiento = 1L;
        String invalidJson = "{invalid json";

        // When
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        ResponseEntity<Emprendimiento> response = emprendimientoControllerEmprendedor.updateEmprendimiento(
                idEmprendimiento,
                invalidJson,
                mockFile
        );

        System.setErr(originalErr);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        verify(emprendimientoService, never())
                .putEmprendimiento(anyLong(), any(EmprendimientoDto.class), any(MultipartFile.class));
    }

    @Test
    @DisplayName("PUT /{idEmprendimiento} - Debe manejar excepción del servicio")
    void testUpdateEmprendimiento_ServiceException() throws Exception {
        // Given
        Long idEmprendimiento = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenThrow(new RuntimeException("Error al actualizar"));

        // When
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        ResponseEntity<Emprendimiento> response = emprendimientoControllerEmprendedor.updateEmprendimiento(
                idEmprendimiento,
                emprendimientoJson,
                mockFile
        );

        System.setErr(originalErr);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("PUT /{idEmprendimiento} - Debe usar el ID correcto del path")
    void testUpdateEmprendimiento_UsesCorrectId() throws Exception {
        // Given
        Long idEmprendimiento = 99L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenReturn(mockEmprendimiento);

        // When
        emprendimientoControllerEmprendedor.updateEmprendimiento(
                idEmprendimiento,
                emprendimientoJson,
                mockFile
        );

        // Then
        verify(emprendimientoService).putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class));
    }

    @Test
    @DisplayName("PUT /{idEmprendimiento} - Debe retornar status OK")
    void testUpdateEmprendimiento_ReturnsOkStatus() throws Exception {
        // Given
        Long idEmprendimiento = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response = emprendimientoControllerEmprendedor.updateEmprendimiento(
                idEmprendimiento,
                emprendimientoJson,
                mockFile
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("PUT /{idEmprendimiento} - Debe retornar el emprendimiento actualizado")
    void testUpdateEmprendimiento_ReturnsUpdatedEmprendimiento() throws Exception {
        // Given
        Long idEmprendimiento = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response = emprendimientoControllerEmprendedor.updateEmprendimiento(
                idEmprendimiento,
                emprendimientoJson,
                mockFile
        );

        // Then
        assertThat(response.getBody()).isEqualTo(mockEmprendimiento);
    }

    @Test
    @DisplayName("PUT /{idEmprendimiento} - Debe llamar al servicio exactamente una vez")
    void testUpdateEmprendimiento_CallsServiceOnce() throws Exception {
        // Given
        Long idEmprendimiento = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenReturn(mockEmprendimiento);

        // When
        emprendimientoControllerEmprendedor.updateEmprendimiento(
                idEmprendimiento,
                emprendimientoJson,
                mockFile
        );

        // Then
        verify(emprendimientoService, times(1))
                .putEmprendimiento(anyLong(), any(EmprendimientoDto.class), any(MultipartFile.class));
        verifyNoMoreInteractions(emprendimientoService);
    }

    @Test
    @DisplayName("PUT /{idEmprendimiento} - Debe parsear JSON correctamente")
    void testUpdateEmprendimiento_ParsesJsonCorrectly() throws Exception {
        // Given
        Long idEmprendimiento = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response = emprendimientoControllerEmprendedor.updateEmprendimiento(
                idEmprendimiento,
                emprendimientoJson,
                mockFile
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(emprendimientoService).putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class));
    }

    // ==========================================
    // TESTS: Validación General
    // ==========================================

    @Test
    @DisplayName("Debe tener el servicio inyectado correctamente")
    void testServiceInjection() {
        // Then
        assertNotNull(emprendimientoService);
    }

    @Test
    @DisplayName("Debe retornar ResponseEntity no nulo en todas las operaciones")
    void testAllEndpointsReturnNonNullResponse() throws Exception {
        // Given
        Long idUsuario = 1L;
        Long idEmprendimiento = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);

        when(emprendimientoService.buscarPorIdUsuario(idUsuario)).thenReturn(mockEmprendimiento);
        when(emprendimientoService.putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenReturn(mockEmprendimiento);

        // When & Then
        assertNotNull(emprendimientoControllerEmprendedor.getEmprendimientoByIdUsuario(idUsuario));
        assertNotNull(emprendimientoControllerEmprendedor.updateEmprendimiento(idEmprendimiento, emprendimientoJson, mockFile));
    }

    @Test
    @DisplayName("Debe manejar múltiples actualizaciones consecutivas")
    void testMultipleUpdates() throws Exception {
        // Given
        Long idEmprendimiento = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(idEmprendimiento), any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response1 = emprendimientoControllerEmprendedor.updateEmprendimiento(
                idEmprendimiento, emprendimientoJson, mockFile);
        ResponseEntity<Emprendimiento> response2 = emprendimientoControllerEmprendedor.updateEmprendimiento(
                idEmprendimiento, emprendimientoJson, null);

        // Then
        assertNotNull(response1);
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.OK, response2.getStatusCode());

        verify(emprendimientoService, times(2))
                .putEmprendimiento(anyLong(), any(EmprendimientoDto.class), any());
    }
}