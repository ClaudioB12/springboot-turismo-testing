package pe.edu.upeu.turismospringboot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.turismospringboot.model.dto.CrearReservaDetalleRequest;
import pe.edu.upeu.turismospringboot.model.dto.CrearReservaRequest;
import pe.edu.upeu.turismospringboot.model.dto.ReservaResponseDTO;
import pe.edu.upeu.turismospringboot.model.entity.*;
import pe.edu.upeu.turismospringboot.model.enums.EstadoReserva;
import pe.edu.upeu.turismospringboot.repository.EmprendimientoRepository;
import pe.edu.upeu.turismospringboot.repository.ReservaRepository;
import pe.edu.upeu.turismospringboot.repository.ServicioTuristicoRepository;
import pe.edu.upeu.turismospringboot.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del Servicio de Reserva")
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private EmprendimientoRepository emprendimientoRepository;

    @Mock
    private ServicioTuristicoRepository servicioTuristicoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private Usuario usuarioAutenticado;
    private Usuario usuarioEmprendedor;
    private Persona personaEmprendedor;
    private Emprendimiento emprendimiento;
    private ServicioTuristico servicioTuristico;
    private Reserva reserva;
    private CrearReservaRequest crearReservaRequest;

    @BeforeEach
    void setUp() {
        // Usuario autenticado (cliente)
        usuarioAutenticado = new Usuario();
        usuarioAutenticado.setIdUsuario(1L);
        usuarioAutenticado.setUsername("cliente@test.com");

        // Persona emprendedor
        personaEmprendedor = new Persona();
        personaEmprendedor.setIdPersona(2L);
        personaEmprendedor.setNombres("Juan");
        personaEmprendedor.setApellidos("Pérez");
        personaEmprendedor.setTelefono("987654321");

        // Usuario emprendedor
        usuarioEmprendedor = new Usuario();
        usuarioEmprendedor.setIdUsuario(2L);
        usuarioEmprendedor.setUsername("emprendedor@test.com");
        usuarioEmprendedor.setPersona(personaEmprendedor);

        // Emprendimiento
        emprendimiento = new Emprendimiento();
        emprendimiento.setIdEmprendimiento(1L);
        emprendimiento.setNombre("Hotel Los Andes");
        emprendimiento.setUsuario(usuarioEmprendedor);

        // Servicio turístico (NOTA: usa idServicio, no idServicioTuristico)
        servicioTuristico = new ServicioTuristico();
        servicioTuristico.setIdServicio(1L);
        servicioTuristico.setNombre("Habitación Doble");
        servicioTuristico.setPrecioUnitario(150.0);
        servicioTuristico.setTipoServicio("ALOJAMIENTO");
        servicioTuristico.setEmprendimiento(emprendimiento);

        // Reserva
        reserva = new Reserva();
        reserva.setIdReserva(1L);
        reserva.setUsuario(usuarioAutenticado);
        reserva.setEmprendimiento(emprendimiento);
        reserva.setFechaHoraInicio(LocalDateTime.now().plusDays(1));
        reserva.setFechaHoraFin(LocalDateTime.now().plusDays(3));
        reserva.setFechaHoraReserva(LocalDateTime.now());
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setTotalGeneral(300.0);

        // Request para crear reserva (NOTA: usa CrearReservaDetalleRequest)
        CrearReservaDetalleRequest detalleRequest = new CrearReservaDetalleRequest();
        detalleRequest.setIdServicioTuristico(1L);
        detalleRequest.setCantidad(2);
        detalleRequest.setObservaciones("Sin observaciones");

        crearReservaRequest = new CrearReservaRequest();
        crearReservaRequest.setIdEmprendimiento(1L);
        crearReservaRequest.setFechaHoraInicio(LocalDateTime.now().plusDays(1));
        crearReservaRequest.setFechaHoraFin(LocalDateTime.now().plusDays(3));
        crearReservaRequest.setDetalles(Arrays.asList(detalleRequest));
    }

    @Test
    @DisplayName("Debe crear una reserva correctamente")
    void testCrearReserva_Exitoso() {
        // Arrange
        when(emprendimientoRepository.findById(1L)).thenReturn(Optional.of(emprendimiento));
        when(servicioTuristicoRepository.findById(1L)).thenReturn(Optional.of(servicioTuristico));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // Act
        ReservaResponseDTO resultado = reservaService.crearReserva(crearReservaRequest, usuarioAutenticado);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdReserva());
        assertEquals("PENDIENTE", resultado.getEstado());

        // Verificar que se llamaron los métodos correctos
        verify(emprendimientoRepository, times(1)).findById(1L);
        verify(servicioTuristicoRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe calcular correctamente el total de la reserva")
    void testCrearReserva_CalculoTotal() {
        // Arrange
        when(emprendimientoRepository.findById(1L)).thenReturn(Optional.of(emprendimiento));
        when(servicioTuristicoRepository.findById(1L)).thenReturn(Optional.of(servicioTuristico));

        ArgumentCaptor<Reserva> reservaCaptor = ArgumentCaptor.forClass(Reserva.class);
        when(reservaRepository.save(reservaCaptor.capture())).thenReturn(reserva);

        // Act
        reservaService.crearReserva(crearReservaRequest, usuarioAutenticado);

        // Assert
        Reserva reservaGuardada = reservaCaptor.getValue();
        assertNotNull(reservaGuardada);
        assertEquals(300.0, reservaGuardada.getTotalGeneral()); // 150.0 * 2 = 300.0
        assertEquals(1, reservaGuardada.getReservaDetalles().size());

        ReservaDetalle detalle = reservaGuardada.getReservaDetalles().get(0);
        assertEquals(2, detalle.getCantidad());
        assertEquals(150.0, detalle.getPrecioUnitario());
        assertEquals(300.0, detalle.getTotal());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando emprendimiento no existe")
    void testCrearReserva_EmprendimientoNoExiste() {
        // Arrange
        when(emprendimientoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.crearReserva(crearReservaRequest, usuarioAutenticado);
        });

        assertEquals("Emprendimiento no encontrado", exception.getMessage());
        verify(emprendimientoRepository, times(1)).findById(1L);
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando servicio turístico no existe")
    void testCrearReserva_ServicioNoExiste() {
        // Arrange
        when(emprendimientoRepository.findById(1L)).thenReturn(Optional.of(emprendimiento));
        when(servicioTuristicoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.crearReserva(crearReservaRequest, usuarioAutenticado);
        });

        assertEquals("Servicio turístico no encontrado", exception.getMessage());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe actualizar el estado de reserva correctamente")
    void testActualizarEstadoReserva_Exitoso() {
        // Arrange
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // Act
        ReservaResponseDTO resultado = reservaService.actualizarEstadoReserva(
                1L,
                EstadoReserva.CONFIRMADA,
                usuarioEmprendedor
        );

        // Assert
        assertNotNull(resultado);
        assertEquals("CONFIRMADA", resultado.getEstado());
        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).save(reserva);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando reserva no existe al actualizar estado")
    void testActualizarEstadoReserva_ReservaNoExiste() {
        // Arrange
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.actualizarEstadoReserva(1L, EstadoReserva.CONFIRMADA, usuarioEmprendedor);
        });

        assertEquals("Reserva no encontrada", exception.getMessage());
        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no tiene permisos")
    void testActualizarEstadoReserva_SinPermisos() {
        // Arrange
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Usuario usuarioSinPermisos = new Usuario();
        usuarioSinPermisos.setIdUsuario(999L);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.actualizarEstadoReserva(1L, EstadoReserva.CONFIRMADA, usuarioSinPermisos);
        });

        assertEquals("No tiene permisos para modificar esta reserva", exception.getMessage());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar cambiar estado de reserva cancelada")
    void testActualizarEstadoReserva_ReservaCancelada() {
        // Arrange
        reserva.setEstado(EstadoReserva.CANCELADA);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.actualizarEstadoReserva(1L, EstadoReserva.CONFIRMADA, usuarioEmprendedor);
        });

        assertEquals("No se puede cambiar el estado de una reserva cancelada o rechazada", exception.getMessage());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar cambiar estado de reserva rechazada")
    void testActualizarEstadoReserva_ReservaRechazada() {
        // Arrange
        reserva.setEstado(EstadoReserva.RECHAZADA);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.actualizarEstadoReserva(1L, EstadoReserva.CONFIRMADA, usuarioEmprendedor);
        });

        assertTrue(exception.getMessage().contains("cancelada o rechazada"));
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe obtener número de emprendedor correctamente")
    void testObtenerNumeroEmprendedorPorIdEmprendimiento_Exitoso() {
        // Arrange
        when(emprendimientoRepository.findById(1L)).thenReturn(Optional.of(emprendimiento));

        // Act
        String numeroTelefono = reservaService.obtenerNumeroEmprendedorPorIdEmprendimiento(1L);

        // Assert
        assertNotNull(numeroTelefono);
        assertEquals("987654321", numeroTelefono);
        verify(emprendimientoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando emprendimiento no existe al obtener número")
    void testObtenerNumeroEmprendedorPorIdEmprendimiento_NoExiste() {
        // Arrange
        when(emprendimientoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.obtenerNumeroEmprendedorPorIdEmprendimiento(1L);
        });

        assertTrue(exception.getMessage().contains("no encontrado"));
    }

    @Test
    @DisplayName("Debe obtener reservas por ID de usuario correctamente")
    void testObtenerReservasPorIdUsuario_Exitoso() {
        // Arrange
        List<Reserva> reservasEsperadas = Arrays.asList(reserva);
        usuarioAutenticado.setReservas(reservasEsperadas);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioAutenticado));

        // Act
        List<Reserva> resultado = reservaService.obtenerReservasPorIdUsuario(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(reserva, resultado.get(0));
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no existe")
    void testObtenerReservasPorIdUsuario_UsuarioNoExiste() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.obtenerReservasPorIdUsuario(1L);
        });

        assertTrue(exception.getMessage().contains("no encontrado"));
    }

    @Test
    @DisplayName("Debe obtener reserva por ID correctamente")
    void testObtenerReservaPorId_Exitoso() {
        // Arrange
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act
        Reserva resultado = reservaService.obtenerReservaPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdReserva());
        assertEquals(EstadoReserva.PENDIENTE, resultado.getEstado());
        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando reserva no existe por ID")
    void testObtenerReservaPorId_NoExiste() {
        // Arrange
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.obtenerReservaPorId(1L);
        });

        assertTrue(exception.getMessage().contains("no encontrado"));
    }

    @Test
    @DisplayName("Debe obtener reservas por ID de emprendimiento correctamente")
    void testObtenerReservasPorIdEmprendimiento_Exitoso() {
        // Arrange
        List<Reserva> reservasEsperadas = Arrays.asList(reserva);
        emprendimiento.setReservas(reservasEsperadas);
        when(emprendimientoRepository.findById(1L)).thenReturn(Optional.of(emprendimiento));

        // Act
        List<Reserva> resultado = reservaService.obtenerReservasPorIdEmprendimiento(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(reserva, resultado.get(0));
        verify(emprendimientoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando emprendimiento no existe al obtener reservas")
    void testObtenerReservasPorIdEmprendimiento_NoExiste() {
        // Arrange
        when(emprendimientoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.obtenerReservasPorIdEmprendimiento(1L);
        });

        assertTrue(exception.getMessage().contains("No se encontro el emprendimiento"));
    }

    @Test
    @DisplayName("Debe crear reserva con múltiples detalles correctamente")
    void testCrearReserva_MultiplesDetalles() {
        // Arrange
        ServicioTuristico servicio2 = new ServicioTuristico();
        servicio2.setIdServicio(2L);
        servicio2.setNombre("Desayuno Buffet");
        servicio2.setPrecioUnitario(30.0);
        servicio2.setTipoServicio("ALIMENTACION");

        CrearReservaDetalleRequest detalle1 = new CrearReservaDetalleRequest();
        detalle1.setIdServicioTuristico(1L);
        detalle1.setCantidad(2);

        CrearReservaDetalleRequest detalle2 = new CrearReservaDetalleRequest();
        detalle2.setIdServicioTuristico(2L);
        detalle2.setCantidad(4);

        crearReservaRequest.setDetalles(Arrays.asList(detalle1, detalle2));

        when(emprendimientoRepository.findById(1L)).thenReturn(Optional.of(emprendimiento));
        when(servicioTuristicoRepository.findById(1L)).thenReturn(Optional.of(servicioTuristico));
        when(servicioTuristicoRepository.findById(2L)).thenReturn(Optional.of(servicio2));

        ArgumentCaptor<Reserva> reservaCaptor = ArgumentCaptor.forClass(Reserva.class);
        when(reservaRepository.save(reservaCaptor.capture())).thenReturn(reserva);

        // Act
        reservaService.crearReserva(crearReservaRequest, usuarioAutenticado);

        // Assert
        Reserva reservaGuardada = reservaCaptor.getValue();
        assertEquals(2, reservaGuardada.getReservaDetalles().size());

        // Total: (150 * 2) + (30 * 4) = 300 + 120 = 420
        assertEquals(420.0, reservaGuardada.getTotalGeneral());
    }
}