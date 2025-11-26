package pe.edu.upeu.turismospringboot.util.dataLoaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;
import pe.edu.upeu.turismospringboot.repository.LugarRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LugaresDataLoaderTest {

    @Mock
    private LugarRepository lugarRepository;

    @InjectMocks
    private LugaresDataLoader lugaresDataLoader;

    private Lugar lugar1;
    private Lugar lugar2;

    @BeforeEach
    void setUp() {
        lugar1 = new Lugar();
        lugar1.setNombre("Valle Escondido de Lunaria");
        lugar1.setDescripcion("Un valle místico oculto entre montañas iluminadas por bioluminiscencia natural.");
        lugar1.setDireccion("Sendero de la Niebla Azul");
        lugar1.setCiudad("Lunaria");
        lugar1.setProvincia("Nébulas Altas");
        lugar1.setPais("Térra Fantástica");
        lugar1.setLatitud(-34.1234);
        lugar1.setLongitud(12.5678);
        lugar1.setImagenUrl("lugar1.jpg");
        lugar1.setFechaCreacionLugar(LocalDateTime.now());

        lugar2 = new Lugar();
        lugar2.setNombre("Isla de los Ecos Susurrantes");
        lugar2.setDescripcion("Una isla legendaria donde los ecos cuentan historias antiguas a quienes saben escuchar.");
        lugar2.setDireccion("Costa del Silencio Eterno");
        lugar2.setCiudad("Echonia");
        lugar2.setProvincia("Archipiélago Murmullo");
        lugar2.setPais("Reino de la Bruma");
        lugar2.setLatitud(5.4321);
        lugar2.setLongitud(-45.6789);
        lugar2.setImagenUrl("lugar2.jpg");
        lugar2.setFechaCreacionLugar(LocalDateTime.now());
    }


    @Test
    void testCargarDatosIniciales_whenLugaresExist_shouldNotSaveLugares() {
        // Arrange
        when(lugarRepository.count()).thenReturn(2L);  // Simula que ya existen lugares en la base de datos

        // Act
        lugaresDataLoader.cargarDatosIniciales();

        // Assert
        verify(lugarRepository, times(0)).saveAll(anyList());  // No debería llamarse saveAll
    }
}
