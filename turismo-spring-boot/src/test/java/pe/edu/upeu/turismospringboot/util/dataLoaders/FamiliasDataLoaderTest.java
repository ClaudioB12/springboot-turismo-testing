package pe.edu.upeu.turismospringboot.util.dataLoaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;
import pe.edu.upeu.turismospringboot.repository.FamiliaRepository;
import pe.edu.upeu.turismospringboot.repository.LugarRepository;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FamiliasDataLoaderTest {

    @Mock
    private FamiliaRepository familiaRepository;

    @Mock
    private LugarRepository lugarRepository;

    @InjectMocks
    private FamiliasDataLoader dataLoader;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ============================================================
    // 1️⃣ Caso: YA EXISTEN familias → no cargar nada
    // ============================================================

    @Test
    void testRun_NoCargaCuandoYaHayFamilias() throws Exception {
        when(familiaRepository.count()).thenReturn(3L);

        dataLoader.run();

        verify(lugarRepository, never()).findAll();
        verify(familiaRepository, never()).saveAll(anyList());
    }

    // ============================================================
    // 2️⃣ Caso: menos de 2 lugares → no insertar
    // ============================================================

    @Test
    void testRun_NoCargaSiHayMenosDeDosLugares() throws Exception {
        when(familiaRepository.count()).thenReturn(0L);

        List<Lugar> pocosLugares = List.of(new Lugar()); // solo 1 lugar
        when(lugarRepository.findAll()).thenReturn(pocosLugares);

        dataLoader.run();

        verify(familiaRepository, never()).saveAll(anyList());
    }

    // ============================================================
    // 3️⃣ Caso: BD vacía y >=2 lugares → insertar 2 familias
    // ============================================================

    @Test
    void testRun_CargaCorrecta() throws Exception {
        when(familiaRepository.count()).thenReturn(0L);

        List<Lugar> lugares = new ArrayList<>();
        lugares.add(new Lugar());
        lugares.add(new Lugar());
        lugares.add(new Lugar()); // si hay más, no importa, solo usa los primeros 2

        when(lugarRepository.findAll()).thenReturn(lugares);

        dataLoader.run();

        // Capturar argumentos enviados al saveAll
        ArgumentCaptor<List<Familia>> captor = ArgumentCaptor.forClass(List.class);
        verify(familiaRepository).saveAll(captor.capture());

        List<Familia> familiasGuardadas = captor.getValue();

        assertEquals(2, familiasGuardadas.size());
        assertEquals("Guardianes del Valle", familiasGuardadas.get(0).getNombre());
        assertEquals("Susurradores del Eco", familiasGuardadas.get(1).getNombre());
    }
}
