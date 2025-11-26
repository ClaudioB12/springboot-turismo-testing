package pe.edu.upeu.turismospringboot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.upeu.turismospringboot.model.dto.UbicacionDTO;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;
import pe.edu.upeu.turismospringboot.repository.EmprendimientoRepository;
import pe.edu.upeu.turismospringboot.repository.LugarRepository;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UbicacionServiceImplTest {

    @Mock
    private EmprendimientoRepository emprendimientoRepository;

    @Mock
    private LugarRepository lugarRepository;

    @InjectMocks
    private UbicacionServiceImpl service;

    private Lugar lugar1;
    private Emprendimiento emp1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Lugar de prueba
        lugar1 = new Lugar();
        lugar1.setNombre("Capachica Pueblo");
        lugar1.setDescripcion("Centro tradicional");
        lugar1.setLatitud(-15.51);
        lugar1.setLongitud(-70.12);
        lugar1.setImagenUrl("lugar.jpg");

        // Emprendimiento de prueba
        emp1 = new Emprendimiento();
        emp1.setNombre("Eco Lodge Titicaca");
        emp1.setDescripcion("Hospedaje en la zona rural");
        emp1.setLatitud(-15.55);
        emp1.setLongitud(-70.10);
        emp1.setImagenUrl("emp.jpg");
    }

    // ============================================================
    // CASO 1: SOLO LUGARES
    // ============================================================

    @Test
    void testObtenerTodasLasUbicaciones_SoloLugares() {
        when(lugarRepository.findAll()).thenReturn(List.of(lugar1));
        when(emprendimientoRepository.findAll()).thenReturn(List.of());

        List<UbicacionDTO> result = service.obtenerTodasLasUbicaciones();

        assertEquals(1, result.size());
        UbicacionDTO dto = result.get(0);

        assertEquals(-15.51, dto.getLat());
        assertEquals(-70.12, dto.getLng());
        assertEquals("Capachica Pueblo", dto.getTitulo());
        assertEquals("lugar", dto.getTipo());
        assertEquals("Centro tradicional", dto.getDescripcion());
        assertEquals("lugar.jpg", dto.getImagen());

        verify(lugarRepository, times(1)).findAll();
        verify(emprendimientoRepository, times(1)).findAll();
    }

    // ============================================================
    // CASO 2: SOLO EMPRENDIMIENTOS
    // ============================================================

    @Test
    void testObtenerTodasLasUbicaciones_SoloEmprendimientos() {
        when(lugarRepository.findAll()).thenReturn(List.of());
        when(emprendimientoRepository.findAll()).thenReturn(List.of(emp1));

        List<UbicacionDTO> result = service.obtenerTodasLasUbicaciones();

        assertEquals(1, result.size());
        UbicacionDTO dto = result.get(0);

        assertEquals(-15.55, dto.getLat());
        assertEquals(-70.10, dto.getLng());
        assertEquals("Eco Lodge Titicaca", dto.getTitulo());
        assertEquals("emprendimiento", dto.getTipo());
        assertEquals("Hospedaje en la zona rural", dto.getDescripcion());
        assertEquals("emp.jpg", dto.getImagen());

        verify(lugarRepository, times(1)).findAll();
        verify(emprendimientoRepository, times(1)).findAll();
    }

    // ============================================================
    // CASO 3: LUGARES + EMPRENDIMIENTOS
    // ============================================================

    @Test
    void testObtenerTodasLasUbicaciones_LugaresYEmprendimientos() {
        when(lugarRepository.findAll()).thenReturn(List.of(lugar1));
        when(emprendimientoRepository.findAll()).thenReturn(List.of(emp1));

        List<UbicacionDTO> result = service.obtenerTodasLasUbicaciones();

        assertEquals(2, result.size());

        UbicacionDTO dtoLugar = result.get(0);
        UbicacionDTO dtoEmp = result.get(1);

        assertEquals("Capachica Pueblo", dtoLugar.getTitulo());
        assertEquals("Eco Lodge Titicaca", dtoEmp.getTitulo());
    }

    // ============================================================
    // CASO 4: LISTAS VACÍAS
    // ============================================================

    @Test
    void testObtenerTodasLasUbicaciones_Vacio() {
        when(lugarRepository.findAll()).thenReturn(List.of());
        when(emprendimientoRepository.findAll()).thenReturn(List.of());

        List<UbicacionDTO> result = service.obtenerTodasLasUbicaciones();

        assertTrue(result.isEmpty());
    }
}
