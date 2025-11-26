package pe.edu.upeu.turismospringboot.dto;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.DashboardAdminDTO;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DashboardAdminDTOTest {

    @Test
    void testConstructorVacio() {
        DashboardAdminDTO dto = new DashboardAdminDTO();

        assertNull(dto.getTotalUsuarios());
        assertNull(dto.getTotalReservas());
        assertNull(dto.getTotalEmprendimientos());
        assertNull(dto.getTotalResenas());
        assertNull(dto.getReservasPorEstado());
        assertNull(dto.getEmprendimientosPorCategoria());
    }

    @Test
    void testConstructorConArgumentos() {
        Map<String, Long> reservasEstado = new HashMap<>();
        reservasEstado.put("PENDIENTE", 5L);

        Map<String, Long> emprendimientosCategoria = new HashMap<>();
        emprendimientosCategoria.put("Turismo", 10L);

        DashboardAdminDTO dto = new DashboardAdminDTO(
                100L,
                50L,
                20L,
                15L,
                reservasEstado,
                emprendimientosCategoria
        );

        assertEquals(100L, dto.getTotalUsuarios());
        assertEquals(50L, dto.getTotalReservas());
        assertEquals(20L, dto.getTotalEmprendimientos());
        assertEquals(15L, dto.getTotalResenas());
        assertEquals(reservasEstado, dto.getReservasPorEstado());
        assertEquals(emprendimientosCategoria, dto.getEmprendimientosPorCategoria());
    }

    @Test
    void testSettersAndGetters() {
        DashboardAdminDTO dto = new DashboardAdminDTO();

        dto.setTotalUsuarios(200L);
        dto.setTotalReservas(80L);
        dto.setTotalEmprendimientos(40L);
        dto.setTotalResenas(25L);

        Map<String, Long> map1 = Map.of("CONFIRMADA", 12L);
        Map<String, Long> map2 = Map.of("Aventura", 7L);

        dto.setReservasPorEstado(map1);
        dto.setEmprendimientosPorCategoria(map2);

        assertEquals(200L, dto.getTotalUsuarios());
        assertEquals(80L, dto.getTotalReservas());
        assertEquals(40L, dto.getTotalEmprendimientos());
        assertEquals(25L, dto.getTotalResenas());
        assertEquals(map1, dto.getReservasPorEstado());
        assertEquals(map2, dto.getEmprendimientosPorCategoria());
    }

    @Test
    void testEqualsAndHashCode() {
        Map<String, Long> map = Map.of("OK", 1L);

        DashboardAdminDTO dto1 = new DashboardAdminDTO(1L, 2L, 3L, 4L, map, map);
        DashboardAdminDTO dto2 = new DashboardAdminDTO(1L, 2L, 3L, 4L, map, map);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEquals() {
        DashboardAdminDTO dto1 = new DashboardAdminDTO();
        dto1.setTotalUsuarios(10L);

        DashboardAdminDTO dto2 = new DashboardAdminDTO();
        dto2.setTotalUsuarios(20L);

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardAdminDTO dto = new DashboardAdminDTO();
        dto.setTotalUsuarios(5L);
        dto.setTotalReservas(10L);

        String output = dto.toString();

        assertTrue(output.contains("5"));
        assertTrue(output.contains("10"));
        assertTrue(output.contains("DashboardAdminDTO"));
    }

    @Test
    void testModificarMaps() {
        DashboardAdminDTO dto = new DashboardAdminDTO();

        Map<String, Long> estadoInicial = new HashMap<>();
        estadoInicial.put("PENDIENTE", 2L);

        Map<String, Long> estadoModificado = new HashMap<>();
        estadoModificado.put("CONFIRMADA", 5L);

        dto.setReservasPorEstado(estadoInicial);
        assertEquals(2L, dto.getReservasPorEstado().get("PENDIENTE"));

        dto.setReservasPorEstado(estadoModificado);
        assertEquals(5L, dto.getReservasPorEstado().get("CONFIRMADA"));
    }
}
