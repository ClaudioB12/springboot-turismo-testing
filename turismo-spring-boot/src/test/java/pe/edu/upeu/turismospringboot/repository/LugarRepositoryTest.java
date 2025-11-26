package pe.edu.upeu.turismospringboot.repository;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class LugarRepositoryTest {

    @Autowired
    private LugarRepository lugarRepository;

    private static Long lugarId;

    @BeforeEach
    public void setUp() {
        // Crear un lugar inicial para pruebas
        Lugar lugar = new Lugar();
        lugar.setNombre("Capachica");
        lugar.setDescripcion("Zona turística del lago Titicaca");
        lugar.setCiudad("Puno");
        lugar.setProvincia("Puno");
        lugar.setPais("Perú");
        lugar.setLatitud(-15.50);
        lugar.setLongitud(-70.15);
        lugar.setImagenUrl("imagen1.jpg");

        Lugar guardado = lugarRepository.save(lugar);
        lugarId = guardado.getIdLugar();
    }

    @Test
    @Order(1)
    public void testGuardarLugar() {
        Lugar nuevo = new Lugar();
        nuevo.setNombre("Chifrón");
        nuevo.setDescripcion("Playa turística");
        nuevo.setCiudad("Capachica");
        nuevo.setProvincia("Puno");
        nuevo.setPais("Perú");
        nuevo.setLatitud(-15.51);
        nuevo.setLongitud(-70.17);
        nuevo.setImagenUrl("playa.jpg");

        Lugar guardado = lugarRepository.save(nuevo);

        assertNotNull(guardado.getIdLugar());
        assertEquals("Chifrón", guardado.getNombre());
    }

    @Test
    @Order(2)
    public void testBuscarPorId() {
        Optional<Lugar> lugar = lugarRepository.findById(lugarId);

        assertTrue(lugar.isPresent());
        assertEquals("Capachica", lugar.get().getNombre());
    }

    @Test
    @Order(3)
    public void testActualizarLugar() {
        Lugar lugar = lugarRepository.findById(lugarId).orElseThrow();
        lugar.setNombre("Capachica Actualizado");

        Lugar actualizado = lugarRepository.save(lugar);

        assertEquals("Capachica Actualizado", actualizado.getNombre());
    }

    @Test
    @Order(4)
    public void testListarLugares() {
        List<Lugar> lugares = lugarRepository.findAll();

        assertFalse(lugares.isEmpty());
        lugares.forEach(l -> System.out.println(l.getIdLugar() + " - " + l.getNombre()));
    }

    @Test
    @Order(5)
    public void testBuscarPorNombreExacto() {
        Optional<Lugar> lugar = lugarRepository.findByNombre("Capachica");

        assertTrue(lugar.isPresent());
        assertEquals("Capachica", lugar.get().getNombre());
    }

    @Test
    @Order(6)
    public void testBuscarPorNombreParcial() {
        List<Lugar> lugares = lugarRepository.buscarPorNombre("capa");

        assertFalse(lugares.isEmpty());
        assertTrue(
                lugares.stream().anyMatch(l -> l.getNombre().toLowerCase().contains("capa"))
        );
    }

    @Test
    @Order(7)
    public void testBuscarCaseInsensitive() {
        List<Lugar> lugares = lugarRepository.buscarPorNombre("capachica");

        assertFalse(lugares.isEmpty());
    }

    @Test
    @Order(8)
    public void testEliminarLugar() {
        lugarRepository.deleteById(lugarId);

        Optional<Lugar> eliminado = lugarRepository.findById(lugarId);

        assertFalse(eliminado.isPresent());
    }
}