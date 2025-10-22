package pe.edu.upeu.turismospringboot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.CategoriaDto;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.repository.CategoriaRepository;
import pe.edu.upeu.turismospringboot.util.ArchivoUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del Servicio de Categoría")
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria categoria;
    private CategoriaDto categoriaDto;
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setIdCategoria(1L);
        categoria.setNombre("Hotelería");
        categoria.setDescripcion("Alojamientos y hospedajes");
        categoria.setImagenUrl("hoteleria.png");
        categoria.setFechaCreacionCategoria(LocalDateTime.now());

        categoriaDto = new CategoriaDto();
        categoriaDto.setNombre("Hotelería");
        categoriaDto.setDescripcion("Alojamientos y hospedajes");

        mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );
    }

    @Test
    @DisplayName("Debe obtener todas las categorías")
    void testGetCategorias() {
        // Arrange
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findAll()).thenReturn(categorias);

        // Act
        List<Categoria> result = categoriaService.getCategorias();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hotelería", result.get(0).getNombre());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener categoría por ID existente")
    void testGetCategoriaById_ExisteCategoria() {
        // Arrange
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // Act
        Categoria result = categoriaService.getCategoriaById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdCategoria());
        assertEquals("Hotelería", result.getNombre());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando categoría no existe")
    void testGetCategoriaById_NoExisteCategoria() {
        // Arrange
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoriaService.getCategoriaById(999L);
        });

        assertTrue(exception.getMessage().contains("no existe"));
        verify(categoriaRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debe actualizar categoría existente")
    void testPutCategoria_ExisteCategoria() {
        // Arrange
        CategoriaDto updateDto = new CategoriaDto();
        updateDto.setNombre("Hotelería Premium");
        updateDto.setDescripcion("Alojamientos de lujo");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.saveAndFlush(any(Categoria.class))).thenReturn(categoria);

        // Act
        Categoria result = categoriaService.putCategoria(1L, updateDto, null);

        // Assert
        assertNotNull(result);
        assertEquals("Hotelería Premium", result.getNombre());
        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).saveAndFlush(any(Categoria.class));
    }


    @Test
    @DisplayName("Debe lanzar excepción al actualizar categoría inexistente")
    void testPutCategoria_NoExisteCategoria() {
        // Arrange
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            categoriaService.putCategoria(999L, categoriaDto, null);
        });

        verify(categoriaRepository, times(1)).findById(999L);
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Debe eliminar categoría por ID existente")
    void testDeleteCategoria() {
        // Arrange
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoriaRepository).deleteById(1L);

        // Act
        categoriaService.deleteCategoria(1L);

        // Assert
        verify(categoriaRepository, times(1)).existsById(1L);
        verify(categoriaRepository, times(1)).deleteById(1L);
    }


    @Test
    @DisplayName("Debe buscar categorías por nombre")
    void testBuscarPorNombre() {
        // Arrange
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.buscarPorNombre("Hotel")).thenReturn(categorias);

        // Act
        List<Categoria> result = categoriaService.buscarPorNombre("Hotel");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hotelería", result.get(0).getNombre());
        verify(categoriaRepository, times(1)).buscarPorNombre("Hotel");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no encuentra categorías por nombre")
    void testBuscarPorNombre_NoEncuentra() {
        // Arrange
        when(categoriaRepository.buscarPorNombre("Inexistente")).thenReturn(Arrays.asList());

        // Act
        List<Categoria> result = categoriaService.buscarPorNombre("Inexistente");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoriaRepository, times(1)).buscarPorNombre("Inexistente");
    }
}