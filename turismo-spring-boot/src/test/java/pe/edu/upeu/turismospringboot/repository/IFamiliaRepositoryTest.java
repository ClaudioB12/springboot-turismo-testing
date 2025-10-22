package pe.edu.upeu.turismospringboot.repository;



import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class IFamiliaRepositoryTest {


    @Autowired
    private FamiliaRepository familiaRepository;


    @Autowired
    private LugarRepository lugarRepository;


    private static Long familiaId;


    @BeforeEach
    public void setUp() {
        // 🔹 Primero creamos y guardamos un lugar
        Lugar lugar = new Lugar();
        lugar.setNombre("Cusco");
        lugar.setDescripcion("Ciudad turística del Perú");
        lugar.setPais("Perú");
        lugar = lugarRepository.save(lugar);


        // 🔹 Luego creamos la familia asociada a ese lugar
        Familia familia = new Familia();
        familia.setNombre("Aventura");
        familia.setLugar(lugar); // 🔸 Asignar el lugar obligatorio
        Familia guardada = familiaRepository.save(familia);


        familiaId = guardada.getIdFamilia();
    }


    @Test
    @Order(1)
    public void testGuardarFamilia() {
        Lugar lugar = lugarRepository.findAll().get(0); // Reutiliza el lugar creado


        Familia nuevaFamilia = new Familia();
        nuevaFamilia.setNombre("Cultura");
        nuevaFamilia.setLugar(lugar); // 🔸 Asignar lugar


        Familia guardada = familiaRepository.save(nuevaFamilia);


        assertNotNull(guardada.getIdFamilia());
        assertEquals("Cultura", guardada.getNombre());
    }


    @Test
    @Order(2)
    public void testBuscarPorId() {
        Optional<Familia> familia = familiaRepository.findById(familiaId);
        assertTrue(familia.isPresent());
        assertEquals("Aventura", familia.get().getNombre());
    }


    @Test
    @Order(3)
    public void testActualizarFamilia() {
        Familia familia = familiaRepository.findById(familiaId).orElseThrow();
        familia.setNombre("Aventura Extrema");
        Familia actualizada = familiaRepository.save(familia);


        assertEquals("Aventura Extrema", actualizada.getNombre());
    }


    @Test
    @Order(4)
    public void testListarFamilias() {
        List<Familia> familias = familiaRepository.findAll();
        assertFalse(familias.isEmpty());
        familias.forEach(f -> System.out.println(f.getIdFamilia() + " - " + f.getNombre()));
    }


    @Test
    @Order(5)
    public void testBuscarPorNombreExacto() {
        Optional<Familia> familia = familiaRepository.findByNombre("Aventura");
        assertTrue(familia.isPresent());
        assertEquals("Aventura", familia.get().getNombre());
    }


    @Test
    @Order(6)
    public void testBuscarPorNombreParcial() {
        List<Familia> familias = familiaRepository.buscarPorNombre("vent");
        assertFalse(familias.isEmpty());
        assertTrue(familias.stream().anyMatch(f -> f.getNombre().contains("Aventura")));
    }


    @Test
    @Order(7)
    public void testEliminarFamilia() {
        familiaRepository.deleteById(familiaId);
        Optional<Familia> eliminada = familiaRepository.findById(familiaId);
        assertFalse(eliminada.isPresent());
    }
}
