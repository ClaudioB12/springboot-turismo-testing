package pe.edu.upeu.turismospringboot.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.FamiliaDto;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;
import pe.edu.upeu.turismospringboot.repository.FamiliaRepository;
import pe.edu.upeu.turismospringboot.repository.LugarRepository;
import pe.edu.upeu.turismospringboot.util.ArchivoUtil;


import java.io.File;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class FamiliaServiceImplTest {


    @Mock
    private FamiliaRepository familiaRepository;


    @Mock
    private LugarRepository lugarRepository;


    @Mock
    private MultipartFile file;


    @InjectMocks
    private FamiliaServiceImpl familiaService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetFamilias() {
        Familia f1 = new Familia(); f1.setNombre("Aventura");
        Familia f2 = new Familia(); f2.setNombre("Cultural");
        when(familiaRepository.findAll()).thenReturn(List.of(f1, f2));


        List<Familia> result = familiaService.getFamilias();


        assertEquals(2, result.size());
        verify(familiaRepository, times(1)).findAll();
    }


    @Test
    void testGetFamiliaById_Found() {
        Familia familia = new Familia();
        familia.setIdFamilia(1L);
        familia.setNombre("Familiar");


        when(familiaRepository.findById(1L)).thenReturn(Optional.of(familia));


        Familia result = familiaService.getFamiliaById(1L);


        assertNotNull(result);
        assertEquals("Familiar", result.getNombre());
        verify(familiaRepository).findById(1L);
    }


    @Test
    void testGetFamiliaById_NotFound() {
        when(familiaRepository.findById(anyLong())).thenReturn(Optional.empty());


        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                familiaService.getFamiliaById(10L)
        );


        assertTrue(ex.getMessage().contains("no enctrada"));
    }


    @Test
    void testPostFamilia_WithFile() throws Exception {
        FamiliaDto dto = new FamiliaDto();
        dto.setNombre("Ecoturismo");
        dto.setDescripcion("Naturaleza y aventura");
        dto.setNombreLugar("Selva");


        Lugar lugar = new Lugar();
        lugar.setNombre("Selva");


        when(lugarRepository.findByNombre("Selva")).thenReturn(Optional.of(lugar));
        when(file.isEmpty()).thenReturn(false);


        // ✅ Evita el error de "CannotStubVoidMethodWithReturnValue"
        doNothing().when(file).transferTo(any(File.class));


        // Mock del método utilitario que guarda el archivo
        mockStatic(ArchivoUtil.class);
        when(ArchivoUtil.saveFile(file)).thenReturn("imagen.jpg");


        Familia saved = new Familia();
        saved.setIdFamilia(1L);
        saved.setNombre("Ecoturismo");
        when(familiaRepository.save(any(Familia.class))).thenReturn(saved);


        Familia result = familiaService.postFamilia(dto, file);


        assertEquals("Ecoturismo", result.getNombre());
        verify(familiaRepository).save(any(Familia.class));
    }


    @Test
    void testPutFamilia_UpdatesSuccessfully() {
        Familia existing = new Familia();
        existing.setIdFamilia(1L);
        existing.setNombre("Antiguo");


        Lugar lugar = new Lugar();
        lugar.setNombre("Selva");


        FamiliaDto dto = new FamiliaDto();
        dto.setNombre("Actualizado");
        dto.setDescripcion("Nueva descripción");
        dto.setNombreLugar("Selva");


        when(familiaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(lugarRepository.findByNombre("Selva")).thenReturn(Optional.of(lugar));
        when(file.isEmpty()).thenReturn(true);
        when(familiaRepository.save(any(Familia.class))).thenReturn(existing);


        Familia result = familiaService.putFamilia(1L, dto, file);


        assertEquals("Actualizado", result.getNombre());
        verify(familiaRepository).save(any(Familia.class));
    }


    @Test
    void testDeleteFamilia() {
        doNothing().when(familiaRepository).deleteById(1L);


        familiaService.deleteFamilia(1L);


        verify(familiaRepository).deleteById(1L);
    }


    @Test
    void testBuscarPorNombre() {
        Familia familia = new Familia();
        familia.setNombre("Aventura");
        when(familiaRepository.buscarPorNombre("Aventura")).thenReturn(List.of(familia));


        List<Familia> result = familiaService.buscarPorNombre("Aventura");


        assertEquals(1, result.size());
        assertEquals("Aventura", result.get(0).getNombre());
        verify(familiaRepository).buscarPorNombre("Aventura");
    }
}
