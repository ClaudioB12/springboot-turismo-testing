package pe.edu.upeu.turismospringboot.dto;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.UsuarioDTO;
import pe.edu.upeu.turismospringboot.model.entity.Persona;
import pe.edu.upeu.turismospringboot.model.entity.Rol;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDTOTest {

    @Test
    void debeCrearUsuarioDTODesdeEntidadUsuario() {
        // ===== Preparar ENTIDAD =====
        Persona persona = new Persona();
        persona.setNombres("Claudio");
        persona.setApellidos("Bustinza");

        Rol rol = new Rol();
        rol.setNombre("ADMIN");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(10L);
        usuario.setUsername("claudio");
        usuario.setPersona(persona);
        usuario.setRol(rol);

        // ===== Ejecutar =====
        UsuarioDTO dto = new UsuarioDTO(usuario);

        // ===== Verificar =====
        assertEquals(10L, dto.getIdUsuario());
        assertEquals("claudio", dto.getUsername());
        assertEquals("Claudio Bustinza", dto.getNombrePersona());
        assertEquals("ADMIN", dto.getRolNombre());
    }
}
