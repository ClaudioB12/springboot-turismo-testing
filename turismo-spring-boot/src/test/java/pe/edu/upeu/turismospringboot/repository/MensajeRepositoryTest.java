package pe.edu.upeu.turismospringboot.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.turismospringboot.model.entity.Mensaje;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.model.entity.Rol;
import pe.edu.upeu.turismospringboot.model.enums.EstadoMensaje;
import pe.edu.upeu.turismospringboot.model.enums.TipoMensaje;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class MensajeRepositoryTest {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    static Long mensajeId;
    static Usuario usuario1;
    static Usuario usuario2;

    @BeforeEach
    void setUp() {
        // -------------------------------
        // Crear un Rol obligatorio
        // -------------------------------
        Rol rol = new Rol();
        rol.setNombre("USER");
        rol = rolRepository.save(rol);

        // -------------------------------
        // Crear usuarios
        // -------------------------------
        usuario1 = new Usuario();
        usuario1.setUsername("pepe");
        usuario1.setPassword("1234");
        usuario1.setRol(rol);
        usuario1 = usuarioRepository.save(usuario1);

        usuario2 = new Usuario();
        usuario2.setUsername("maria");
        usuario2.setPassword("abcd");
        usuario2.setRol(rol);
        usuario2 = usuarioRepository.save(usuario2);

        // -------------------------------
        // Crear mensaje inicial
        // -------------------------------
        Mensaje msg = new Mensaje();
        msg.setEmisor(usuario1);
        msg.setReceptor(usuario2);
        msg.setContenidoTexto("Hola María");
        msg.setTipo(TipoMensaje.TEXTO);
        msg.setEstado(EstadoMensaje.ENVIADO);
        msg.setFechaEnvio(LocalDateTime.now());

        Mensaje guardado = mensajeRepository.save(msg);
        mensajeId = guardado.getId();
    }

    @Test
    @Order(1)
    void testGuardarMensaje() {
        Mensaje m = new Mensaje();
        m.setEmisor(usuario2);
        m.setReceptor(usuario1);
        m.setContenidoTexto("Respuesta");
        m.setTipo(TipoMensaje.TEXTO);
        m.setEstado(EstadoMensaje.ENTREGADO);
        m.setFechaEnvio(LocalDateTime.now());

        Mensaje guardado = mensajeRepository.save(m);

        assertNotNull(guardado.getId());
        assertEquals("Respuesta", guardado.getContenidoTexto());
    }

    @Test
    @Order(2)
    void testBuscarPorConversacion() {
        List<Mensaje> lista = mensajeRepository
                .findByEmisor_IdUsuarioAndReceptor_IdUsuarioOrEmisor_IdUsuarioAndReceptor_IdUsuarioOrderByFechaEnvioAsc(
                        usuario1.getIdUsuario(),
                        usuario2.getIdUsuario(),
                        usuario2.getIdUsuario(),
                        usuario1.getIdUsuario()
                );

        assertFalse(lista.isEmpty());
    }

    @Test
    @Order(3)
    void testMensajesRecientesPorUsuario() {
        List<Mensaje> lista = mensajeRepository.findMensajesRecientesPorUsuario(usuario1.getIdUsuario());

        assertFalse(lista.isEmpty());
        assertEquals(usuario1.getIdUsuario(), lista.get(0).getEmisor().getIdUsuario());
    }

    @Test
    @Order(4)
    void testBuscarPorUsernameYEstado() {
        List<Mensaje> lista = mensajeRepository.findAllByEmisor_UsernameAndReceptor_UsernameAndEstado(
                "pepe",
                "maria",
                EstadoMensaje.ENVIADO
        );

        assertFalse(lista.isEmpty());
        assertEquals("Hola María", lista.get(0).getContenidoTexto());
    }

    @Test
    @Order(5)
    void testBuscarPorUsernameYEstadoNot() {
        List<Mensaje> lista = mensajeRepository.findAllByEmisor_UsernameAndReceptor_UsernameAndEstadoNot(
                "pepe",
                "maria",
                EstadoMensaje.ERROR_ENVIO
        );

        assertFalse(lista.isEmpty());
    }

    @Test
    @Order(6)
    void testEliminarMensaje() {
        mensajeRepository.deleteById(mensajeId);

        boolean existe = mensajeRepository.findById(mensajeId).isPresent();

        assertFalse(existe);
    }
}
