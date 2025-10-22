package pe.edu.upeu.turismospringboot.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.controller.admin.CategoriaController;
import pe.edu.upeu.turismospringboot.model.dto.CategoriaDto;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.service.CategoriaService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerCategorias() {
        when(categoriaService.getCategorias()).thenReturn(List.of(new Categoria()));
        ResponseEntity<List<Categoria>> response = categoriaController.obtenerCategorias();
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testObtenerCategoriaPorId() {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1L);
        when(categoriaService.getCategoriaById(1L)).thenReturn(categoria);
        ResponseEntity<Categoria> response = categoriaController.obtenerCategoriaPorId(1L);
        assertEquals(1L, response.getBody().getIdCategoria());
    }

    @Test
    void testEliminarCategoria() {
        doNothing().when(categoriaService).deleteCategoria(1L);
        ResponseEntity<String> response = categoriaController.eliminarCategoria(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testBuscarPorNombre() {
        when(categoriaService.buscarPorNombre("Hotelería")).thenReturn(List.of(new Categoria()));
        ResponseEntity<List<Categoria>> response = categoriaController.buscarPorNombre("Hotelería");
        assertEquals(1, response.getBody().size());
    }
}
