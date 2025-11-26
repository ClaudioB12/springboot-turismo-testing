package pe.edu.upeu.turismospringboot.util.dataLoaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.turismospringboot.repository.CategoriaRepository;
import pe.edu.upeu.turismospringboot.repository.FamiliaRepository;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaDataLoaderTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private FamiliaRepository familiaRepository;

    @InjectMocks
    private CategoriaDataLoader categoriaDataLoader;

    @BeforeEach
    void setUp() {
        // Configuración inicial de los objetos antes de cada test
    }

    @Test
    void testRun_whenNoCategoriesInDB_shouldLoadCategories() {
        // Arrange: Configurar el comportamiento del repositorio
        when(categoriaRepository.count()).thenReturn(0L);  // Simula que no hay categorías en la base de datos

        // Act: Ejecutar el método run
        categoriaDataLoader.run();

        // Assert: Verificar que el repositorio ha guardado las categorías correctamente
        verify(categoriaRepository, times(1)).saveAll(anyList());  // Verifica que se llame saveAll una vez con una lista de categorías
    }

    @Test
    void testRun_whenCategoriesAlreadyExist_shouldNotLoadCategories() {
        // Arrange: Configurar el comportamiento del repositorio
        when(categoriaRepository.count()).thenReturn(5L);  // Simula que ya hay 5 categorías en la base de datos

        // Act: Ejecutar el método run
        categoriaDataLoader.run();

        // Assert: Verificar que saveAll no se haya llamado, ya que las categorías ya existen
        verify(categoriaRepository, times(0)).saveAll(anyList());  // Verifica que no se llame saveAll
    }
}
