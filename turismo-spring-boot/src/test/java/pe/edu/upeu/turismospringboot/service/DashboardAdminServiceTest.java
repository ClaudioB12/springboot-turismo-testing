package pe.edu.upeu.turismospringboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.turismospringboot.model.dto.DashboardAdminDTO;
import pe.edu.upeu.turismospringboot.model.enums.EstadoReserva;
import pe.edu.upeu.turismospringboot.repository.EmprendimientoRepository;
import pe.edu.upeu.turismospringboot.repository.ResenaRepository;
import pe.edu.upeu.turismospringboot.repository.ReservaRepository;
import pe.edu.upeu.turismospringboot.repository.UsuarioRepository;
import pe.edu.upeu.turismospringboot.service.impl.DashboardAdminServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DashboardAdminServiceTest {

    @InjectMocks
    private DashboardAdminServiceImpl dashboardAdminService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private EmprendimientoRepository emprendimientoRepository;

    @Mock
    private ResenaRepository resenaRepository;

    @BeforeEach
    void setUp() {
        // Mock the repository responses
        when(usuarioRepository.count()).thenReturn(5L);
        when(reservaRepository.count()).thenReturn(10L);
        when(emprendimientoRepository.count()).thenReturn(8L);
        when(resenaRepository.count()).thenReturn(3L);

        // Mock the counts by EstadoReserva
        when(reservaRepository.countByEstado(EstadoReserva.PENDIENTE)).thenReturn(4L);
        when(reservaRepository.countByEstado(EstadoReserva.CONFIRMADA)).thenReturn(5L);
        when(reservaRepository.countByEstado(EstadoReserva.CANCELADA)).thenReturn(1L);

        // Mock the counts by Categoria
        when(emprendimientoRepository.countByCategoria()).thenReturn(
                List.of(new Object[]{"Turismo", 5L}, new Object[]{"Aventura", 3L})
        );
    }

    @Test
    void testObtenerDashboard() {
        // Act
        DashboardAdminDTO dashboard = dashboardAdminService.obtenerDashboard();

        // Assert
        assertNotNull(dashboard);
        assertEquals(5L, dashboard.getTotalUsuarios());
        assertEquals(10L, dashboard.getTotalReservas());
        assertEquals(8L, dashboard.getTotalEmprendimientos());
        assertEquals(3L, dashboard.getTotalResenas());

        // Validate "Reservas por Estado"
        Map<String, Long> reservasPorEstado = dashboard.getReservasPorEstado();
        assertEquals(4L, reservasPorEstado.get("Pendiente"));
        assertEquals(5L, reservasPorEstado.get("Confirmada"));
        assertEquals(1L, reservasPorEstado.get("Cancelada"));

        // Validate "Emprendimientos por Categoria"
        Map<String, Long> emprendimientosPorCategoria = dashboard.getEmprendimientosPorCategoria();
        assertEquals(5L, emprendimientosPorCategoria.get("Turismo"));
        assertEquals(3L, emprendimientosPorCategoria.get("Aventura"));
    }

    @Test
    void testObtenerDashboard_NoData() {
        // Simulate no data scenario
        when(usuarioRepository.count()).thenReturn(0L);
        when(reservaRepository.count()).thenReturn(0L);
        when(emprendimientoRepository.count()).thenReturn(0L);
        when(resenaRepository.count()).thenReturn(0L);
        when(reservaRepository.countByEstado(EstadoReserva.PENDIENTE)).thenReturn(0L);
        when(reservaRepository.countByEstado(EstadoReserva.CONFIRMADA)).thenReturn(0L);
        when(reservaRepository.countByEstado(EstadoReserva.CANCELADA)).thenReturn(0L);
        when(emprendimientoRepository.countByCategoria()).thenReturn(List.of());

        // Act
        DashboardAdminDTO dashboard = dashboardAdminService.obtenerDashboard();

        // Assert
        assertNotNull(dashboard);
        assertEquals(0L, dashboard.getTotalUsuarios());
        assertEquals(0L, dashboard.getTotalReservas());
        assertEquals(0L, dashboard.getTotalEmprendimientos());
        assertEquals(0L, dashboard.getTotalResenas());

        // Validate "Reservas por Estado"
        Map<String, Long> reservasPorEstado = dashboard.getReservasPorEstado();
        assertEquals(0L, reservasPorEstado.get("Pendiente"));
        assertEquals(0L, reservasPorEstado.get("Confirmada"));
        assertEquals(0L, reservasPorEstado.get("Cancelada"));

        // Validate "Emprendimientos por Categoria"
        Map<String, Long> emprendimientosPorCategoria = dashboard.getEmprendimientosPorCategoria();
        assertTrue(emprendimientosPorCategoria.isEmpty());
    }
}
