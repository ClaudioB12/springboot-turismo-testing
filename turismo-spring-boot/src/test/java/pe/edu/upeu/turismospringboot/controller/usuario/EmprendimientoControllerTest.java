package pe.edu.upeu.turismospringboot.controller.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.controller.admin.EmprendimientoController;
import pe.edu.upeu.turismospringboot.model.dto.EmprendimientoDto;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.service.EmprendimientoService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmprendimientoController - Pruebas Unitarias")
class EmprendimientoControllerTest {

    @Mock
    private EmprendimientoService emprendimientoService;

    @InjectMocks
    private EmprendimientoController emprendimientoController;

    private ObjectMapper objectMapper;
    private Emprendimiento mockEmprendimiento;
    private EmprendimientoDto mockEmprendimientoDto;
    private List<Emprendimiento> mockEmprendimientosList;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Mock Emprendimiento Entity
        mockEmprendimiento = new Emprendimiento();
        mockEmprendimiento.setIdEmprendimiento(1L);
        mockEmprendimiento.setNombre("Emprendimiento Test");
        mockEmprendimiento.setDescripcion("Descripción de prueba");

        // Mock Emprendimiento DTO
        mockEmprendimientoDto = new EmprendimientoDto();
        mockEmprendimientoDto.setNombre("Emprendimiento Test");
        mockEmprendimientoDto.setDescripcion("Descripción de prueba");

        // Mock List
        mockEmprendimientosList = Arrays.asList(
                mockEmprendimiento,
                createEmprendimiento(2L, "Emprendimiento 2"),
                createEmprendimiento(3L, "Emprendimiento 3")
        );

        // Mock MultipartFile
        mockFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    private Emprendimiento createEmprendimiento(Long id, String nombre) {
        Emprendimiento emp = new Emprendimiento();
        emp.setIdEmprendimiento(id);
        emp.setNombre(nombre);
        return emp;
    }

    // ==========================================
    // TESTS: GET /admin/emprendimiento
    // ==========================================

    @Test
    @DisplayName("GET / - Debe obtener todos los emprendimientos")
    void testObtenerEmprendimientos_Success() {
        // Given
        when(emprendimientoService.getEmprendimientos()).thenReturn(mockEmprendimientosList);

        // When
        ResponseEntity<List<Emprendimiento>> response = emprendimientoController.obtenerEmprendimientos();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());

