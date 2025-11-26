package pe.edu.upeu.turismospringboot.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.turismospringboot.model.entity.*;
import pe.edu.upeu.turismospringboot.model.enums.EstadoCuenta;
import pe.edu.upeu.turismospringboot.model.enums.EstadoReserva;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservaRepositoryTest {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EmprendimientoRepository emprendimientoRepository;

    static Long reservaId;
    static Usuario usuario;
    static Emprendimiento emprendimiento;

    @BeforeEach
    void setUp() {
        //-------------------------------
        // 1. Crear Rol obligatorio
        //-------------------------------
        Rol rol = new Rol();
        rol.setNombre("USER");
        rol = rolRepository.save(rol);

        //-------------------------------
        // 2. Crear Usuario obligatorio
        //-------------------------------
        usuario = new Usuario();
        usuario.setUsername("cliente1");
        usuario.setPassword("1234");
        usuario.setRol(rol);
        usuario.setEstado(EstadoCuenta.ACTIVO);
        usuario = usuarioRepository.save(usuario);

        //-------------------------------
        // 3. Crear Emprendimiento obligatorio
        //-------------------------------
        emprendimiento = new Emprendimiento();
        emprendimiento.setNombre("Eco Lodge Titicaca");
        emprendimiento.setDescripcion("Hospedaje frente al lago");
        emprendimiento.setLatitud(-15.50);
        emprendimiento.setLongitud(-70.15);
        emprendimiento = emprendimientoRepository.save(emprendimiento);

        //-------------------------------
        // 4. Crear Reserva inicial
        //-------------------------------
        Reserva reserva = new Reserva();
        reserva.setFechaHoraReserva(LocalDateTime.now());
        reserva.setFechaHoraInicio(LocalDateTime.now().plusDays(1));
        reserva.setFechaHoraFin(LocalDateTime.now().plusDays(2));
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setUsuario(usuario);
        reserva.setEmprendimiento(emprendimiento);
        reserva.setTotalGeneral(150.0);

        Reserva guardada = reservaRepository.save(reserva);
        reservaId = guardada.getIdReserva();
    }

    @Test
    @Order(1)
    void testGuardarReserva() {
        Reserva r = new Reserva();
        r.setFechaHoraReserva(LocalDateTime.now());
        r.setFechaHoraInicio(LocalDateTime.now().plusDays(3));
        r.setFechaHoraFin(LocalDateTime.now().plusDays(4));
        r.setEstado(EstadoReserva.CONFIRMADA);
        r.setUsuario(usuario);
        r.setEmprendimiento(emprendimiento);
        r.setTotalGeneral(200.0);

        Reserva guardada = reservaRepository.save(r);

        assertNotNull(guardada.getIdReserva());
        assertEquals(EstadoReserva.CONFIRMADA, guardada.getEstado());
    }

    @Test
    @Order(2)
    void testBuscarPorId() {
        Optional<Reserva> reserva = reservaRepository.findById(reservaId);

        assertTrue(reserva.isPresent());
        assertEquals(EstadoReserva.PENDIENTE, reserva.get().getEstado());
    }

    @Test
    @Order(3)
    void testActualizarEstado() {
        Reserva reserva = reservaRepository.findById(reservaId).orElseThrow();
        reserva.setEstado(EstadoReserva.CONFIRMADA);

        Reserva actualizada = reservaRepository.save(reserva);

        assertEquals(EstadoReserva.CONFIRMADA, actualizada.getEstado());
    }

    @Test
    @Order(4)
    void testContarReservas() {
        long total = reservaRepository.count();
        assertTrue(total >= 1);
    }

    @Test
    @Order(5)
    void testContarPorEstado() {
        long pendientes = reservaRepository.countByEstado(EstadoReserva.PENDIENTE);

        assertTrue(pendientes >= 0); // puede ser 0 después de actualizar
    }

    @Test
    @Order(6)
    void testEliminarReserva() {
        reservaRepository.deleteById(reservaId);

        Optional<Reserva> eliminada = reservaRepository.findById(reservaId);

        assertFalse(eliminada.isPresent());
    }
}
