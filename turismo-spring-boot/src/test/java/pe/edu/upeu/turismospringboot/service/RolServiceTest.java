package pe.edu.upeu.turismospringboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.upeu.turismospringboot.model.dto.RolDto;
import pe.edu.upeu.turismospringboot.model.entity.Rol;
import pe.edu.upeu.turismospringboot.repository.RolRepository;
import pe.edu.upeu.turismospringboot.service.impl.RolServiceImpl;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    private Rol rol;
    private RolDto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombre("ADMIN");

        dto = new RolDto();
        dto.setNombre("NUEVO_ROL");
    }

    // ============================================================
    // listarRoles()
    // ============================================================
    @Test
    void testListarRoles() {
        when(rolRepository.findAll()).thenReturn(List.of(rol));

        List<Rol> result = rolService.listarRoles();

        assertEquals(1, result.size());
        verify(rolRepository).findAll();
    }

    // ============================================================
    // obtenerRolPorId()
    // ============================================================
    @Test
    void testObtenerRolPorId_Found() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        Rol result = rolService.obtenerRolPorId(1L);

        assertNotNull(result);
        assertEquals("ADMIN", result.getNombre());
    }

    @Test
    void testObtenerRolPorId_NotFound() {
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rolService.obtenerRolPorId(1L));

        assertTrue(ex.getMessage().contains("No se encontro"));
    }

    // ============================================================
    // guardarRol()
    // ============================================================
    @Test
    void testGuardarRol() {
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);

        Rol result = rolService.guardarRol(dto);

        assertNotNull(result);
        verify(rolRepository).save(any(Rol.class));
    }

    // ============================================================
    // actualizarRol()
    // ============================================================
    @Test
    void testActualizarRol_Found() {
        Rol existente = new Rol();
        existente.setIdRol(1L);
        existente.setNombre("ANtiguo");

        when(rolRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(rolRepository.save(any(Rol.class))).thenReturn(existente);

        Rol result = rolService.actualizarRol(1L, dto);

        assertEquals("NUEVO_ROL", result.getNombre());
        verify(rolRepository).save(existente);
    }

    @Test
    void testActualizarRol_NotFound() {
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rolService.actualizarRol(1L, dto));

        assertTrue(ex.getMessage().contains("No se encontro"));
    }

    // ============================================================
    // eliminarRolPorId()
    // ============================================================
    @Test
    void testEliminarRol() {
        doNothing().when(rolRepository).deleteById(1L);

        rolService.eliminarRolPorId(1L);

        verify(rolRepository).deleteById(1L);
    }

    // ============================================================
    // buscarRolesPorNombre()
    // ============================================================
    @Test
    void testBuscarRolesPorNombre() {
        when(rolRepository.buscarPorNombre("adm")).thenReturn(List.of(rol));

        List<Rol> result = rolService.buscarRolesPorNombre("adm");

        assertEquals(1, result.size());
        verify(rolRepository).buscarPorNombre("adm");
    }
}
