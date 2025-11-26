package pe.edu.upeu.turismospringboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.LugarDto;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;
import pe.edu.upeu.turismospringboot.repository.LugarRepository;
import pe.edu.upeu.turismospringboot.service.impl.LugarServiceImpl;
import pe.edu.upeu.turismospringboot.util.ArchivoUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LugarServiceTest {

    @Mock
    private LugarRepository lugarRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private LugarServiceImpl lugarService;

    private Lugar lugar1;
    private Lugar lugar2;
    private LugarDto lugarDto;
    private Familia familia1;
    private Familia familia2;

    @BeforeEach
    void setUp() {
        try {
            System.out.println("\n=== INICIO SETUP ===");

            // Crear Familias
            System.out.println("1. Creando Familias mock...");
            familia1 = new Familia();
            familia1.setIdFamilia(1L);
            familia1.setNombre("Familia García");
            System.out.println("   ✓ Familia 1: " + familia1.getNombre());

            familia2 = new Familia();
            familia2.setIdFamilia(2L);
            familia2.setNombre("Familia López");
            System.out.println("   ✓ Familia 2: " + familia2.getNombre());

            // Crear Lugares
            System.out.println("2. Creando Lugares mock...");
            lugar1 = new Lugar();
            lugar1.setIdLugar(1L);
            lugar1.setNombre("Machu Picchu");
            lugar1.setDescripcion("Ciudadela inca del siglo XV");
            lugar1.setDireccion("Aguas Calientes, Cusco");
            lugar1.setLatitud(-13.1631);
            lugar1.setLongitud(-72.5450);
            lugar1.setImagenUrl("machu_picchu.jpg");
            lugar1.setFamilias(Arrays.asList(familia1, familia2));
            System.out.println("   ✓ Lugar 1: " + lugar1.getNombre());

            lugar2 = new Lugar();
            lugar2.setIdLugar(2L);
            lugar2.setNombre("Lago Titicaca");
            lugar2.setDescripcion("El lago navegable más alto del mundo");
            lugar2.setDireccion("Puno, Perú");
            lugar2.setLatitud(-15.8402);
            lugar2.setLongitud(-69.4914);
            lugar2.setImagenUrl("titicaca.jpg");
            lugar2.setFamilias(Arrays.asList(familia1));
            System.out.println("   ✓ Lugar 2: " + lugar2.getNombre());

            // Crear DTO
            System.out.println("3. Creando LugarDto mock...");
            lugarDto = new LugarDto();
            lugarDto.setNombre("Valle Sagrado");
            lugarDto.setDescripcion("Valle histórico en los Andes");
            lugarDto.setDireccion("Cusco, Perú");
            lugarDto.setLatitud(-13.3198);
            lugarDto.setLongitud(-72.0854);
            System.out.println("   ✓ DTO creado: " + lugarDto.getNombre());

            System.out.println("=== FIN SETUP EXITOSO ===\n");

        } catch (Exception e) {
            System.err.println("❌ ERROR EN SETUP: " + e.getClass().getName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 1: getlugares() - Debe retornar todos los lugares")
    void testGetLugares() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 1: getlugares()");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando lista de lugares...");
            List<Lugar> lugares = Arrays.asList(lugar1, lugar2);
            when(lugarRepository.findAll()).thenReturn(lugares);
            System.out.println("   ✓ Mock configurado con " + lugares.size() + " lugares");

            // When
            System.out.println("\nWHEN: Ejecutando getlugares()...");
            List<Lugar> resultado = lugarService.getlugares();
            System.out.println("   → Lugares obtenidos: " + resultado.size());

            for (Lugar lugar : resultado) {
                System.out.println("      • " + lugar.getNombre() + " - " + lugar.getDireccion());
            }

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals("Machu Picchu", resultado.get(0).getNombre());
            assertEquals("Lago Titicaca", resultado.get(1).getNombre());

            verify(lugarRepository, times(1)).findAll();
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testGetLugares:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 2: getLugarById() - Exitoso")
    void testGetLugarById_Exitoso() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 2: getLugarById() - Exitoso");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando lugar...");
            when(lugarRepository.findById(1L)).thenReturn(Optional.of(lugar1));
            System.out.println("   ✓ Mock configurado para idLugar=1");

            // When
            System.out.println("\nWHEN: Ejecutando getLugarById(1L)...");
            Lugar resultado = lugarService.getLugarById(1L);
            System.out.println("   → Lugar encontrado: " + resultado.getNombre());
            System.out.println("   → Ubicación: " + resultado.getDireccion());

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado);
            assertEquals(1L, resultado.getIdLugar());
            assertEquals("Machu Picchu", resultado.getNombre());

            verify(lugarRepository, times(1)).findById(1L);
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testGetLugarById_Exitoso:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 3: getLugarById() - No existe")
    void testGetLugarById_NoExiste() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 3: getLugarById() - No Existe");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando mock para ID inexistente...");
            when(lugarRepository.findById(999L)).thenReturn(Optional.empty());
            System.out.println("   ✓ Mock configurado");

            // When & Then
            System.out.println("\nWHEN/THEN: Ejecutando y esperando excepción...");
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                lugarService.getLugarById(999L);
            });

            System.out.println("   → Excepción lanzada: " + exception.getClass().getSimpleName());
            System.out.println("   → Mensaje: " + exception.getMessage());
            assertTrue(exception.getMessage().contains("no encontrado") ||
                    exception.getMessage().contains("not found"));

            verify(lugarRepository, times(1)).findById(999L);
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testGetLugarById_NoExiste:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 4: postLugar() - Sin archivo")
    void testPostLugar_SinArchivo() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 4: postLugar() - Sin Archivo");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando DTO y mocks...");
            when(lugarRepository.save(any(Lugar.class))).thenReturn(lugar1);
            System.out.println("   ✓ Mocks configurados");
            System.out.println("   ✓ Archivo: null (sin imagen)");

            // When
            System.out.println("\nWHEN: Ejecutando postLugar()...");
            Lugar resultado = lugarService.postLugar(lugarDto, null);
            System.out.println("   → Lugar creado: " + resultado.getNombre());

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado);
            assertEquals("Machu Picchu", resultado.getNombre());

            verify(lugarRepository, times(1)).save(any(Lugar.class));
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testPostLugar_SinArchivo:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 6: putLugar() - Sin archivo")
    void testPutLugar_SinArchivo() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 6: putLugar() - Sin Archivo");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando actualización de lugar...");
            when(lugarRepository.findById(1L)).thenReturn(Optional.of(lugar1));
            when(lugarRepository.save(any(Lugar.class))).thenReturn(lugar1);
            System.out.println("   ✓ Mocks configurados para actualización");

            // When
            System.out.println("\nWHEN: Ejecutando putLugar(1L, dto, null)...");
            Lugar resultado = lugarService.putLugar(1L, lugarDto, null);
            System.out.println("   → Lugar actualizado: " + resultado.getNombre());

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado);

            verify(lugarRepository, times(1)).findById(1L);
            verify(lugarRepository, times(1)).save(any(Lugar.class));
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testPutLugar_SinArchivo:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 8: putLugar() - Lugar no existe")
    void testPutLugar_LugarNoExiste() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 8: putLugar() - Lugar No Existe");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando mock para lugar inexistente...");
            when(lugarRepository.findById(999L)).thenReturn(Optional.empty());
            System.out.println("   ✓ Mock configurado");

            // When & Then
            System.out.println("\nWHEN/THEN: Ejecutando y esperando excepción...");
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                lugarService.putLugar(999L, lugarDto, null);
            });

            System.out.println("   → Excepción lanzada: " + exception.getClass().getSimpleName());
            System.out.println("   → Mensaje: " + exception.getMessage());
            assertTrue(exception.getMessage().contains("no encontrado") ||
                    exception.getMessage().contains("not found"));

            verify(lugarRepository, times(1)).findById(999L);
            verify(lugarRepository, never()).save(any());
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testPutLugar_LugarNoExiste:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 9: deleteLugar() - Exitoso")
    void testDeleteLugar() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 9: deleteLugar()");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando eliminación de lugar ID=1...");
            doNothing().when(lugarRepository).deleteById(1L);
            System.out.println("   ✓ Mock configurado");

            // When
            System.out.println("\nWHEN: Ejecutando deleteLugar(1L)...");
            lugarService.deleteLugar(1L);
            System.out.println("   → Eliminación ejecutada");

            // Then
            System.out.println("\nTHEN: Verificando que se llamó deleteById...");
            verify(lugarRepository, times(1)).deleteById(1L);
            System.out.println("   ✓ Verificación completada");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testDeleteLugar:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 10: buscarLugarPorNombre() - Exitoso")
    void testBuscarLugarPorNombre() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 10: buscarLugarPorNombre()");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando búsqueda por nombre...");
            List<Lugar> lugares = Arrays.asList(lugar1);
            when(lugarRepository.buscarPorNombre("Machu")).thenReturn(lugares);
            System.out.println("   ✓ Mock configurado para buscar 'Machu'");

            // When
            System.out.println("\nWHEN: Ejecutando buscarLugarPorNombre('Machu')...");
            List<Lugar> resultado = lugarService.buscarLugarPorNombre("Machu");
            System.out.println("   → Lugares encontrados: " + resultado.size());

            for (Lugar lugar : resultado) {
                System.out.println("      • " + lugar.getNombre());
            }

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals("Machu Picchu", resultado.get(0).getNombre());

            verify(lugarRepository, times(1)).buscarPorNombre("Machu");
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testBuscarLugarPorNombre:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 11: getFamiliasPorLugar() - Sin filtro de nombre")
    void testGetFamiliasPorLugar_SinFiltro() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 11: getFamiliasPorLugar() - Sin Filtro");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando lugar con familias...");
            when(lugarRepository.findById(1L)).thenReturn(Optional.of(lugar1));
            System.out.println("   ✓ Mock configurado con " + lugar1.getFamilias().size() + " familias");

            // When
            System.out.println("\nWHEN: Ejecutando getFamiliasPorLugar(1L, null)...");
            List<Familia> resultado = lugarService.getFamiliasPorLugar(1L, null);
            System.out.println("   → Familias obtenidas: " + resultado.size());

            for (Familia familia : resultado) {
                System.out.println("      • " + familia.getNombre());
            }

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals("Familia García", resultado.get(0).getNombre());
            assertEquals("Familia López", resultado.get(1).getNombre());

            verify(lugarRepository, times(1)).findById(1L);
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testGetFamiliasPorLugar_SinFiltro:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 12: getFamiliasPorLugar() - Con filtro de nombre")
    void testGetFamiliasPorLugar_ConFiltro() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 12: getFamiliasPorLugar() - Con Filtro");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando lugar con familias...");
            when(lugarRepository.findById(1L)).thenReturn(Optional.of(lugar1));
            System.out.println("   ✓ Mock configurado");

            // When
            String filtro = "garcía";
            System.out.println("\nWHEN: Ejecutando con filtro: '" + filtro + "'");
            List<Familia> resultado = lugarService.getFamiliasPorLugar(1L, filtro);
            System.out.println("   → Familias filtradas: " + resultado.size());

            for (Familia familia : resultado) {
                System.out.println("      • " + familia.getNombre());
            }

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertTrue(resultado.get(0).getNombre().toLowerCase().contains("garcía"));

            verify(lugarRepository, times(1)).findById(1L);
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testGetFamiliasPorLugar_ConFiltro:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 13: getFamiliasPorLugar() - Lugar no existe")
    void testGetFamiliasPorLugar_LugarNoExiste() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 13: getFamiliasPorLugar() - Lugar No Existe");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando mock para ID inexistente...");
            when(lugarRepository.findById(999L)).thenReturn(Optional.empty());
            System.out.println("   ✓ Mock configurado para retornar vacío");

            // When & Then
            System.out.println("\nWHEN/THEN: Ejecutando y esperando excepción...");
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                lugarService.getFamiliasPorLugar(999L, null);
            });

            System.out.println("   → Excepción lanzada: " + exception.getClass().getSimpleName());
            System.out.println("   → Mensaje: " + exception.getMessage());
            assertTrue(exception.getMessage().contains("no encontrado") ||
                    exception.getMessage().contains("not found"));

            verify(lugarRepository, times(1)).findById(999L);
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testGetFamiliasPorLugar_LugarNoExiste:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}