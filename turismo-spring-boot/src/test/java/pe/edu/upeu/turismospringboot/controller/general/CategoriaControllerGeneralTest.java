package pe.edu.upeu.turismospringboot.controller.general;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.service.CategoriaService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaControllerGeneralTest {

    @InjectMocks
    private CategoriaControllerGeneral controller;

    @Mock
    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------
    // GET /general/categoria
    // -------------------------------------------------------------
    @Test
    void testObtenerCategorias() {

        List<Categoria> lista = List.of(new Categoria(), new Categoria());
        when(categoriaService.getCategorias()).thenReturn(lista);

        ResponseEntity<List<Categoria>> response = controller.obtenerCategorias();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
    }

    // -------------------------------------------------------------
    // GET /general/categoria/{idCategoria}
    // -------------------------------------------------------------
    @Test
    void testObtenerCategoriaPorId() {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1L);

        when(categoriaService.getCategoriaById(1L)).thenReturn(categoria);

        ResponseEntity<Categoria> response = controller.obtenerCategoriaPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoria, response.getBody());
    }

    // -------------------------------------------------------------
    // GET /general/categoria/buscar?nombre=X
    // -------------------------------------------------------------
    @Test
    void testBuscarPorNombre() {
        List<Categoria> lista = List.of(new Categoria());
        when(categoriaService.buscarPorNombre("playas")).thenReturn(lista);

        ResponseEntity<List<Categoria>> response =
                controller.buscarPorNombre("playas");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
    }
}
