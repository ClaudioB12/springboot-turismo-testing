package pe.edu.upeu.turismospringboot.service.impl;

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

import java.util.Map;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DashboardAdminServiceImplTest {

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
        // Set up mock data for repositories, if necessary
    }

    @Test
    void testObtenerDashboard() {
        // Arrange
        when(usuarioRepository.count()).thenReturn(100L);
        when(reservaRepository.count()).thenReturn(200L);
        when(emprendimientoRepository.count()).thenReturn(50L);
        when(resenaRepository.count()).thenReturn(30L);

        when(reservaRepository.countByEstado(EstadoReserva.PENDIENTE)).thenReturn(50L);
        when(reservaRepository.countByEstado(EstadoReserva.CONFIRMADA)).thenReturn(120L);
        when(reservaRepository.countByEstado(EstadoReserva.CANCELADA)).thenReturn(30L);

        when(emprendimientoRepository.countByCategoria()).thenReturn(List.of(
                new Object[]{"Turismo", 10L},
                new Object[]{"Aventura", 15L},
                new Object[]{"Cultural", 25L}
        ));

        // Act
        DashboardAdminDTO dashboard = dashboardAdminService.obtenerDashboard();

        // Assert
        assertNotNull(dashboard);
        assertEquals(100L, dashboard.getTotalUsuarios());
        assertEquals(200L, dashboard.getTotalReservas());
        assertEquals(50L, dashboard.getTotalEmprendimientos());
        assertEquals(30L, dashboard.getTotalResenas());

        Map<String, Long> reservasPorEstado = dashboard.getReservasPorEstado();
        assertNotNull(reservasPorEstado);
        assertEquals(50L, reservasPorEstado.get("Pendiente"));
        assertEquals(120L, reservasPorEstado.get("Confirmada"));
        assertEquals(30L, reservasPorEstado.get("Cancelada"));

        Map<String, Long> emprendimientosPorCategoria = dashboard.getEmprendimientosPorCategoria();
        assertNotNull(emprendimientosPorCategoria);
        assertEquals(10L, emprendimientosPorCategoria.get("Turismo"));
        assertEquals(15L, emprendimientosPorCategoria.get("Aventura"));
        assertEquals(25L, emprendimientosPorCategoria.get("Cultural"));
    }

    @Test
    void testObtenerDashboard_EmptyData() {
        // Arrange
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

        Map<String, Long> reservasPorEstado = dashboard.getReservasPorEstado();
        assertNotNull(reservasPorEstado);
        assertEquals(0L, reservasPorEstado.get("Pendiente"));
        assertEquals(0L, reservasPorEstado.get("Confirmada"));
        assertEquals(0L, reservasPorEstado.get("Cancelada"));

        Map<String, Long> emprendimientosPorCategoria = dashboard.getEmprendimientosPorCategoria();
        assertNotNull(emprendimientosPorCategoria);
        assertTrue(emprendimientosPorCategoria.isEmpty());
    }
}
