package pe.edu.upeu.turismospringboot.util.dataLoaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.model.entity.FamiliaCategoria;
import pe.edu.upeu.turismospringboot.repository.EmprendimientoRepository;
import pe.edu.upeu.turismospringboot.repository.FamiliaCategoriaRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EmprendimientoDataLoaderTest {

    @Mock
    private EmprendimientoRepository emprendimientoRepository;

    @Mock
    private FamiliaCategoriaRepository familiaCategoriaRepository;

    @InjectMocks
    private EmprendimientoDataLoader dataLoader;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ============================================================
    // 1️⃣ Caso: YA EXISTEN emprendimientos → NO carga nada
    // ============================================================
    @Test
    void testRun_NoCargaCuandoYaHayDatos() throws Exception {
        when(emprendimientoRepository.count()).thenReturn(5L);

        dataLoader.run();

        verify(familiaCategoriaRepository, never()).findAll();
        verify(emprendimientoRepository, never()).saveAll(anyList());
    }

    // ============================================================
    // 2️⃣ Caso: Hay menos de 7 FamiliaCategoria → NO inserta nada
    // ============================================================
    @Test
    void testRun_NoCargaCuandoHayMenosDe7Familias() throws Exception {
        when(emprendimientoRepository.count()).thenReturn(0L);

        List<FamiliaCategoria> pocasFamilias = List.of(
                new FamiliaCategoria(), new FamiliaCategoria(), new FamiliaCategoria()
        );

        when(familiaCategoriaRepository.findAll()).thenReturn(pocasFamilias);

        dataLoader.run();

        verify(emprendimientoRepository, never()).saveAll(anyList());
    }

    // ============================================================
    // 3️⃣ Caso: BD vacía y >=7 familias → DEBE insertar 7 emprendimientos
    // ============================================================
    @Test
    void testRun_CargaCorrecta() throws Exception {
        when(emprendimientoRepository.count()).thenReturn(0L);

        List<FamiliaCategoria> familias = new ArrayList<>();
        for (int i = 0; i < 7; i++) familias.add(new FamiliaCategoria());

        when(familiaCategoriaRepository.findAll()).thenReturn(familias);

        dataLoader.run();

        // Captura lista enviada a saveAll(...)
        var captor = ArgumentCaptor.forClass(List.class);
        verify(emprendimientoRepository).saveAll(captor.capture());

        List<Emprendimiento> lista = captor.getValue();
        assertEquals(7, lista.size());
    }
}
