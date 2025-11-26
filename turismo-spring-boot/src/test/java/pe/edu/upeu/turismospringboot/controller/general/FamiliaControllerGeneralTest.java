package pe.edu.upeu.turismospringboot.controller.general;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.service.FamiliaService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FamiliaControllerGeneralTest {

    @Mock
    private FamiliaService familiaService;

    @InjectMocks
    private FamiliaControllerGeneral controller;

    // ---------------------------------------------------------------------
    // TEST: Obtener todas las familias
    // ---------------------------------------------------------------------
    @Test
    void testObtenerFamilias() {
        when(familiaService.getFamilias())
                .thenReturn(List.of(new Familia(), new Familia()));

        var response = controller.obtenerFamilias();

        assertEquals(2, response.getBody().size());
    }

    // ---------------------------------------------------------------------
    // TEST: Obtener familia por ID
    // ---------------------------------------------------------------------
    @Test
    void testObtenerFamiliaPorId() {
        Familia familia = new Familia();
        familia.setIdFamilia(1L);

        when(familiaService.getFamiliaById(1L)).thenReturn(familia);

        var response = controller.obtenerFamiliaPorId(1L);

        assertEquals(1L, response.getBody().getIdFamilia());
    }

    // ---------------------------------------------------------------------
    // TEST: Buscar familias por nombre
    // ---------------------------------------------------------------------
    @Test
    void testBuscarPorNombre() {
        when(familiaService.buscarPorNombre("turismo"))
                .thenReturn(List.of(new Familia()));

        var response = controller.buscarPorNombre("turismo");

        assertEquals(1, response.getBody().size());
    }
}
