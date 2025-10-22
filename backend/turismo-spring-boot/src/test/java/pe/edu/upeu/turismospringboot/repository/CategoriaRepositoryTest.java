package pe.edu.upeu.turismospringboot.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Tests del Repository de Categoría")
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria categoria1;
    private Categoria categoria2;

    @BeforeEach
    void setUp() {
        categoriaRepository.deleteAll();

        categoria1 = new Categoria();
        categoria1.setNombre("Hotelería");
        categoria1.setDescripcion("Alojamientos y hospedajes");
        categoria1.setImagenUrl("hoteleria.png");
        categoria1.setFechaCreacionCategoria(LocalDateTime.now());

        categoria2 = new Categoria();
        categoria2.setNombre("Gastronomía");
        categoria2.setDescripcion("Sabores locales");
        categoria2.setImagenUrl("gastronomia.png");
        categoria2.setFechaCreacionCategoria(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debe guardar una categoría correctamente")
    void testSaveCategoria() {
        // Act
        Categoria savedCategoria = categoriaRepository.save(categoria1);

        // Assert
        assertNotNull(savedCategoria);
        assertNotNull(savedCategoria.getIdCategoria());
        assertEquals("Hotelería", savedCategoria.getNombre());
        assertEquals("Alojamientos y hospedajes", savedCategoria.getDescripcion());
        assertNotNull(savedCategoria.getFechaCreacionCategoria());
    }

    @Test
    @DisplayName("Debe encontrar categoría por ID")
    void testFindById() {
        // Arrange
        Categoria savedCategoria = categoriaRepository.save(categoria1);

        // Act
        Optional<Categoria> foundCategoria = categoriaRepository.findById(savedCategoria.getIdCategoria());

        // Assert
        assertTrue(foundCategoria.isPresent());
        assertEquals("Hotelería", foundCategoria.get().getNombre());
    }

    @Test
    @DisplayName("Debe retornar empty cuando categoría no existe")
    void testFindById_NoExiste() {
        // Act
        Optional<Categoria> foundCategoria = categoriaRepository.findById(999L);

        // Assert
        assertFalse(foundCategoria.isPresent());
    }

    @Test
    @DisplayName("Debe encontrar categoría por nombre")
    void testFindByNombre() {
        // Arrange
        categoriaRepository.save(categoria1);

        // Act
        Optional<Categoria> foundCategoria = categoriaRepository.findByNombre("Hotelería");

        // Assert
        assertTrue(foundCategoria.isPresent());
        assertEquals("Hotelería", foundCategoria.get().getNombre());
    }

    @Test
    @DisplayName("Debe buscar categorías por nombre con LIKE")
    void testBuscarPorNombre() {
        // Arrange
        categoriaRepository.save(categoria1);
        categoriaRepository.save(categoria2);

        // Act
        List<Categoria> result = categoriaRepository.buscarPorNombre("gas");

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Gastronomía");
    }

    @Test
    @DisplayName("Debe buscar categorías sin importar mayúsculas/minúsculas")
    void testBuscarPorNombre_CaseInsensitive() {
        // Arrange
        categoriaRepository.save(categoria1);

        // Act
        List<Categoria> result1 = categoriaRepository.buscarPorNombre("HOTEL");
        List<Categoria> result2 = categoriaRepository.buscarPorNombre("hotel");
        List<Categoria> result3 = categoriaRepository.buscarPorNombre("HoTeL");

        // Assert
        assertThat(result1).hasSize(1);
        assertThat(result2).hasSize(1);
        assertThat(result3).hasSize(1);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no encuentra por nombre")
    void testBuscarPorNombre_NoEncuentra() {
        // Arrange
        categoriaRepository.save(categoria1);

        // Act
        List<Categoria> result = categoriaRepository.buscarPorNombre("Inexistente");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Debe obtener todas las categorías")
    void testFindAll() {
        // Arrange
        categoriaRepository.save(categoria1);
        categoriaRepository.save(categoria2);

        // Act
        List<Categoria> categorias = categoriaRepository.findAll();

        // Assert
        assertThat(categorias).hasSize(2);
        assertThat(categorias).extracting(Categoria::getNombre)
                .containsExactlyInAnyOrder("Hotelería", "Gastronomía");
    }

    @Test
    @DisplayName("Debe actualizar una categoría")
    void testUpdateCategoria() {
        // Arrange
        Categoria savedCategoria = categoriaRepository.save(categoria1);
        Long id = savedCategoria.getIdCategoria();

        // Act
        savedCategoria.setNombre("Hotelería Premium");
        savedCategoria.setDescripcion("Alojamientos de lujo");
        Categoria updatedCategoria = categoriaRepository.save(savedCategoria);

        // Assert
        Optional<Categoria> foundCategoria = categoriaRepository.findById(id);
        assertTrue(foundCategoria.isPresent());
        assertEquals("Hotelería Premium", foundCategoria.get().getNombre());
        assertEquals("Alojamientos de lujo", foundCategoria.get().getDescripcion());
    }

    @Test
    @DisplayName("Debe eliminar una categoría")
    void testDeleteCategoria() {
        // Arrange
        Categoria savedCategoria = categoriaRepository.save(categoria1);
        Long id = savedCategoria.getIdCategoria();

        // Act
        categoriaRepository.deleteById(id);

        // Assert
        Optional<Categoria> foundCategoria = categoriaRepository.findById(id);
        assertFalse(foundCategoria.isPresent());
    }

    @Test
    @DisplayName("Debe contar categorías correctamente")
    void testCount() {
        // Arrange
        categoriaRepository.save(categoria1);
        categoriaRepository.save(categoria2);

        // Act
        long count = categoriaRepository.count();

        // Assert
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Debe verificar si existe una categoría por ID")
    void testExistsById() {
        // Arrange
        Categoria savedCategoria = categoriaRepository.save(categoria1);

        // Act
        boolean exists = categoriaRepository.existsById(savedCategoria.getIdCategoria());
        boolean notExists = categoriaRepository.existsById(999L);

        // Assert
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    @DisplayName("Debe buscar múltiples categorías con término parcial")
    void testBuscarPorNombre_MultiplesResultados() {
        // Arrange
        Categoria cat3 = new Categoria();
        cat3.setNombre("Hotel Económico");
        cat3.setDescripcion("Hoteles baratos");

        Categoria cat4 = new Categoria();
        cat4.setNombre("Hotel de Lujo");
        cat4.setDescripcion("Hoteles premium");

        categoriaRepository.save(categoria1); // Hotelería
        categoriaRepository.save(cat3);
        categoriaRepository.save(cat4);

        // Act
        List<Categoria> result = categoriaRepository.buscarPorNombre("Hotel");

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result).extracting(Categoria::getNombre)
                .allMatch(nombre -> nombre.toLowerCase().contains("hotel"));
    }
}