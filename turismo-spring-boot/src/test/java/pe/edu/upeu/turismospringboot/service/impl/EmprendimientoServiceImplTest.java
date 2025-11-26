package pe.edu.upeu.turismospringboot.service.impl;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.EmprendimientoDto;
import pe.edu.upeu.turismospringboot.model.entity.Emprendimiento;
import pe.edu.upeu.turismospringboot.model.entity.FamiliaCategoria;
import pe.edu.upeu.turismospringboot.model.entity.Usuario;

import pe.edu.upeu.turismospringboot.repository.EmprendimientoRepository;
import pe.edu.upeu.turismospringboot.repository.FamiliaCategoriaRepository;
import pe.edu.upeu.turismospringboot.repository.UsuarioRepository;
import pe.edu.upeu.turismospringboot.util.ArchivoUtil;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmprendimientoServiceImplTest {

    @Mock
    private EmprendimientoRepository emprendimientoRepository;

    @Mock
    private FamiliaCategoriaRepository familiaCategoriaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private EmprendimientoServiceImpl service;

    private EmprendimientoDto dto;
    private FamiliaCategoria familiaCategoria;
    private Emprendimiento emprendimiento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new EmprendimientoDto();
        dto.setNombre("Eco Lodge");
        dto.setDescripcion("Hospedaje rural");
        dto.setLatitud(-15.50);
        dto.setLongitud(-70.15);
        dto.setIdFamiliaCategoria(1L);

        familiaCategoria = new FamiliaCategoria();
        familiaCategoria.setIdFamiliaCategoria(1L);

        emprendimiento = new Emprendimiento();
        emprendimiento.setIdEmprendimiento(10L);
        emprendimiento.setNombre("Antiguo");
    }

    /* ============================================================
       GET ALL
       ============================================================ */
    @Test
    void testGetEmprendimientos() {
        when(emprendimientoRepository.findAll())
                .thenReturn(List.of(new Emprendimiento()));

        List<Emprendimiento> lista = service.getEmprendimientos();

        assertFalse(lista.isEmpty());
        verify(emprendimientoRepository, times(1)).findAll();
    }

    /* ============================================================
       GET BY ID
       ============================================================ */
    @Test
    void testGetEmprendimientoById() {
        when(emprendimientoRepository.findById(10L))
                .thenReturn(Optional.of(emprendimiento));

        Emprendimiento encontrado = service.getEmprendimientoById(10L);

        assertNotNull(encontrado);
        assertEquals(10L, encontrado.getIdEmprendimiento());
    }

    @Test
    void testGetEmprendimientoByIdNoExiste() {
        when(emprendimientoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getEmprendimientoById(99L));
    }

    /* ============================================================
       POST (con imagen)
       ============================================================ */

    /* ============================================================
       POST (sin imagen)
       ============================================================ */
    @Test
    void testPostEmprendimientoSinImagen() {
        when(familiaCategoriaRepository.findById(1L))
                .thenReturn(Optional.of(familiaCategoria));

        when(file.isEmpty()).thenReturn(true); // no se guarda imagen

        when(emprendimientoRepository.save(any(Emprendimiento.class)))
                .thenAnswer(inv -> {
                    Emprendimiento e = inv.getArgument(0);
                    e.setIdEmprendimiento(2L);
                    return e;
                });

        Emprendimiento creado = service.postEmprendimiento(dto, file);

        assertNotNull(creado.getIdEmprendimiento());
        assertNull(creado.getImagenUrl());
    }

    /* ============================================================
       PUT (actualización)
       ============================================================ */
    @Test
    void testPutEmprendimiento() {
        when(familiaCategoriaRepository.findById(1L))
                .thenReturn(Optional.of(familiaCategoria));

        when(emprendimientoRepository.findById(10L))
                .thenReturn(Optional.of(emprendimiento));

        when(file.isEmpty()).thenReturn(true);

        dto.setNombre("Nuevo Nombre");

        when(emprendimientoRepository.save(any(Emprendimiento.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Emprendimiento actualizado =
                service.putEmprendimiento(10L, dto, file);

        assertEquals("Nuevo Nombre", actualizado.getNombre());
    }

    @Test
    void testPutEmprendimientoNoExiste() {
        when(familiaCategoriaRepository.findById(1L))
                .thenReturn(Optional.of(familiaCategoria));

        when(emprendimientoRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.putEmprendimiento(999L, dto, file));
    }

    /* ============================================================
       DELETE
       ============================================================ */
    @Test
    void testDeleteEmprendimiento() {
        service.deleteEmprendimiento(10L);
        verify(emprendimientoRepository, times(1)).deleteById(10L);
    }

    /* ============================================================
       BUSCAR POR NOMBRE
       ============================================================ */
    @Test
    void testBuscarPorNombre() {
        when(emprendimientoRepository.buscarPorNombre("eco"))
                .thenReturn(List.of(new Emprendimiento()));

        List<Emprendimiento> lista = service.buscarPorNombre("eco");

        assertFalse(lista.isEmpty());
    }

    /* ============================================================
       BUSCAR POR ID DE USUARIO
       ============================================================ */
    @Test
    void testBuscarPorIdUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmprendimiento(emprendimiento);

        when(usuarioRepository.findById(20L))
                .thenReturn(Optional.of(usuario));

        Emprendimiento encontrado = service.buscarPorIdUsuario(20L);

        assertNotNull(encontrado);
        assertEquals(emprendimiento, encontrado);
    }

    @Test
    void testBuscarPorIdUsuarioNoExiste() {
        when(usuarioRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.buscarPorIdUsuario(100L));
    }
}
