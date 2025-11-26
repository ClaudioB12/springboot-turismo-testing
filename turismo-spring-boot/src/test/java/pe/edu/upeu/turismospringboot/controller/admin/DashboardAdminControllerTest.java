package pe.edu.upeu.turismospringboot.controller.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upeu.turismospringboot.model.dto.DashboardAdminDTO;
import pe.edu.upeu.turismospringboot.service.DashboardAdminService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardAdminController - Pruebas Unitarias")
class DashboardAdminControllerTest {

    @Mock
    private DashboardAdminService dashboardAdminService;

    @InjectMocks
    private DashboardAdminController dashboardAdminController;

    private DashboardAdminDTO mockDashboardDTO;

    @BeforeEach
    void setUp() {
        // Crear un DTO de prueba
        mockDashboardDTO = new DashboardAdminDTO();
        // Configura aquí los valores según tu DTO
        // Por ejemplo:
        // mockDashboardDTO.setTotalUsuarios(100L);
        // mockDashboardDTO.setTotalReservas(50L);
        // etc.
    }

    @Test
    @DisplayName("Debe obtener el dashboard correctamente")
    void testObtenerDashboard_Success() {
        // Given
        when(dashboardAdminService.obtenerDashboard()).thenReturn(mockDashboardDTO);

        // When
        ResponseEntity<DashboardAdminDTO> response = dashboardAdminController.obtenerDashboard();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockDashboardDTO, response.getBody());

        verify(dashboardAdminService, times(1)).obtenerDashboard();
    }

    @Test
    @DisplayName("Debe retornar status 200 OK")
    void testObtenerDashboard_ReturnsOkStatus() {
        // Given
        when(dashboardAdminService.obtenerDashboard()).thenReturn(mockDashboardDTO);

        // When
        ResponseEntity<DashboardAdminDTO> response = dashboardAdminController.obtenerDashboard();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Debe retornar el DTO del servicio")
    void testObtenerDashboard_ReturnsServiceDTO() {
        // Given
        when(dashboardAdminService.obtenerDashboard()).thenReturn(mockDashboardDTO);

        // When
        ResponseEntity<DashboardAdminDTO> response = dashboardAdminController.obtenerDashboard();

        // Then
        assertThat(response.getBody()).isEqualTo(mockDashboardDTO);
    }

    @Test
    @DisplayName("Debe llamar al servicio exactamente una vez")
    void testObtenerDashboard_CallsServiceOnce() {
        // Given
        when(dashboardAdminService.obtenerDashboard()).thenReturn(mockDashboardDTO);

        // When
        dashboardAdminController.obtenerDashboard();

        // Then
        verify(dashboardAdminService, times(1)).obtenerDashboard();
        verifyNoMoreInteractions(dashboardAdminService);
    }

    @Test
    @DisplayName("Debe manejar DTO vacío correctamente")
    void testObtenerDashboard_WithEmptyDTO() {
        // Given
        DashboardAdminDTO emptyDTO = new DashboardAdminDTO();
        when(dashboardAdminService.obtenerDashboard()).thenReturn(emptyDTO);

        // When
        ResponseEntity<DashboardAdminDTO> response = dashboardAdminController.obtenerDashboard();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Debe retornar ResponseEntity no nulo")
    void testObtenerDashboard_ReturnsNonNullResponse() {
        // Given
        when(dashboardAdminService.obtenerDashboard()).thenReturn(mockDashboardDTO);

        // When
        ResponseEntity<DashboardAdminDTO> response = dashboardAdminController.obtenerDashboard();

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Debe propagar excepción del servicio")
    void testObtenerDashboard_PropagatesServiceException() {
        // Given
        when(dashboardAdminService.obtenerDashboard())
                .thenThrow(new RuntimeException("Error en el servicio"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            dashboardAdminController.obtenerDashboard();
        });

        verify(dashboardAdminService, times(1)).obtenerDashboard();
    }

    @Test
    @DisplayName("Debe mantener la integridad de los datos del DTO")
    void testObtenerDashboard_MaintainsDTOIntegrity() {
        // Given
        when(dashboardAdminService.obtenerDashboard()).thenReturn(mockDashboardDTO);

        // When
        ResponseEntity<DashboardAdminDTO> response = dashboardAdminController.obtenerDashboard();

        // Then
        assertThat(response.getBody()).isSameAs(mockDashboardDTO);
    }

    @Test
    @DisplayName("Debe funcionar con múltiples llamadas")
    void testObtenerDashboard_MultipleCallsWork() {
        // Given
        when(dashboardAdminService.obtenerDashboard()).thenReturn(mockDashboardDTO);

        // When
        ResponseEntity<DashboardAdminDTO> response1 = dashboardAdminController.obtenerDashboard();
        ResponseEntity<DashboardAdminDTO> response2 = dashboardAdminController.obtenerDashboard();
        ResponseEntity<DashboardAdminDTO> response3 = dashboardAdminController.obtenerDashboard();

        // Then
        assertAll(
                () -> assertEquals(HttpStatus.OK, response1.getStatusCode()),
                () -> assertEquals(HttpStatus.OK, response2.getStatusCode()),
                () -> assertEquals(HttpStatus.OK, response3.getStatusCode()),
                () -> assertNotNull(response1.getBody()),
                () -> assertNotNull(response2.getBody()),
                () -> assertNotNull(response3.getBody())
        );

        verify(dashboardAdminService, times(3)).obtenerDashboard();
    }

    @Test
    @DisplayName("Debe tener el servicio inyectado correctamente")
    void testServiceInjection() {
        // Then
        assertNotNull(dashboardAdminService);
    }
}