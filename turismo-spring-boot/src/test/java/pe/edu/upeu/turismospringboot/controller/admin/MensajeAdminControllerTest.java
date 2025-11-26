package pe.edu.upeu.turismospringboot.controller.admin;

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
import pe.edu.upeu.turismospringboot.model.dto.ChatResumenDto;
import pe.edu.upeu.turismospringboot.model.dto.MensajeDto;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.service.MensajeService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("MensajeAdminController - Pruebas Unitarias")
class MensajeAdminControllerTest {

    @Mock
    private MensajeService mensajeService;

    @InjectMocks
    private MensajeAdminController mensajeAdminController;

    private Usuario mockUsuarioAutenticado;
    private List<MensajeDto> mockMensajesList;
    private List<ChatResumenDto> mockChatsRecientesList;
    private MensajeDto mockMensaje1;
    private MensajeDto mockMensaje2;
    private MensajeDto mockMensaje3;
    private ChatResumenDto mockChat1;
    private ChatResumenDto mockChat2;
    private ChatResumenDto mockChat3;

    @BeforeEach
    void setUp() {
        // Mock Usuario Autenticado (Admin)
        mockUsuarioAutenticado = mock(Usuario.class);
        when(mockUsuarioAutenticado.getIdUsuario()).thenReturn(1L);
        when(mockUsuarioAutenticado.getUsername()).thenReturn("admin");

        // Mock MensajeDtos (usando mocks de Mockito)
        mockMensaje1 = mock(MensajeDto.class);
        mockMensaje2 = mock(MensajeDto.class);
        mockMensaje3 = mock(MensajeDto.class);

        // Mock List de Mensajes
        mockMensajesList = Arrays.asList(mockMensaje1, mockMensaje2, mockMensaje3);

        // Mock ChatResumenDtos (usando mocks de Mockito)
        mockChat1 = mock(ChatResumenDto.class);
        mockChat2 = mock(ChatResumenDto.class);
        mockChat3 = mock(ChatResumenDto.class);

        // Mock List de Chats Recientes
        mockChatsRecientesList = Arrays.asList(mockChat1, mockChat2, mockChat3);
    }

    // ==========================================
    // TESTS: GET /admin/mensajes/historial
    // ==========================================

