package pe.edu.upeu.turismospringboot.controller.general;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;
import pe.edu.upeu.turismospringboot.service.LugarService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LugarControllerGeneralTest {

    @Mock
    private LugarService lugarService;

    @InjectMocks
    private LugarControllerGeneral lugarControllerGeneral;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------------------------
    // 1. TEST listarLugares()
    // ------------------------------------------------------------
    @Test
    void testListarLugares() {
        Lugar lugar1 = new Lugar();
        lugar1.setIdLugar(1L);
        lugar1.setNombre("Capachica");

        Lugar lugar2 = new Lugar();
        lugar2.setIdLugar(2L);
        lugar2.setNombre("Llachón");

        List<Lugar> lugares = Arrays.asList(lugar1, lugar2);

        when(lugarService.getlugares()).thenReturn(lugares);

        ResponseEntity<List<Lugar>> response = lugarControllerGeneral.listarLugares();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(lugarService, times(1)).getlugares();
    }

    // ------------------------------------------------------------
    // 2. TEST buscarPorNombre(nombre)
    // ------------------------------------------------------------
    @Test
    void testBuscarPorNombre() {
        Lugar lugar = new Lugar();
        lugar.setIdLugar(1L);
        lugar.setNombre("Capachica");

        List<Lugar> lugares = List.of(lugar);

        when(lugarService.buscarLugarPorNombre("Capachica"))
                .thenReturn(lugares);

        ResponseEntity<List<Lugar>> response =
                lugarControllerGeneral.buscarPorNombre("Capachica");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(lugarService, times(1)).buscarLugarPorNombre("Capachica");
    }

    // ------------------------------------------------------------
    // 3. TEST obtenerFamiliasPorLugar(idLugar, sin nombre)
    // ------------------------------------------------------------
    @Test
    void testObtenerFamiliasPorLugar_SinNombre() {
        Familia familia = new Familia();
        familia.setIdFamilia(1L);
        familia.setNombre("Familia A");

        List<Familia> familias = List.of(familia);

        when(lugarService.getFamiliasPorLugar(1L, null))
                .thenReturn(familias);

        ResponseEntity<List<Familia>> response =
                lugarControllerGeneral.obtenerFamiliasPorLugar(1L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(lugarService, times(1)).getFamiliasPorLugar(1L, null);
    }

    // ------------------------------------------------------------
    // 4. TEST obtenerFamiliasPorLugar(idLugar, con filtro nombre)
    // ------------------------------------------------------------
    @Test
    void testObtenerFamiliasPorLugar_ConNombre() {
        Familia familia = new Familia();
        familia.setIdFamilia(2L);
        familia.setNombre("Familia B");

        List<Familia> familias = List.of(familia);

        when(lugarService.getFamiliasPorLugar(1L, "B"))
                .thenReturn(familias);

        ResponseEntity<List<Familia>> response =
                lugarControllerGeneral.obtenerFamiliasPorLugar(1L, "B");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(lugarService, times(1)).getFamiliasPorLugar(1L, "B");
    }
}
