package pe.edu.upeu.turismospringboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.EmprendimientoDto;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.model.entity.FamiliaCategoria;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;
import pe.edu.upeu.turismospringboot.repository.EmprendimientoRepository;
import pe.edu.upeu.turismospringboot.repository.FamiliaCategoriaRepository;
import pe.edu.upeu.turismospringboot.repository.UsuarioRepository;
import pe.edu.upeu.turismospringboot.service.impl.EmprendimientoServiceImpl;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmprendimientoServiceTest {

    @Mock
    private EmprendimientoRepository emprendimientoRepository;

    @Mock
    private FamiliaCategoriaRepository familiaCategoriaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private EmprendimientoServiceImpl emprendimientoService;

    private EmprendimientoDto dto;
    private Emprendimiento emprendimiento;
    private FamiliaCategoria familiaCategoria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // DTO de prueba
        dto = new EmprendimientoDto();
        dto.setNombre("Eco Lodge");
        dto.setDescripcion("Turismo vivencial en Capachica");
        dto.setLatitud(-15.50);
        dto.setLongitud(-70.12);
        dto.setIdFamiliaCategoria(5L);

        // Entidades
        familiaCategoria = new FamiliaCategoria();
        familiaCategoria.setIdFamiliaCategoria(5L);

        emprendimiento = new Emprendimiento();
        emprendimiento.setIdEmprendimiento(1L);

        // Configurar file vacío
        when(file.isEmpty()).thenReturn(true);
    }

    // ============================================================
    // GET ALL
    // ============================================================

    @Test
    void testGetEmprendimientos() {
        when(emprendimientoRepository.findAll()).thenReturn(List.of(emprendimiento));

        List<Emprendimiento> result = emprendimientoService.getEmprendimientos();

        assertEquals(1, result.size());
        verify(emprendimientoRepository).findAll();
    }

    // ============================================================
    // GET POR ID
    // ============================================================

    @Test
    void testGetEmprendimientoById_Found() {
        when(emprendimientoRepository.findById(1L)).thenReturn(Optional.of(emprendimiento));

        Emprendimiento result = emprendimientoService.getEmprendimientoById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdEmprendimiento());
        verify(emprendimientoRepository).findById(1L);
    }

    @Test
    void testGetEmprendimientoById_NotFound() {
        when(emprendimientoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emprendimientoService.getEmprendimientoById(1L));

        assertTrue(ex.getMessage().contains("no encontrado"));
    }

    // ============================================================
    // POST
    // ============================================================

    @Test
    void testPostEmprendimiento() {
        when(familiaCategoriaRepository.findById(5L))
                .thenReturn(Optional.of(familiaCategoria));

        when(emprendimientoRepository.save(any(Emprendimiento.class)))
                .thenReturn(emprendimiento);

        Emprendimiento result = emprendimientoService.postEmprendimiento(dto, file);

        assertNotNull(result);
        verify(emprendimientoRepository).save(any(Emprendimiento.class));
    }

    // ============================================================
    // PUT
    // ============================================================

    @Test
    void testPutEmprendimiento_Found() {
        when(familiaCategoriaRepository.findById(5L))
                .thenReturn(Optional.of(familiaCategoria));

        when(emprendimientoRepository.findById(1L))
                .thenReturn(Optional.of(emprendimiento));

        when(emprendimientoRepository.save(any(Emprendimiento.class)))
                .thenReturn(emprendimiento);

        Emprendimiento result =
                emprendimientoService.putEmprendimiento(1L, dto, file);

        assertNotNull(result);
        verify(emprendimientoRepository).save(emprendimiento);
    }

    @Test
    void testPutEmprendimiento_NotFound() {
        when(familiaCategoriaRepository.findById(5L))
                .thenReturn(Optional.of(familiaCategoria));

        when(emprendimientoRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> emprendimientoService.putEmprendimiento(1L, dto, file));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Test
    void testDeleteEmprendimiento() {
        doNothing().when(emprendimientoRepository).deleteById(1L);

        emprendimientoService.deleteEmprendimiento(1L);

        verify(emprendimientoRepository).deleteById(1L);
    }

    // ============================================================
    // BUSCAR POR NOMBRE
    // ============================================================

    @Test
    void testBuscarPorNombre() {
        when(emprendimientoRepository.buscarPorNombre("eco"))
                .thenReturn(List.of(emprendimiento));

        List<Emprendimiento> result =
                emprendimientoService.buscarPorNombre("eco");

        assertEquals(1, result.size());
        verify(emprendimientoRepository).buscarPorNombre("eco");
    }

    // ============================================================
    // BUSCAR POR ID USUARIO
    // ============================================================

    @Test
    void testBuscarPorIdUsuario_Found() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(10L);
        usuario.setEmprendimiento(emprendimiento);

        when(usuarioRepository.findById(10L))
                .thenReturn(Optional.of(usuario));

        Emprendimiento result =
                emprendimientoService.buscarPorIdUsuario(10L);

        assertNotNull(result);
        assertEquals(emprendimiento, result);
    }

    @Test
    void testBuscarPorIdUsuario_NotFound() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> emprendimientoService.buscarPorIdUsuario(10L));
    }
}
