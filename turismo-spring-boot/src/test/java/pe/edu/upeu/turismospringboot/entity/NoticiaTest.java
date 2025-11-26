package pe.edu.upeu.turismospringboot.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Noticia;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NoticiaTest {

    @Test
    void debeAsignarYLeerAtributosBasicos() {
        Noticia noticia = new Noticia();

        noticia.setIdNoticia(1L);
        noticia.setTitulo("Nuevo atractivo turístico");
        noticia.setContenido("Contenido de la noticia...");
        noticia.setImagenUrl("imagen.jpg");
        noticia.setFechaPublicacion(LocalDateTime.of(2024, 10, 10, 12, 0));

        assertEquals(1L, noticia.getIdNoticia());
        assertEquals("Nuevo atractivo turístico", noticia.getTitulo());
        assertEquals("Contenido de la noticia...", noticia.getContenido());
        assertEquals("imagen.jpg", noticia.getImagenUrl());
        assertEquals(LocalDateTime.of(2024, 10, 10, 12, 0), noticia.getFechaPublicacion());
    }

    @Test
    void debeAsignarAutorCorrectamente() {
        Noticia noticia = new Noticia();
        Usuario autor = new Usuario();
        autor.setIdUsuario(99L);
        autor.setUsername("admin123");

        noticia.setAutor(autor);

        assertNotNull(noticia.getAutor());
        assertEquals(99L, noticia.getAutor().getIdUsuario());
        assertEquals("admin123", noticia.getAutor().getUsername());
    }

    @Test
    void debeEjecutarPrePersist() {
        Noticia noticia = new Noticia();
        noticia.onCreate();

        assertNotNull(noticia.getFechaCreacionNoticia());
        assertTrue(noticia.getFechaCreacionNoticia().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void debeEjecutarPreUpdate() {
        Noticia noticia = new Noticia();
        noticia.onUpdate();

        assertNotNull(noticia.getFechaModificacionNoticia());
        assertTrue(noticia.getFechaModificacionNoticia().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void debeSerializarCorrectamenteSinAutorPorJsonBackReference() throws Exception {
        Noticia noticia = new Noticia();
        noticia.setIdNoticia(1L);
        noticia.setTitulo("Test noticia");

        Usuario autor = new Usuario();
        autor.setIdUsuario(10L);
        noticia.setAutor(autor); // Debe NO aparecer en JSON por JsonBackReference

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(noticia);

        assertTrue(json.contains("Test noticia")); // serializa titulo
        assertFalse(json.contains("autor"));       // JsonBackReference evita serialización
        assertTrue(json.contains("idNoticia"));    // OK
    }
}
