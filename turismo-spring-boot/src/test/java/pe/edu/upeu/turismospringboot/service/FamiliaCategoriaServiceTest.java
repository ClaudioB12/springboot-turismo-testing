package pe.edu.upeu.turismospringboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.upeu.turismospringboot.model.dto.FamiliaCategoriaDto;
import pe.edu.upeu.turismospringboot.model.dto.FamiliaCategoriaDtoPost;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.FamiliaCategoria;
import pe.edu.upeu.turismospringboot.repository.FamiliaCategoriaRepository;
import pe.edu.upeu.turismospringboot.service.impl.FamiliaCategoriaServiceImpl;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Pruebas del servicio FamiliaCategoriaServiceImpl")
class FamiliaCategoriaServiceTest {

    @InjectMocks
    private FamiliaCategoriaServiceImpl familiaCategoriaService;

    @Mock
    private FamiliaCategoriaRepository familiaCategoriaRepository;

    private Familia familia;
    private Categoria categoria;
    private FamiliaCategoria familiaCategoria;

    @BeforeEach
    void setUp() {
        // Crear familia y categoria
        familia = new Familia();
        familia.setIdFamilia(1L);
        familia.setNombre("Aventura");

        categoria = new Categoria();
        categoria.setIdCategoria(10L);
        categoria.setNombre("Turismo");

        // Crear una relación FamiliaCategoria válida
        familiaCategoria = new FamiliaCategoria();
        familiaCategoria.setIdFamiliaCategoria(100L);
        familiaCategoria.setFamilia(familia);
        familiaCategoria.setCategoria(categoria);
    }


    @Test
    @DisplayName("Debe listar todas las relaciones de familia y categoría")
    void testListarRelaciones() {
        // Arrange
        when(familiaCategoriaRepository.findAll()).thenReturn(List.of(familiaCategoria));

        // Act
        List<FamiliaCategoriaDto> result = familiaCategoriaService.listarRelaciones();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Aventura", result.get(0).getNombreFamilia());
        assertEquals("Turismo", result.get(0).getNombreCategoria());
    }

    @Test
    @DisplayName("Debe obtener las relaciones de familia y categoría por ID de familia")
    void testObtenerFamiliaCategoriaPorIdFamilia() {
        // Arrange
        when(familiaCategoriaRepository.findByFamiliaIdFamilia(1L)).thenReturn(List.of(familiaCategoria));

        // Act
        List<FamiliaCategoriaDto> result = familiaCategoriaService.obtenerFamiliaCategoriaPorIdFamilia(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Aventura", result.get(0).getNombreFamilia());
    }

    @Test
    @DisplayName("Debe obtener las relaciones de familia y categoría por ID de categoría")
    void testObtenerFamiliaCategoriaPorIdCategoria() {
        // Arrange
        when(familiaCategoriaRepository.findByCategoriaIdCategoria(10L)).thenReturn(List.of(familiaCategoria));

        // Act
        List<FamiliaCategoriaDto> result = familiaCategoriaService.obtenerFamiliaCategoriaPorIdCategoria(10L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Turismo", result.get(0).getNombreCategoria());
    }

    @Test
    @DisplayName("Debe eliminar una relación correctamente")
    void testEliminarRelacion() {
        // Arrange
        when(familiaCategoriaRepository.existsById(100L)).thenReturn(true);
        doNothing().when(familiaCategoriaRepository).deleteById(100L);

        // Act
        familiaCategoriaService.eliminarRelacion(100L);

        // Assert
        verify(familiaCategoriaRepository, times(1)).deleteById(100L);
    }


}