    @Test
    @DisplayName("GET /historial - Debe obtener historial de mensajes correctamente")
    void testObtenerHistorial_Success() {
        // Given
        Long usuarioId = 2L;
        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId))
                .thenReturn(mockMensajesList);

        // When
        ResponseEntity<List<MensajeDto>> response = mensajeAdminController.obtenerHistorial(
                usuarioId,
                mockUsuarioAutenticado
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());

        verify(mensajeService, times(1))
                .obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId);
    }

    @Test
    @DisplayName("GET /historial - Debe retornar lista vacía si no hay mensajes")
    void testObtenerHistorial_EmptyList() {
        // Given
        Long usuarioId = 2L;
        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId))
                .thenReturn(Arrays.asList());

        // When
        ResponseEntity<List<MensajeDto>> response = mensajeAdminController.obtenerHistorial(
                usuarioId,
                mockUsuarioAutenticado
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /historial - Debe llamar al servicio con parámetros correctos")
    void testObtenerHistorial_CallsServiceWithCorrectParams() {
        // Given
        Long usuarioId = 5L;
        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId))
                .thenReturn(mockMensajesList);

        // When
        mensajeAdminController.obtenerHistorial(usuarioId, mockUsuarioAutenticado);

        // Then
        verify(mensajeService).obtenerHistorialEntre(eq(mockUsuarioAutenticado), eq(usuarioId));
    }

    @Test
    @DisplayName("GET /historial - Debe usar el usuario autenticado correctamente")
    void testObtenerHistorial_UsesAuthenticatedUser() {
        // Given
        Long usuarioId = 2L;
        Usuario otroUsuario = mock(Usuario.class);
        when(otroUsuario.getIdUsuario()).thenReturn(99L);
        when(otroUsuario.getUsername()).thenReturn("otro_usuario");

        when(mensajeService.obtenerHistorialEntre(otroUsuario, usuarioId))
                .thenReturn(mockMensajesList);

        // When
        mensajeAdminController.obtenerHistorial(usuarioId, otroUsuario);

        // Then
        verify(mensajeService).obtenerHistorialEntre(eq(otroUsuario), eq(usuarioId));
        verify(mensajeService, never()).obtenerHistorialEntre(eq(mockUsuarioAutenticado), anyLong());
    }

    @Test
    @DisplayName("GET /historial - Debe retornar los mensajes correctos")
    void testObtenerHistorial_ReturnsCorrectMessages() {
        // Given
        Long usuarioId = 2L;
        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId))
                .thenReturn(mockMensajesList);

        // When
        ResponseEntity<List<MensajeDto>> response = mensajeAdminController.obtenerHistorial(
                usuarioId,
                mockUsuarioAutenticado
        );

        // Then
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertThat(response.getBody()).containsExactly(mockMensaje1, mockMensaje2, mockMensaje3);
    }

    @Test
    @DisplayName("GET /historial - Debe manejar diferentes IDs de usuario")
    void testObtenerHistorial_DifferentUserIds() {
        // Given
        Long usuarioId1 = 2L;
        Long usuarioId2 = 3L;

        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId1))
                .thenReturn(mockMensajesList);
        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId2))
                .thenReturn(Arrays.asList());

        // When
        ResponseEntity<List<MensajeDto>> response1 = mensajeAdminController.obtenerHistorial(
                usuarioId1, mockUsuarioAutenticado);
        ResponseEntity<List<MensajeDto>> response2 = mensajeAdminController.obtenerHistorial(
                usuarioId2, mockUsuarioAutenticado);

        // Then
        assertFalse(response1.getBody().isEmpty());
        assertTrue(response2.getBody().isEmpty());

        verify(mensajeService, times(1)).obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId1);
        verify(mensajeService, times(1)).obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId2);
    }

    @Test
    @DisplayName("GET /historial - Debe retornar status OK")
    void testObtenerHistorial_ReturnsOkStatus() {
        // Given
        Long usuarioId = 2L;
        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId))
                .thenReturn(mockMensajesList);

        // When
        ResponseEntity<List<MensajeDto>> response = mensajeAdminController.obtenerHistorial(
                usuarioId,
                mockUsuarioAutenticado
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("GET /historial - Debe retornar el body correcto")
    void testObtenerHistorial_ReturnsCorrectBody() {
        // Given
        Long usuarioId = 2L;
        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId))
                .thenReturn(mockMensajesList);

        // When
        ResponseEntity<List<MensajeDto>> response = mensajeAdminController.obtenerHistorial(
                usuarioId,
                mockUsuarioAutenticado
        );

        // Then
        assertThat(response.getBody()).isEqualTo(mockMensajesList);
    }

    // ==========================================
    // TESTS: GET /admin/mensajes/recientes
    // ==========================================

    @Test
    @DisplayName("GET /recientes - Debe obtener chats recientes correctamente")
    void testObtenerChatsRecientes_Success() {
        // Given
        when(mensajeService.obtenerChatsRecientes(mockUsuarioAutenticado.getIdUsuario()))
                .thenReturn(mockChatsRecientesList);

        // When
        ResponseEntity<List<ChatResumenDto>> response = mensajeAdminController.obtenerChatsRecientes(
                mockUsuarioAutenticado
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());

        verify(mensajeService, times(1))
                .obtenerChatsRecientes(mockUsuarioAutenticado.getIdUsuario());
    }

    @Test
    @DisplayName("GET /recientes - Debe retornar lista vacía si no hay chats")
    void testObtenerChatsRecientes_EmptyList() {
        // Given
        when(mensajeService.obtenerChatsRecientes(mockUsuarioAutenticado.getIdUsuario()))
                .thenReturn(Arrays.asList());

        // When
        ResponseEntity<List<ChatResumenDto>> response = mensajeAdminController.obtenerChatsRecientes(
                mockUsuarioAutenticado
        );

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /recientes - Debe usar el ID del usuario autenticado")
    void testObtenerChatsRecientes_UsesAuthenticatedUserId() {
        // Given
        when(mensajeService.obtenerChatsRecientes(mockUsuarioAutenticado.getIdUsuario()))
                .thenReturn(mockChatsRecientesList);

        // When
        mensajeAdminController.obtenerChatsRecientes(mockUsuarioAutenticado);

        // Then
        verify(mensajeService).obtenerChatsRecientes(eq(1L));
    }

    @Test
    @DisplayName("GET /recientes - Debe llamar al servicio exactamente una vez")
    void testObtenerChatsRecientes_CallsServiceOnce() {
        // Given
        when(mensajeService.obtenerChatsRecientes(mockUsuarioAutenticado.getIdUsuario()))
                .thenReturn(mockChatsRecientesList);

        // When
        mensajeAdminController.obtenerChatsRecientes(mockUsuarioAutenticado);

        // Then
        verify(mensajeService, times(1)).obtenerChatsRecientes(anyLong());
        verifyNoMoreInteractions(mensajeService);
    }

    @Test
    @DisplayName("GET /recientes - Debe retornar los chats correctos")
    void testObtenerChatsRecientes_ReturnsCorrectChats() {
        // Given
        when(mensajeService.obtenerChatsRecientes(mockUsuarioAutenticado.getIdUsuario()))
                .thenReturn(mockChatsRecientesList);

        // When
        ResponseEntity<List<ChatResumenDto>> response = mensajeAdminController.obtenerChatsRecientes(
                mockUsuarioAutenticado
        );

        // Then
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertThat(response.getBody()).containsExactly(mockChat1, mockChat2, mockChat3);
    }

    @Test
    @DisplayName("GET /recientes - Debe retornar status OK")
    void testObtenerChatsRecientes_ReturnsOkStatus() {
        // Given
        when(mensajeService.obtenerChatsRecientes(mockUsuarioAutenticado.getIdUsuario()))
                .thenReturn(mockChatsRecientesList);

        // When
        ResponseEntity<List<ChatResumenDto>> response = mensajeAdminController.obtenerChatsRecientes(
                mockUsuarioAutenticado
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("GET /recientes - Debe manejar diferentes usuarios autenticados")
    void testObtenerChatsRecientes_DifferentAuthenticatedUsers() {
        // Given
        Usuario otroUsuario = mock(Usuario.class);
        when(otroUsuario.getIdUsuario()).thenReturn(99L);

        when(mensajeService.obtenerChatsRecientes(mockUsuarioAutenticado.getIdUsuario()))
                .thenReturn(mockChatsRecientesList);
        when(mensajeService.obtenerChatsRecientes(otroUsuario.getIdUsuario()))
                .thenReturn(Arrays.asList());

        // When
        ResponseEntity<List<ChatResumenDto>> response1 =
                mensajeAdminController.obtenerChatsRecientes(mockUsuarioAutenticado);
        ResponseEntity<List<ChatResumenDto>> response2 =
                mensajeAdminController.obtenerChatsRecientes(otroUsuario);

        // Then
        assertFalse(response1.getBody().isEmpty());
        assertTrue(response2.getBody().isEmpty());

        verify(mensajeService, times(1)).obtenerChatsRecientes(1L);
        verify(mensajeService, times(1)).obtenerChatsRecientes(99L);
    }

    @Test
    @DisplayName("GET /recientes - Debe retornar el body correcto")
    void testObtenerChatsRecientes_ReturnsCorrectBody() {
        // Given
        when(mensajeService.obtenerChatsRecientes(mockUsuarioAutenticado.getIdUsuario()))
                .thenReturn(mockChatsRecientesList);

        // When
        ResponseEntity<List<ChatResumenDto>> response = mensajeAdminController.obtenerChatsRecientes(
                mockUsuarioAutenticado
        );

        // Then
        assertThat(response.getBody()).isEqualTo(mockChatsRecientesList);
    }

    // ==========================================
    // TESTS: Validación de Inyección y General
    // ==========================================

    @Test
    @DisplayName("Debe tener el servicio inyectado correctamente")
    void testServiceInjection() {
        // Then
        assertNotNull(mensajeService);
    }

    @Test
    @DisplayName("Debe manejar múltiples llamadas consecutivas")
    void testMultipleCalls() {
        // Given
        Long usuarioId = 2L;
        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId))
                .thenReturn(mockMensajesList);
        when(mensajeService.obtenerChatsRecientes(mockUsuarioAutenticado.getIdUsuario()))
                .thenReturn(mockChatsRecientesList);

        // When
        ResponseEntity<List<MensajeDto>> response1 =
                mensajeAdminController.obtenerHistorial(usuarioId, mockUsuarioAutenticado);
        ResponseEntity<List<ChatResumenDto>> response2 =
                mensajeAdminController.obtenerChatsRecientes(mockUsuarioAutenticado);

        // Then
        assertNotNull(response1.getBody());
        assertNotNull(response2.getBody());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }

    @Test
    @DisplayName("Debe validar que el usuario autenticado no sea null")
    void testAuthenticatedUserNotNull() {
        // Given
        Long usuarioId = 2L;
        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId))
                .thenReturn(mockMensajesList);

        // When
        ResponseEntity<List<MensajeDto>> response =
                mensajeAdminController.obtenerHistorial(usuarioId, mockUsuarioAutenticado);

        // Then
        assertNotNull(mockUsuarioAutenticado);
        assertNotNull(response);
        verify(mensajeService).obtenerHistorialEntre(notNull(), anyLong());
    }

    @Test
    @DisplayName("Debe llamar al servicio sin interacciones adicionales")
    void testNoAdditionalInteractions() {
        // Given
        Long usuarioId = 2L;
        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId))
                .thenReturn(mockMensajesList);

        // When
        mensajeAdminController.obtenerHistorial(usuarioId, mockUsuarioAutenticado);

        // Then
        verify(mensajeService, times(1)).obtenerHistorialEntre(any(), anyLong());
        verifyNoMoreInteractions(mensajeService);
    }

    @Test
    @DisplayName("Debe retornar ResponseEntity no nulo para historial")
    void testHistorialReturnsNonNullResponse() {
        // Given
        Long usuarioId = 2L;
        when(mensajeService.obtenerHistorialEntre(mockUsuarioAutenticado, usuarioId))
                .thenReturn(mockMensajesList);

        // When
        ResponseEntity<List<MensajeDto>> response =
                mensajeAdminController.obtenerHistorial(usuarioId, mockUsuarioAutenticado);

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Debe retornar ResponseEntity no nulo para chats recientes")
    void testChatsRecientesReturnsNonNullResponse() {
        // Given
        when(mensajeService.obtenerChatsRecientes(mockUsuarioAutenticado.getIdUsuario()))
                .thenReturn(mockChatsRecientesList);

        // When
        ResponseEntity<List<ChatResumenDto>> response =
                mensajeAdminController.obtenerChatsRecientes(mockUsuarioAutenticado);

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
    }
}