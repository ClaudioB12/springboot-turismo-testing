package pe.edu.upeu.turismospringboot.service;

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
import pe.edu.upeu.turismospringboot.service.impl.UbicacionServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UbicacionServiceTest {

    @Mock
    private EmprendimientoRepository emprendimientoRepository;

    @Mock
    private LugarRepository lugarRepository;

    @InjectMocks
    private UbicacionServiceImpl ubicacionService;

    private Lugar lugar;
    private Emprendimiento emprendimiento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        lugar = new Lugar();
        lugar.setNombre("Capachica Centro");
        lugar.setDescripcion("Lugar turístico");
        lugar.setLatitud(-15.51);
        lugar.setLongitud(-70.12);
        lugar.setImagenUrl("lugar.png");

        emprendimiento = new Emprendimiento();
        emprendimiento.setNombre("Eco Lodge");
        emprendimiento.setDescripcion("Hospedaje rural");
        emprendimiento.setLatitud(-15.55);
        emprendimiento.setLongitud(-70.10);
        emprendimiento.setImagenUrl("eco.png");
    }

    // ============================================================
    // SOLO LUGARES
    // ============================================================

    @Test
    void testObtenerUbicaciones_SoloLugares() {
        when(lugarRepository.findAll()).thenReturn(List.of(lugar));
        when(emprendimientoRepository.findAll()).thenReturn(List.of());

        List<UbicacionDTO> result = ubicacionService.obtenerTodasLasUbicaciones();

        assertEquals(1, result.size());
        UbicacionDTO dto = result.get(0);

        assertEquals("Capachica Centro", dto.getTitulo());
        assertEquals("lugar", dto.getTipo());
    }

    // ============================================================
    // SOLO EMPRENDIMIENTOS
    // ============================================================

    @Test
    void testObtenerUbicaciones_SoloEmprendimientos() {
        when(lugarRepository.findAll()).thenReturn(List.of());
        when(emprendimientoRepository.findAll()).thenReturn(List.of(emprendimiento));

        List<UbicacionDTO> result = ubicacionService.obtenerTodasLasUbicaciones();

        assertEquals(1, result.size());
        UbicacionDTO dto = result.get(0);

        assertEquals("Eco Lodge", dto.getTitulo());
        assertEquals("emprendimiento", dto.getTipo());
    }

    // ============================================================
    // LUGARES + EMPRENDIMIENTOS
    // ============================================================

    @Test
    void testObtenerUbicaciones_Mixto() {
        when(lugarRepository.findAll()).thenReturn(List.of(lugar));
        when(emprendimientoRepository.findAll()).thenReturn(List.of(emprendimiento));

        List<UbicacionDTO> result = ubicacionService.obtenerTodasLasUbicaciones();

        assertEquals(2, result.size());
        assertEquals("Capachica Centro", result.get(0).getTitulo());
        assertEquals("Eco Lodge", result.get(1).getTitulo());
    }

    // ============================================================
    // LISTAS VACÍAS
    // ============================================================

    @Test
    void testObtenerUbicaciones_Vacio() {
        when(lugarRepository.findAll()).thenReturn(List.of());
        when(emprendimientoRepository.findAll()).thenReturn(List.of());

        List<UbicacionDTO> result = ubicacionService.obtenerTodasLasUbicaciones();

        assertTrue(result.isEmpty());
    }
}
