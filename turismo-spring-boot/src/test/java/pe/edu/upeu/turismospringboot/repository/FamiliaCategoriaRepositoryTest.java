package pe.edu.upeu.turismospringboot.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.FamiliaCategoria;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;
import pe.edu.upeu.turismospringboot.repository.CategoriaRepository;
import pe.edu.upeu.turismospringboot.repository.FamiliaCategoriaRepository;
import pe.edu.upeu.turismospringboot.repository.FamiliaRepository;
import pe.edu.upeu.turismospringboot.repository.LugarRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class FamiliaCategoriaRepositoryTest {

    @Autowired
    private FamiliaCategoriaRepository familiaCategoriaRepository;

    @Autowired
    private FamiliaRepository familiaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LugarRepository lugarRepository;

    private static Long familiaId;
    private static Long categoriaId;
    private static Long fcId;

    @BeforeEach
    void setUp() {
        if (familiaCategoriaRepository.count() == 0) {

            // 🔹 Crear lugar obligatorio
            Lugar lugar = new Lugar();
            lugar.setNombre("Cusco");
            lugar.setDescripcion("Ciudad turística");
            lugar.setPais("Perú");
            lugarRepository.save(lugar);

            // 🔹 Crear familia (requiere lugar obligatorio)
            Familia familia = new Familia();
            familia.setNombre("Aventura");
            familia.setDescripcion("Familia mítica");
            familia.setLugar(lugar); // ✔ obligatorio
            familiaRepository.save(familia);
            familiaId = familia.getIdFamilia();

            // 🔹 Crear categoría
            Categoria categoria = new Categoria();
            categoria.setNombre("Turismo");
            categoriaRepository.save(categoria);
            categoriaId = categoria.getIdCategoria();

            // 🔹 Crear relación familia–categoría
            FamiliaCategoria fc = new FamiliaCategoria();
            fc.setFamilia(familia);
            fc.setCategoria(categoria);
            familiaCategoriaRepository.save(fc);

            fcId = fc.getIdFamiliaCategoria();
        }
    }

    @Test
    @Order(1)
    void testGuardarFamiliaCategoria() {
        // Crear un lugar real
        Lugar lugar = new Lugar();
        lugar.setNombre("Arequipa");
        lugar.setDescripcion("Ciudad blanca");
        lugar.setPais("Perú");
        lugarRepository.save(lugar);

        // Crear familia válida
        Familia fam = new Familia();
        fam.setNombre("Cultura");
        fam.setDescripcion("Familia cultural");
        fam.setLugar(lugar);
        familiaRepository.save(fam);

        // Crear categoría válida
        Categoria cat = new Categoria();
        cat.setNombre("Gastronomía");
        categoriaRepository.save(cat);

        // Crear relación
        FamiliaCategoria fc = new FamiliaCategoria();
        fc.setFamilia(fam);
        fc.setCategoria(cat);

        FamiliaCategoria guardado = familiaCategoriaRepository.save(fc);

        assertNotNull(guardado.getIdFamiliaCategoria());
        assertEquals("Cultura", guardado.getFamilia().getNombre());
        assertEquals("Gastronomía", guardado.getCategoria().getNombre());
    }


    @Test
    @Order(2)
    void testFindAll() {
        List<FamiliaCategoria> lista = familiaCategoriaRepository.findAll();
        assertFalse(lista.isEmpty());
    }


}
