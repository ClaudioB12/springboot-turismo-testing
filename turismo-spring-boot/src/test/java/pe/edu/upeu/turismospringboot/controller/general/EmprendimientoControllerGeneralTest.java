package pe.edu.upeu.turismospringboot.controller.general;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.service.EmprendimientoService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmprendimientoControllerGeneralTest {

    @InjectMocks
    private EmprendimientoControllerGeneral controller;

    @Mock
    private EmprendimientoService emprendimientoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------
    // GET /general/emprendimiento
    // -------------------------------------------------------------
    @Test
    void testObtenerEmprendimientos() {
        List<Emprendimiento> lista = List.of(new Emprendimiento(), new Emprendimiento());

        when(emprendimientoService.getEmprendimientos()).thenReturn(lista);

        ResponseEntity<List<Emprendimiento>> response = controller.obtenerEmprendimientos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
    }

    // -------------------------------------------------------------
    // GET /general/emprendimiento/buscar?nombre=
    // -------------------------------------------------------------
    @Test
    void testBuscarPorNombre() {
        List<Emprendimiento> lista = List.of(new Emprendimiento());

        when(emprendimientoService.buscarPorNombre("artesanias")).thenReturn(lista);

        ResponseEntity<List<Emprendimiento>> response =
                controller.buscarPorNombre("artesanias");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
    }

    // -------------------------------------------------------------
    // GET /general/emprendimiento/{idEmprendimiento}
    // -------------------------------------------------------------
    @Test
    void testObtenerEmprendimientoPorId() {
        Emprendimiento e = new Emprendimiento();
        e.setIdEmprendimiento(1L);

        when(emprendimientoService.getEmprendimientoById(1L)).thenReturn(e);

        ResponseEntity<Emprendimiento> response =
                controller.obtenerEmprendimientoPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(e, response.getBody());
    }
}
