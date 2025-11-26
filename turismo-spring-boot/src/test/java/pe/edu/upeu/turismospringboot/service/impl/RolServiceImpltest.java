package pe.edu.upeu.turismospringboot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.upeu.turismospringboot.model.dto.RolDto;
import pe.edu.upeu.turismospringboot.model.entity.Rol;
import pe.edu.upeu.turismospringboot.repository.RolRepository;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolServiceImpltest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    private Rol rolBase;
    private RolDto rolDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rolBase = new Rol();
        rolBase.setIdRol(1L);
        rolBase.setNombre("ADMIN");

        rolDto = new RolDto();
        rolDto.setNombre("USER");
    }

    // ============================================================
    // LISTAR
    // ============================================================

    @Test
    @DisplayName("Debe listar todos los roles")
    void testListarRoles() {
        when(rolRepository.findAll()).thenReturn(List.of(rolBase));

        List<Rol> lista = rolService.listarRoles();

        assertEquals(1, lista.size());
        verify(rolRepository, times(1)).findAll();
    }

    // ============================================================
    // OBTENER POR ID
    // ============================================================

    @Test
    @DisplayName("Debe obtener rol por ID")
    void testObtenerRolPorId() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolBase));

        Rol encontrado = rolService.obtenerRolPorId(1L);

        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getIdRol());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el rol no existe")
    void testObtenerRolPorId_NoExiste() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> rolService.obtenerRolPorId(99L));
    }

    // ============================================================
    // GUARDAR
    // ============================================================

    @Test
    @DisplayName("Debe guardar un nuevo rol")
    void testGuardarRol() {
        Rol nuevo = new Rol();
        nuevo.setIdRol(2L);
        nuevo.setNombre("USER");

        when(rolRepository.save(any(Rol.class))).thenReturn(nuevo);

        Rol creado = rolService.guardarRol(rolDto);

        assertNotNull(creado);
        assertEquals("USER", creado.getNombre());
    }

    // ============================================================
    // ACTUALIZAR
    // ============================================================

    @Test
    @DisplayName("Debe actualizar un rol")
    void testActualizarRol() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolBase));

        Rol actualizado = new Rol();
        actualizado.setIdRol(1L);
        actualizado.setNombre("USER");

        when(rolRepository.save(any(Rol.class))).thenReturn(actualizado);

        Rol result = rolService.actualizarRol(1L, rolDto);

        assertEquals("USER", result.getNombre());
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar rol no existente")
    void testActualizarRol_NoExiste() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> rolService.actualizarRol(99L, rolDto));
    }

    // ============================================================
    // ELIMINAR
    // ============================================================

    @Test
    @DisplayName("Debe eliminar un rol por ID")
    void testEliminarRol() {
        rolService.eliminarRolPorId(1L);

        verify(rolRepository, times(1)).deleteById(1L);
    }

    // ============================================================
    // BUSCAR POR NOMBRE
    // ============================================================

    @Test
    @DisplayName("Debe buscar roles por nombre")
    void testBuscarRolesPorNombre() {
        when(rolRepository.buscarPorNombre("adm")).thenReturn(List.of(rolBase));

        List<Rol> lista = rolService.buscarRolesPorNombre("adm");

        assertFalse(lista.isEmpty());
        assertEquals("ADMIN", lista.get(0).getNombre());
    }
}
