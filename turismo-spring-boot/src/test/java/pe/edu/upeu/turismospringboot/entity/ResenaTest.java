package pe.edu.upeu.turismospringboot.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.model.entity.Resena;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ResenaTest {

    @Test
    void debeAsignarYLeerAtributosBasicos() {
        Resena resena = new Resena();

        resena.setIdResena(5L);
        resena.setComentario("Muy buen servicio");
        resena.setCalificacion(4);

        assertEquals(5L, resena.getIdResena());
        assertEquals("Muy buen servicio", resena.getComentario());
        assertEquals(4, resena.getCalificacion());
    }

    @Test
    void debeAsignarUsuarioCorrectamente() {
        Resena resena = new Resena();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(10L);
        usuario.setUsername("cliente01");

        resena.setUsuario(usuario);

        assertNotNull(resena.getUsuario());
        assertEquals(10L, resena.getUsuario().getIdUsuario());
        assertEquals("cliente01", resena.getUsuario().getUsername());
    }

    @Test
    void debeAsignarEmprendimientoCorrectamente() {
        Resena resena = new Resena();
        Emprendimiento em = new Emprendimiento();
        em.setIdEmprendimiento(33L);

        resena.setEmprendimiento(em);

        assertNotNull(resena.getEmprendimiento());
        assertEquals(33L, resena.getEmprendimiento().getIdEmprendimiento());
    }

    @Test
    void debeEjecutarPrePersist() {
        Resena resena = new Resena();
        resena.onCreate();

        assertNotNull(resena.getFechaCreacionResena());
        assertTrue(resena.getFechaCreacionResena().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void debeEjecutarPreUpdate() {
        Resena resena = new Resena();
        resena.onUpdate();

        assertNotNull(resena.getFechaModificacionResena());
        assertTrue(resena.getFechaModificacionResena().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void debeSerializarSinUsuarioPorJsonBackReference() throws Exception {
        Resena resena = new Resena();
        resena.setIdResena(50L);
        resena.setComentario("Excelente experiencia");
        resena.setCalificacion(5);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(99L);
        usuario.setUsername("testUser");
        resena.setUsuario(usuario); // NO debe aparecer por JsonBackReference

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(resena);

        assertTrue(json.contains("Excelente experiencia"));
        assertTrue(json.contains("calificacion"));
        assertTrue(json.contains("idResena"));

        // ✔ NO debe aparecer "usuario"
        assertFalse(json.contains("usuario"));
    }
}
