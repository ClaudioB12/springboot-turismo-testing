package pe.edu.upeu.turismospringboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.CategoriaDto;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.repository.CategoriaRepository;
import pe.edu.upeu.turismospringboot.service.impl.CategoriaServiceImpl;
import pe.edu.upeu.turismospringboot.util.ArchivoUtil;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ArchivoUtil archivoUtil;

    private Categoria categoria;
    private CategoriaDto categoriaDto;

    @BeforeEach
    void setUp() {
        // Set up mock data for CategoriaDto
        categoriaDto = new CategoriaDto();
        categoriaDto.setNombre("Hotelería");
        categoriaDto.setDescripcion("Alojamientos y hospedajes");

        // Set up mock Categoria entity
        categoria = new Categoria();
        categoria.setIdCategoria(1L);
        categoria.setNombre("Hotelería");
        categoria.setDescripcion("Alojamientos y hospedajes");
    }

    @Test
    void testGetCategorias() {
        // Arrange
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        // Act
        List<Categoria> categorias = categoriaService.getCategorias();

        // Assert
        assertNotNull(categorias);
        assertEquals(1, categorias.size());
        assertEquals("Hotelería", categorias.get(0).getNombre());
    }

    @Test
    void testGetCategoriaById() {
        // Arrange
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // Act
        Categoria foundCategoria = categoriaService.getCategoriaById(1L);

        // Assert
        assertNotNull(foundCategoria);
        assertEquals("Hotelería", foundCategoria.getNombre());
    }

    @Test
    void testGetCategoriaById_NotFound() {
        // Arrange
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> categoriaService.getCategoriaById(999L));
        assertEquals("La categoria con id 999 no existe", thrown.getMessage());
    }


    @Test
    void testDeleteCategoria() {
        // Arrange
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        // Act
        categoriaService.deleteCategoria(1L);

        // Assert
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategoria_NotFound() {
        // Arrange
        when(categoriaRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> categoriaService.deleteCategoria(999L));
        assertEquals("La categoria con id 999 no existe", thrown.getMessage());
    }

    @Test
    void testBuscarPorNombre() {
        // Arrange
        when(categoriaRepository.buscarPorNombre("Hotel")).thenReturn(List.of(categoria));

        // Act
        List<Categoria> result = categoriaService.buscarPorNombre("Hotel");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hotelería", result.get(0).getNombre());
    }
}
