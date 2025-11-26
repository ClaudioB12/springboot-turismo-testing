package pe.edu.upeu.turismospringboot.util.dataLoaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.FamiliaCategoria;
import pe.edu.upeu.turismospringboot.repository.CategoriaRepository;
import pe.edu.upeu.turismospringboot.repository.FamiliaCategoriaRepository;
import pe.edu.upeu.turismospringboot.repository.FamiliaRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FamiliaCategoriaDataLoaderTest {

    @Mock
    private FamiliaCategoriaRepository familiaCategoriaRepository;

    @Mock
    private FamiliaRepository familiaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private FamiliaCategoriaDataLoader familiaCategoriaDataLoader;

    private Familia familia1;
    private Familia familia2;
    private Categoria categoria1;
    private Categoria categoria2;
    private Categoria categoria3;
    private Categoria categoria4;
    private Categoria categoria5;
    private Categoria categoria6;
    private Categoria categoria7;

    @BeforeEach
    void setUp() {
        familia1 = new Familia();
        familia1.setIdFamilia(1L);
        familia1.setNombre("Familia A");

        familia2 = new Familia();
        familia2.setIdFamilia(2L);
        familia2.setNombre("Familia B");

        categoria1 = new Categoria();
        categoria1.setIdCategoria(1L);
        categoria1.setNombre("Hotelería");

        categoria2 = new Categoria();
        categoria2.setIdCategoria(2L);
        categoria2.setNombre("Gastronomía");

        categoria3 = new Categoria();
        categoria3.setIdCategoria(3L);
        categoria3.setNombre("Artesanía");

        categoria4 = new Categoria();
        categoria4.setIdCategoria(4L);
        categoria4.setNombre("Cycling");

        categoria5 = new Categoria();
        categoria5.setIdCategoria(5L);
        categoria5.setNombre("Kayak");

        categoria6 = new Categoria();
        categoria6.setIdCategoria(6L);
        categoria6.setNombre("Cultura");

        categoria7 = new Categoria();
        categoria7.setIdCategoria(7L);
        categoria7.setNombre("Paquetes");
    }

    @Test
    void testRun_whenNoFamiliaCategoriaExists_shouldInsertFamiliaCategoriaRelations() {
        // Arrange
        when(familiaCategoriaRepository.count()).thenReturn(0L);
        when(familiaRepository.findAll()).thenReturn(Arrays.asList(familia1, familia2));
        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoria1, categoria2, categoria3, categoria4, categoria5, categoria6, categoria7));

        // Act
        familiaCategoriaDataLoader.run();

        // Assert
        verify(familiaCategoriaRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testRun_whenFamiliaCategoriaExists_shouldNotInsertFamiliaCategoriaRelations() {
        // Arrange
        when(familiaCategoriaRepository.count()).thenReturn(1L);  // FamiliaCategoria ya existe en la base de datos

        // Act
        familiaCategoriaDataLoader.run();

        // Assert
        verify(familiaCategoriaRepository, times(0)).saveAll(anyList());
    }

    @Test
    void testRun_whenInsufficientFamiliasOrCategorias_shouldNotInsertFamiliaCategoriaRelations() {
        // Arrange: Menos de 2 familias o 7 categorías
        when(familiaCategoriaRepository.count()).thenReturn(0L);
        when(familiaRepository.findAll()).thenReturn(Arrays.asList(familia1));  // Solo 1 familia
        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoria1, categoria2, categoria3, categoria4, categoria5, categoria6));  // Solo 6 categorías

        // Act
        familiaCategoriaDataLoader.run();

        // Assert
        verify(familiaCategoriaRepository, times(0)).saveAll(anyList());
    }
}
