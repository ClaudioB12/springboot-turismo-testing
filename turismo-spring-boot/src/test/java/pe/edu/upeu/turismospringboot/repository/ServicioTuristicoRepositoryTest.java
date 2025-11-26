package pe.edu.upeu.turismospringboot.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.model.entity.ServicioTuristico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServicioTuristicoRepositoryTest {

    @Autowired
    private ServicioTuristicoRepository servicioRepository;

    @Autowired
    private EmprendimientoRepository emprendimientoRepository;

    static Long servicioId;
    static Emprendimiento emprendimiento;

    @BeforeEach
    void setUp() {
        // --------------------------------
        // 1. Crear Emprendimiento requerido
        // --------------------------------
        emprendimiento = new Emprendimiento();
        emprendimiento.setNombre("Agencia Titicaca Tours");
        emprendimiento.setDescripcion("Servicios turísticos en Capachica");

        emprendimiento.setLatitud(-15.50);
        emprendimiento.setLongitud(-70.15);
        emprendimiento = emprendimientoRepository.save(emprendimiento);

        // --------------------------------
        // 2. Crear servicio base para tests
        // --------------------------------
        ServicioTuristico st = new ServicioTuristico();
        st.setNombre("Tour en Kayak");
        st.setDescripcion("Recorrido guiado por el lago");
        st.setPrecioUnitario(80.0);
        st.setTipoServicio("TOUR");
        st.setImagenUrl("kayak.png");
        st.setEmprendimiento(emprendimiento);
        st.setFechaCreacion(LocalDateTime.now());

        ServicioTuristico guardado = servicioRepository.save(st);
        servicioId = guardado.getIdServicio();
    }

    @Test
    @Order(1)
    void testGuardarServicio() {
        ServicioTuristico st = new ServicioTuristico();
        st.setNombre("Almuerzo Local");
        st.setDescripcion("Plato típico de la zona");
        st.setPrecioUnitario(25.5);
        st.setTipoServicio("ALIMENTACION");
        st.setImagenUrl("menu.png");
        st.setEmprendimiento(emprendimiento);
        st.setFechaCreacion(LocalDateTime.now());

        ServicioTuristico guardado = servicioRepository.save(st);

        assertNotNull(guardado.getIdServicio());
        assertEquals("Almuerzo Local", guardado.getNombre());
    }

    @Test
    @Order(2)
    void testBuscarPorId() {
        Optional<ServicioTuristico> servicio = servicioRepository.findById(servicioId);

        assertTrue(servicio.isPresent());
        assertEquals("Tour en Kayak", servicio.get().getNombre());
    }

    @Test
    @Order(3)
    void testActualizarServicio() {
        ServicioTuristico st = servicioRepository.findById(servicioId).orElseThrow();
        st.setNombre("Tour en Kayak Premium");

        ServicioTuristico actualizado = servicioRepository.save(st);

        assertEquals("Tour en Kayak Premium", actualizado.getNombre());
    }

    @Test
    @Order(4)
    void testListarServicios() {
        List<ServicioTuristico> lista = servicioRepository.findAll();

        assertFalse(lista.isEmpty());
    }

    @Test
    @Order(5)
    void testBuscarPorNombreParcial() {
        List<ServicioTuristico> lista = servicioRepository.buscarPorNombre("kay");

        assertFalse(lista.isEmpty());
        assertTrue(lista.stream().anyMatch(s -> s.getNombre().toLowerCase().contains("kay")));
    }

    @Test
    @Order(6)
    void testBuscarPorNombreCaseInsensitive() {
        List<ServicioTuristico> lista = servicioRepository.buscarPorNombre("kayAK");

        assertFalse(lista.isEmpty());
    }

    @Test
    @Order(7)
    void testEliminarServicio() {
        servicioRepository.deleteById(servicioId);

        Optional<ServicioTuristico> eliminado = servicioRepository.findById(servicioId);

        assertFalse(eliminado.isPresent());
    }
}
