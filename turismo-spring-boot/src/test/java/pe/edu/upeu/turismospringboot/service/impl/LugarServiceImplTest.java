package pe.edu.upeu.turismospringboot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.LugarDto;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;
import pe.edu.upeu.turismospringboot.repository.LugarRepository;
import pe.edu.upeu.turismospringboot.util.ArchivoUtil;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LugarServiceImplTest {

    @InjectMocks
    private LugarServiceImpl lugarService;

    @Mock
    private LugarRepository lugarRepository;

    @Mock
    private ArchivoUtil archivoUtil;

    private Lugar lugar;
    private LugarDto lugarDto;

    @BeforeEach
    void setUp() {
        // Set up mock data for LugarDto
        lugarDto = new LugarDto();
        lugarDto.setNombre("Lago Titicaca");
        lugarDto.setDescripcion("Hermoso lago en los Andes");
        lugarDto.setDireccion("Capachica");
        lugarDto.setCiudad("Puno");
        lugarDto.setProvincia("Puno");
        lugarDto.setPais("Perú");
        lugarDto.setLatitud(-15.7653);
        lugarDto.setLongitud(-69.5321);

        // Set up mock Lugar entity
        lugar = new Lugar();
        lugar.setIdLugar(1L);
        lugar.setNombre("Lago Titicaca");
        lugar.setDescripcion("Hermoso lago en los Andes");
        lugar.setDireccion("Capachica");
        lugar.setCiudad("Puno");
        lugar.setProvincia("Puno");
        lugar.setPais("Perú");
        lugar.setLatitud(-15.7653);
        lugar.setLongitud(-69.5321);
    }

    @Test
    void testGetLugares() {
        // Arrange
        when(lugarRepository.findAll()).thenReturn(List.of(lugar));

        // Act
        List<Lugar> lugares = lugarService.getlugares();

        // Assert
        assertNotNull(lugares);
        assertEquals(1, lugares.size());
        assertEquals("Lago Titicaca", lugares.get(0).getNombre());
    }

    @Test
    void testGetLugarById() {
        // Arrange
        when(lugarRepository.findById(1L)).thenReturn(Optional.of(lugar));

        // Act
        Lugar foundLugar = lugarService.getLugarById(1L);

        // Assert
        assertNotNull(foundLugar);
        assertEquals("Lago Titicaca", foundLugar.getNombre());
    }

    @Test
    void testGetLugarById_NotFound() {
        // Arrange
        when(lugarRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> lugarService.getLugarById(999L));
        assertEquals("Lugar con id: 999 no encontrado", thrown.getMessage());
    }

    @Test
    void testPostLugar() {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);  // Simulate an empty file (no image)

        when(lugarRepository.save(any(Lugar.class))).thenReturn(lugar);

        // Act
        Lugar savedLugar = lugarService.postLugar(lugarDto, file);

        // Assert
        assertNotNull(savedLugar);
        assertEquals("Lago Titicaca", savedLugar.getNombre());
    }

    @Test
    void testPutLugar() {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);  // Simulate an empty file (no image)

        when(lugarRepository.findById(1L)).thenReturn(Optional.of(lugar));
        when(lugarRepository.save(any(Lugar.class))).thenReturn(lugar);

        // Act
        Lugar updatedLugar = lugarService.putLugar(1L, lugarDto, file);

        // Assert
        assertNotNull(updatedLugar);
        assertEquals("Lago Titicaca", updatedLugar.getNombre());
    }


    @Test
    void testBuscarLugarPorNombre() {
        // Arrange
        when(lugarRepository.buscarPorNombre("Titicaca")).thenReturn(List.of(lugar));

        // Act
        List<Lugar> result = lugarService.buscarLugarPorNombre("Titicaca");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lago Titicaca", result.get(0).getNombre());
    }

}
