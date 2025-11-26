package pe.edu.upeu.turismospringboot.util.dataLoaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.repository.EmprendimientoRepository;
import pe.edu.upeu.turismospringboot.repository.ServicioTuristicoRepository;

import java.util.List;

import static org.mockito.Mockito.*;

class ServicioTuristicoDataLoaderTest {

    @Mock
    private EmprendimientoRepository emprendimientoRepository;

    @Mock
    private ServicioTuristicoRepository servicioTuristicoRepository;

    @InjectMocks
    private ServicioTuristicoDataLoader dataLoader;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ============================================================
    // 1️⃣ Caso: YA EXISTEN servicios → no cargar nada
    // ============================================================

    @Test
    void testRun_NoCargaCuandoYaHayServicios() throws Exception {
        when(servicioTuristicoRepository.count()).thenReturn(5L);

        dataLoader.run();

        verify(emprendimientoRepository, never()).findAll();
        verify(servicioTuristicoRepository, never()).save(any());
    }

    // ============================================================
    // 2️⃣ Caso: BD vacía → crear todos los servicios según los nombres
    // ============================================================

    @Test
    void testRun_CreaServiciosCorrectos() throws Exception {
        when(servicioTuristicoRepository.count()).thenReturn(0L);

        // Crear lista de emprendimientos con los nombres EXACTOS
        List<Emprendimiento> emps = List.of(
                emp("Hostal Estrella Andina"),
                emp("Sabores de Lunaria"),
                emp("Manos de Barro"),
                emp("Ciclotur Lunaria"),
                emp("Aventura Kayak"),
                emp("Museo Vivo Lunaria"),
                emp("Pack Aventurero")
        );

        when(emprendimientoRepository.findAll()).thenReturn(emps);

        dataLoader.run();

        // TOTAL esperado: 10 servicios
        verify(servicioTuristicoRepository, times(10)).save(any());
    }

    // ============================================================
    // 3️⃣ Validación clave: crearServicio() llama a save()
    // ============================================================

    @Test
    void testCrearServicio_LlamaSave() throws Exception {
        Emprendimiento emp = emp("Test Emp");

        dataLoader.run(); // no importa, testearemos con invocación indirecta

        // Llamada manual al método privado mediante reflexión NO SE HACE.
        // En cambio: simulamos creando un emprendimiento que cae en un case.

        when(servicioTuristicoRepository.count()).thenReturn(0L);
        when(emprendimientoRepository.findAll()).thenReturn(List.of(emp("Manos de Barro")));

        dataLoader.run();

        // "Manos de Barro" → 1 servicio
        verify(servicioTuristicoRepository, times(1)).save(any());
    }

    // ============================================================
    // Helper para crear emprendimientos reales
    // ============================================================

    private Emprendimiento emp(String nombre) {
        Emprendimiento e = new Emprendimiento();
        e.setNombre(nombre);
        return e;
    }
}