        verify(emprendimientoService, times(1)).getEmprendimientos();
    }

    @Test
    @DisplayName("GET / - Debe retornar lista vacía si no hay emprendimientos")
    void testObtenerEmprendimientos_EmptyList() {
        // Given
        when(emprendimientoService.getEmprendimientos()).thenReturn(Arrays.asList());

        // When
        ResponseEntity<List<Emprendimiento>> response = emprendimientoController.obtenerEmprendimientos();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    // ==========================================
    // TESTS: GET /admin/emprendimiento/{id}
    // ==========================================

    @Test
    @DisplayName("GET /{id} - Debe obtener emprendimiento por ID")
    void testObtenerEmprendimientoPorId_Success() {
        // Given
        Long id = 1L;
        when(emprendimientoService.getEmprendimientoById(id)).thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response = emprendimientoController.obtenerEmprendimientoPorId(id);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getIdEmprendimiento());

        verify(emprendimientoService, times(1)).getEmprendimientoById(id);
    }

    @Test
    @DisplayName("GET /{id} - Debe llamar al servicio con el ID correcto")
    void testObtenerEmprendimientoPorId_CallsServiceWithCorrectId() {
        // Given
        Long id = 5L;
        when(emprendimientoService.getEmprendimientoById(id)).thenReturn(mockEmprendimiento);

        // When
        emprendimientoController.obtenerEmprendimientoPorId(id);

        // Then
        verify(emprendimientoService).getEmprendimientoById(eq(id));
    }

    // ==========================================
    // TESTS: POST /admin/emprendimiento
    // ==========================================

    @Test
    @DisplayName("POST / - Debe crear emprendimiento con archivo")
    void testCrearEmprendimiento_WithFile_Success() throws Exception {
        // Given
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.postEmprendimiento(any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response = emprendimientoController.crearEmprendimiento(
                emprendimientoJson,
                mockFile
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockEmprendimiento.getNombre(), response.getBody().getNombre());

        verify(emprendimientoService, times(1))
                .postEmprendimiento(any(EmprendimientoDto.class), any(MultipartFile.class));
    }

    @Test
    @DisplayName("POST / - Debe crear emprendimiento sin archivo")
    void testCrearEmprendimiento_WithoutFile_Success() throws Exception {
        // Given
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.postEmprendimiento(any(EmprendimientoDto.class), isNull()))
                .thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response = emprendimientoController.crearEmprendimiento(
                emprendimientoJson,
                null
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(emprendimientoService, times(1))
                .postEmprendimiento(any(EmprendimientoDto.class), isNull());
    }

    @Test
    @DisplayName("POST / - Debe retornar 500 si ocurre un error")
    void testCrearEmprendimiento_Error() throws Exception {
        // Given
        String invalidJson = "{invalid json";

        // When
        // Redirigir System.err para suprimir printStackTrace()
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        ResponseEntity<Emprendimiento> response = emprendimientoController.crearEmprendimiento(
                invalidJson,
                mockFile
        );

        // Restaurar System.err
        System.setErr(originalErr);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        verify(emprendimientoService, never())
                .postEmprendimiento(any(EmprendimientoDto.class), any(MultipartFile.class));
    }

    @Test
    @DisplayName("POST / - Debe manejar excepción del servicio")
    void testCrearEmprendimiento_ServiceException() throws Exception {
        // Given
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.postEmprendimiento(any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenThrow(new RuntimeException("Error al guardar"));

        // When
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        ResponseEntity<Emprendimiento> response = emprendimientoController.crearEmprendimiento(
                emprendimientoJson,
                mockFile
        );

        System.setErr(originalErr);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ==========================================
    // TESTS: PUT /admin/emprendimiento/{id}
    // ==========================================

    @Test
    @DisplayName("PUT /{id} - Debe actualizar emprendimiento con archivo")
    void testActualizarEmprendimiento_WithFile_Success() throws Exception {
        // Given
        Long id = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(id), any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response = emprendimientoController.actualizarEmprendimiento(
                id,
                emprendimientoJson,
                mockFile
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(emprendimientoService, times(1))
                .putEmprendimiento(eq(id), any(EmprendimientoDto.class), any(MultipartFile.class));
    }

    @Test
    @DisplayName("PUT /{id} - Debe actualizar emprendimiento sin archivo")
    void testActualizarEmprendimiento_WithoutFile_Success() throws Exception {
        // Given
        Long id = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(id), any(EmprendimientoDto.class), isNull()))
                .thenReturn(mockEmprendimiento);

        // When
        ResponseEntity<Emprendimiento> response = emprendimientoController.actualizarEmprendimiento(
                id,
                emprendimientoJson,
                null
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(emprendimientoService, times(1))
                .putEmprendimiento(eq(id), any(EmprendimientoDto.class), isNull());
    }

    @Test
    @DisplayName("PUT /{id} - Debe retornar 500 si ocurre un error")
    void testActualizarEmprendimiento_Error() throws Exception {
        // Given
        Long id = 1L;
        String invalidJson = "{invalid json";

        // When
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        ResponseEntity<Emprendimiento> response = emprendimientoController.actualizarEmprendimiento(
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
    void testActualizarEmprendimiento_ServiceException() throws Exception {
        // Given
        Long id = 1L;
        String emprendimientoJson = objectMapper.writeValueAsString(mockEmprendimientoDto);
        when(emprendimientoService.putEmprendimiento(eq(id), any(EmprendimientoDto.class), any(MultipartFile.class)))
                .thenThrow(new RuntimeException("Error al actualizar"));

        // When
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        ResponseEntity<Emprendimiento> response = emprendimientoController.actualizarEmprendimiento(
                id,
                emprendimientoJson,
                mockFile
        );

        System.setErr(originalErr);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ==========================================
    // TESTS: DELETE /admin/emprendimiento/{id}
    // ==========================================

    @Test
    @DisplayName("DELETE /{id} - Debe eliminar emprendimiento correctamente")
    void testEliminarEmprendimiento_Success() {
        // Given
        Long id = 1L;
        doNothing().when(emprendimientoService).deleteEmprendimiento(id);

        // When
        ResponseEntity<String> response = emprendimientoController.eliminarEmprendimiento(id);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Emprendimiento eliminado", response.getBody());

        verify(emprendimientoService, times(1)).deleteEmprendimiento(id);
    }

    @Test
    @DisplayName("DELETE /{id} - Debe retornar 500 si ocurre un error")
    void testEliminarEmprendimiento_Error() {
        // Given
        Long id = 1L;
        doThrow(new RuntimeException("Error al eliminar"))
                .when(emprendimientoService).deleteEmprendimiento(id);

        // When
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        ResponseEntity<String> response = emprendimientoController.eliminarEmprendimiento(id);

        System.setErr(originalErr);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("DELETE /{id} - Debe llamar al servicio con el ID correcto")
    void testEliminarEmprendimiento_CallsServiceWithCorrectId() {
        // Given
        Long id = 99L;
        doNothing().when(emprendimientoService).deleteEmprendimiento(id);

        // When
        emprendimientoController.eliminarEmprendimiento(id);

        // Then
        verify(emprendimientoService).deleteEmprendimiento(eq(id));
    }

    // ==========================================
    // TESTS: GET /admin/emprendimiento/buscar
    // ==========================================

    @Test
    @DisplayName("GET /buscar - Debe buscar emprendimientos por nombre")
    void testBuscarPorNombre_Success() {
        // Given
        String nombre = "Test";
        List<Emprendimiento> resultados = Arrays.asList(mockEmprendimiento);
        when(emprendimientoService.buscarPorNombre(nombre)).thenReturn(resultados);

        // When
        ResponseEntity<List<Emprendimiento>> response = emprendimientoController.buscarPorNombre(nombre);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(emprendimientoService, times(1)).buscarPorNombre(nombre);
    }

    @Test
    @DisplayName("GET /buscar - Debe retornar lista vacía si no encuentra resultados")
    void testBuscarPorNombre_NoResults() {
        // Given
        String nombre = "NoExiste";
        when(emprendimientoService.buscarPorNombre(nombre)).thenReturn(Arrays.asList());

        // When
        ResponseEntity<List<Emprendimiento>> response = emprendimientoController.buscarPorNombre(nombre);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /buscar - Debe llamar al servicio con el nombre correcto")
    void testBuscarPorNombre_CallsServiceWithCorrectName() {
        // Given
        String nombre = "Búsqueda Test";
        when(emprendimientoService.buscarPorNombre(nombre)).thenReturn(Arrays.asList());

        // When
        emprendimientoController.buscarPorNombre(nombre);

        // Then
        verify(emprendimientoService).buscarPorNombre(eq(nombre));
    }

    // ==========================================
    // TESTS: Validación de Inyección
    // ==========================================

    @Test
    @DisplayName("Debe tener el servicio inyectado correctamente")
    void testServiceInjection() {
        // Then
        assertNotNull(emprendimientoService);
    }
}