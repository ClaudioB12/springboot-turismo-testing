package pe.edu.upeu.turismospringboot.controller.admin;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upeu.turismospringboot.model.dto.RolDto;
import pe.edu.upeu.turismospringboot.model.entity.Rol;
import pe.edu.upeu.turismospringboot.service.RolService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolControllerTest {

    @InjectMocks
    private RolController rolController;

    @Mock
    private RolService rolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -----------------------------------------------------------
    // LISTAR ROLES
    // -----------------------------------------------------------
    @Test
    void testListarRoles() {
        List<Rol> roles = List.of(new Rol(), new Rol());
        when(rolService.listarRoles()).thenReturn(roles);

        ResponseEntity<List<Rol>> response = rolController.listarRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roles, response.getBody());
    }

    // -----------------------------------------------------------
    // BUSCAR POR ID
    // -----------------------------------------------------------
    @Test
    void testFindById() {
        Rol rol = new Rol();
        rol.setIdRol(1L);

        when(rolService.obtenerRolPorId(1L)).thenReturn(rol);

        ResponseEntity<Rol> response = rolController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rol, response.getBody());
    }

    // -----------------------------------------------------------
    // GUARDAR ROL
    // -----------------------------------------------------------
    @Test
    void testGuardarRol() {
        RolDto dto = new RolDto();
        dto.setNombre("ADMIN");

        Rol rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombre("ADMIN");

        when(rolService.guardarRol(dto)).thenReturn(rol);

        ResponseEntity<Rol> response = rolController.guardarRol(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rol, response.getBody());
    }

    // -----------------------------------------------------------
    // ACTUALIZAR ROL
    // -----------------------------------------------------------
    @Test
    void testActualizarRol() {
        RolDto dto = new RolDto();
        dto.setNombre("GESTOR");

        Rol rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombre("GESTOR");

        when(rolService.actualizarRol(1L, dto)).thenReturn(rol);

        ResponseEntity<Rol> response = rolController.actualizarRol(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rol, response.getBody());
    }

    // -----------------------------------------------------------
    // ELIMINAR ROL EXITOSO
    // -----------------------------------------------------------
    @Test
    void testEliminarRol() {
        ResponseEntity<String> response = rolController.eliminarRol(1L);

        verify(rolService).eliminarRolPorId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rol eliminado exitosamente", response.getBody());
    }

    // -----------------------------------------------------------
    // ELIMINAR ROL NO ENCONTRADO
    // -----------------------------------------------------------
    @Test
    void testEliminarRol_NotFound() {
        doThrow(new EntityNotFoundException("No existe"))
                .when(rolService).eliminarRolPorId(1L);

        ResponseEntity<String> response = rolController.eliminarRol(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Rol no encontrado", response.getBody());
    }

    // -----------------------------------------------------------
    // ERROR GENERAL AL ELIMINAR
    // -----------------------------------------------------------
    @Test
    void testEliminarRol_InternalError() {
        doThrow(new RuntimeException("Error inesperado"))
                .when(rolService).eliminarRolPorId(1L);

        ResponseEntity<String> response = rolController.eliminarRol(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al eliminar el rol", response.getBody());
    }

    // -----------------------------------------------------------
    // BUSCAR POR NOMBRE
    // -----------------------------------------------------------
    @Test
    void testBuscarPorNombre() {
        List<Rol> roles = List.of(new Rol());
        when(rolService.buscarRolesPorNombre("admin")).thenReturn(roles);

        ResponseEntity<List<Rol>> response = rolController.buscarPorNombre("admin");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roles, response.getBody());
    }
}
