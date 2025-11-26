package pe.edu.upeu.turismospringboot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.turismospringboot.model.dto.FamiliaCategoriaDto;
import pe.edu.upeu.turismospringboot.model.dto.FamiliaCategoriaDtoPost;
import pe.edu.upeu.turismospringboot.model.entity.*;
import pe.edu.upeu.turismospringboot.repository.CategoriaRepository;
import pe.edu.upeu.turismospringboot.repository.FamiliaCategoriaRepository;
import pe.edu.upeu.turismospringboot.repository.FamiliaRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FamiliaCategoriaServiceImplTest {

    @Mock
    private FamiliaCategoriaRepository familiaCategoriaRepository;

    @Mock
    private FamiliaRepository familiaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private FamiliaCategoriaServiceImpl familiaCategoriaService;

    private Familia familia;
    private Categoria categoria;
    private FamiliaCategoria familiaCategoria;
    private Emprendimiento emprendimiento1;
    private Emprendimiento emprendimiento2;

    @BeforeEach
    void setUp() {
        try {
            System.out.println("\n=== INICIO SETUP ===");

            // Crear Familia
            System.out.println("1. Creando Familia mock...");
            familia = new Familia();
            familia.setIdFamilia(1L);
            familia.setNombre("Familia García");
            System.out.println("   ✓ Familia creada: ID=" + familia.getIdFamilia() + ", Nombre=" + familia.getNombre());

            // Crear Categoría
            System.out.println("2. Creando Categoría mock...");
            categoria = new Categoria();
            categoria.setIdCategoria(1L);
            categoria.setNombre("Restaurante");
            System.out.println("   ✓ Categoría creada: ID=" + categoria.getIdCategoria() + ", Nombre=" + categoria.getNombre());

            // Crear FamiliaCategoria
            System.out.println("3. Creando FamiliaCategoria mock...");
            familiaCategoria = new FamiliaCategoria();
            familiaCategoria.setIdFamiliaCategoria(1L);
            familiaCategoria.setFamilia(familia);
            familiaCategoria.setCategoria(categoria);
            familiaCategoria.setFechaCreacionFamiliaCategoria(LocalDateTime.now());
            familiaCategoria.setFechaModificacionFamiliaCategoria(LocalDateTime.now());
            System.out.println("   ✓ FamiliaCategoria creada: ID=" + familiaCategoria.getIdFamiliaCategoria());

            // Crear Emprendimientos
            System.out.println("4. Creando Emprendimientos mock...");
            emprendimiento1 = new Emprendimiento();
            emprendimiento1.setIdEmprendimiento(1L);
            emprendimiento1.setNombre("Restaurant El Sabor");
            emprendimiento1.setFamiliaCategoria(familiaCategoria);
            System.out.println("   ✓ Emprendimiento 1: " + emprendimiento1.getNombre());

            emprendimiento2 = new Emprendimiento();
            emprendimiento2.setIdEmprendimiento(2L);
            emprendimiento2.setNombre("Cafetería Aroma");
            emprendimiento2.setFamiliaCategoria(familiaCategoria);
            System.out.println("   ✓ Emprendimiento 2: " + emprendimiento2.getNombre());

            // Asignar emprendimientos a familiaCategoria
            familiaCategoria.setEmprendimientos(Arrays.asList(emprendimiento1, emprendimiento2));
            System.out.println("   ✓ Total emprendimientos en FamiliaCategoria: " + familiaCategoria.getEmprendimientos().size());

            System.out.println("=== FIN SETUP EXITOSO ===\n");

        } catch (Exception e) {
            System.err.println("❌ ERROR EN SETUP: " + e.getClass().getName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 1: asociarCategoriaAFamilia() - Debe crear relación exitosamente")
    void testAsociarCategoriaAFamilia_Exitoso() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 1: asociarCategoriaAFamilia() - Exitoso");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando DTO y mocks...");
            FamiliaCategoriaDtoPost dto = new FamiliaCategoriaDtoPost();
            dto.setIdFamilia(1L);
            dto.setIdCategoria(1L);
            System.out.println("   DTO: idFamilia=" + dto.getIdFamilia() + ", idCategoria=" + dto.getIdCategoria());

            when(familiaRepository.findById(1L)).thenReturn(Optional.of(familia));
            when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
            when(familiaCategoriaRepository.save(any(FamiliaCategoria.class))).thenReturn(familiaCategoria);
            System.out.println("   ✓ Mocks configurados");

            // When
            System.out.println("\nWHEN: Ejecutando asociarCategoriaAFamilia()...");
            FamiliaCategoria resultado = familiaCategoriaService.asociarCategoriaAFamilia(dto);
            System.out.println("   → Resultado ID: " + resultado.getIdFamiliaCategoria());
            System.out.println("   → Familia: " + resultado.getFamilia().getNombre());
            System.out.println("   → Categoría: " + resultado.getCategoria().getNombre());

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado, "El resultado no debería ser null");
            assertEquals(1L, resultado.getIdFamiliaCategoria());
            assertEquals("Familia García", resultado.getFamilia().getNombre());
            assertEquals("Restaurante", resultado.getCategoria().getNombre());

            verify(familiaRepository, times(1)).findById(1L);
            verify(categoriaRepository, times(1)).findById(1L);
            verify(familiaCategoriaRepository, times(1)).save(any(FamiliaCategoria.class));
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testAsociarCategoriaAFamilia_Exitoso:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 2: asociarCategoriaAFamilia() - Debe lanzar excepción cuando familia no existe")
    void testAsociarCategoriaAFamilia_FamiliaNoExiste() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 2: asociarCategoriaAFamilia() - Familia No Existe");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando DTO con familia inexistente...");
            FamiliaCategoriaDtoPost dto = new FamiliaCategoriaDtoPost();
            dto.setIdFamilia(999L);
            dto.setIdCategoria(1L);
            System.out.println("   DTO: idFamilia=" + dto.getIdFamilia() + " (NO EXISTE)");

            when(familiaRepository.findById(999L)).thenReturn(Optional.empty());
            System.out.println("   ✓ Mock configurado para retornar vacío");

            // When & Then
            System.out.println("\nWHEN/THEN: Ejecutando y esperando excepción...");
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                familiaCategoriaService.asociarCategoriaAFamilia(dto);
            });

            System.out.println("   → Excepción lanzada: " + exception.getClass().getSimpleName());
            System.out.println("   → Mensaje: " + exception.getMessage());
            assertEquals("No existe la familia", exception.getMessage());

            verify(familiaRepository, times(1)).findById(999L);
            verify(categoriaRepository, never()).findById(any());
            verify(familiaCategoriaRepository, never()).save(any());
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testAsociarCategoriaAFamilia_FamiliaNoExiste:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 3: asociarCategoriaAFamilia() - Debe lanzar excepción cuando categoría no existe")
    void testAsociarCategoriaAFamilia_CategoriaNoExiste() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 3: asociarCategoriaAFamilia() - Categoría No Existe");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando DTO con categoría inexistente...");
            FamiliaCategoriaDtoPost dto = new FamiliaCategoriaDtoPost();
            dto.setIdFamilia(1L);
            dto.setIdCategoria(999L);
            System.out.println("   DTO: idCategoria=" + dto.getIdCategoria() + " (NO EXISTE)");

            when(familiaRepository.findById(1L)).thenReturn(Optional.of(familia));
            when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());
            System.out.println("   ✓ Mocks configurados");

            // When & Then
            System.out.println("\nWHEN/THEN: Ejecutando y esperando excepción...");
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                familiaCategoriaService.asociarCategoriaAFamilia(dto);
            });

            System.out.println("   → Excepción lanzada: " + exception.getClass().getSimpleName());
            System.out.println("   → Mensaje: " + exception.getMessage());
            assertEquals("Categoría no encontrada", exception.getMessage());

            verify(familiaRepository, times(1)).findById(1L);
            verify(categoriaRepository, times(1)).findById(999L);
            verify(familiaCategoriaRepository, never()).save(any());
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testAsociarCategoriaAFamilia_CategoriaNoExiste:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 4: listarRelaciones() - Debe retornar lista de DTOs")
    void testListarRelaciones() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 4: listarRelaciones()");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando lista de FamiliaCategorias...");
            List<FamiliaCategoria> listaFamiliaCategorias = Arrays.asList(familiaCategoria);
            when(familiaCategoriaRepository.findAll()).thenReturn(listaFamiliaCategorias);
            System.out.println("   ✓ Mock configurado con " + listaFamiliaCategorias.size() + " relación(es)");

            // When
            System.out.println("\nWHEN: Ejecutando listarRelaciones()...");
            List<FamiliaCategoriaDto> resultado = familiaCategoriaService.listarRelaciones();
            System.out.println("   → DTOs obtenidos: " + resultado.size());

            if (!resultado.isEmpty()) {
                FamiliaCategoriaDto dto = resultado.get(0);
                System.out.println("   → DTO[0]: ID=" + dto.getIdFamiliaCategoria());
                System.out.println("   → Familia: " + dto.getNombreFamilia());
                System.out.println("   → Categoría: " + dto.getNombreCategoria());
                System.out.println("   → Emprendimientos: " + dto.getEmprendimientos().size());
            }

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado, "El resultado no debería ser null");
            assertEquals(1, resultado.size());
            assertEquals(1L, resultado.get(0).getIdFamiliaCategoria());
            assertEquals("Familia García", resultado.get(0).getNombreFamilia());
            assertEquals("Restaurante", resultado.get(0).getNombreCategoria());
            assertEquals(2, resultado.get(0).getEmprendimientos().size());

            verify(familiaCategoriaRepository, times(1)).findAll();
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testListarRelaciones:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 5: obtenerFamiliaCategoriaPorIdFamilia() - Debe retornar DTOs por familia")
    void testObtenerFamiliaCategoriaPorIdFamilia() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 5: obtenerFamiliaCategoriaPorIdFamilia()");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando búsqueda por idFamilia=1...");
            List<FamiliaCategoria> listaFamiliaCategorias = Arrays.asList(familiaCategoria);
            when(familiaCategoriaRepository.findByFamiliaIdFamilia(1L)).thenReturn(listaFamiliaCategorias);
            System.out.println("   ✓ Mock configurado");

            // When
            System.out.println("\nWHEN: Ejecutando obtenerFamiliaCategoriaPorIdFamilia(1L)...");
            List<FamiliaCategoriaDto> resultado = familiaCategoriaService.obtenerFamiliaCategoriaPorIdFamilia(1L);
            System.out.println("   → DTOs obtenidos: " + resultado.size());

            if (!resultado.isEmpty()) {
                System.out.println("   → Familia encontrada: " + resultado.get(0).getNombreFamilia());
            }

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals(1L, resultado.get(0).getIdFamilia());
            assertEquals("Familia García", resultado.get(0).getNombreFamilia());

            verify(familiaCategoriaRepository, times(1)).findByFamiliaIdFamilia(1L);
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testObtenerFamiliaCategoriaPorIdFamilia:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 6: obtenerFamiliaCategoriaPorIdCategoria() - Debe retornar DTOs por categoría")
    void testObtenerFamiliaCategoriaPorIdCategoria() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 6: obtenerFamiliaCategoriaPorIdCategoria()");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando búsqueda por idCategoria=1...");
            List<FamiliaCategoria> listaFamiliaCategorias = Arrays.asList(familiaCategoria);
            when(familiaCategoriaRepository.findByCategoriaIdCategoria(1L)).thenReturn(listaFamiliaCategorias);
            System.out.println("   ✓ Mock configurado");

            // When
            System.out.println("\nWHEN: Ejecutando obtenerFamiliaCategoriaPorIdCategoria(1L)...");
            List<FamiliaCategoriaDto> resultado = familiaCategoriaService.obtenerFamiliaCategoriaPorIdCategoria(1L);
            System.out.println("   → DTOs obtenidos: " + resultado.size());

            if (!resultado.isEmpty()) {
                System.out.println("   → Categoría encontrada: " + resultado.get(0).getNombreCategoria());
            }

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals(1L, resultado.get(0).getIdCategoria());
            assertEquals("Restaurante", resultado.get(0).getNombreCategoria());

            verify(familiaCategoriaRepository, times(1)).findByCategoriaIdCategoria(1L);
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testObtenerFamiliaCategoriaPorIdCategoria:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 7: eliminarRelacion() - Debe eliminar relación por ID")
    void testEliminarRelacion() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 7: eliminarRelacion()");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando eliminación de relación ID=1...");
            doNothing().when(familiaCategoriaRepository).deleteById(1L);
            System.out.println("   ✓ Mock configurado");

            // When
            System.out.println("\nWHEN: Ejecutando eliminarRelacion(1L)...");
            familiaCategoriaService.eliminarRelacion(1L);
            System.out.println("   → Eliminación ejecutada");

            // Then
            System.out.println("\nTHEN: Verificando que se llamó deleteById...");
            verify(familiaCategoriaRepository, times(1)).deleteById(1L);
            System.out.println("   ✓ Verificación completada");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testEliminarRelacion:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 8: getEmprendimientosPorFamiliaCategoria() - Sin filtro de nombre")
    void testGetEmprendimientosPorFamiliaCategoria_SinFiltro() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 8: getEmprendimientosPorFamiliaCategoria() - Sin Filtro");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando FamiliaCategoria con emprendimientos...");
            when(familiaCategoriaRepository.findById(1L)).thenReturn(Optional.of(familiaCategoria));
            System.out.println("   ✓ Mock configurado con " + familiaCategoria.getEmprendimientos().size() + " emprendimientos");

            // When
            System.out.println("\nWHEN: Ejecutando getEmprendimientosPorFamiliaCategoria(1L, null)...");
            List<Emprendimiento> resultado = familiaCategoriaService.getEmprendimientosPorFamiliaCategoria(1L, null);
            System.out.println("   → Emprendimientos obtenidos: " + resultado.size());

            for (Emprendimiento emp : resultado) {
                System.out.println("      • " + emp.getNombre());
            }

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals("Restaurant El Sabor", resultado.get(0).getNombre());
            assertEquals("Cafetería Aroma", resultado.get(1).getNombre());

            verify(familiaCategoriaRepository, times(1)).findById(1L);
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testGetEmprendimientosPorFamiliaCategoria_SinFiltro:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 9: getEmprendimientosPorFamiliaCategoria() - Con filtro de nombre")
    void testGetEmprendimientosPorFamiliaCategoria_ConFiltro() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 9: getEmprendimientosPorFamiliaCategoria() - Con Filtro");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando FamiliaCategoria con emprendimientos...");
            when(familiaCategoriaRepository.findById(1L)).thenReturn(Optional.of(familiaCategoria));
            System.out.println("   ✓ Mock configurado");

            // When
            String filtro = "restaurant";
            System.out.println("\nWHEN: Ejecutando con filtro: '" + filtro + "'");
            List<Emprendimiento> resultado = familiaCategoriaService.getEmprendimientosPorFamiliaCategoria(1L, filtro);
            System.out.println("   → Emprendimientos filtrados: " + resultado.size());

            for (Emprendimiento emp : resultado) {
                System.out.println("      • " + emp.getNombre());
            }

            // Then
            System.out.println("\nTHEN: Verificando resultado...");
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertTrue(resultado.get(0).getNombre().toLowerCase().contains("restaurant"));

            verify(familiaCategoriaRepository, times(1)).findById(1L);
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testGetEmprendimientosPorFamiliaCategoria_ConFiltro:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @DisplayName("Test 10: getEmprendimientosPorFamiliaCategoria() - FamiliaCategoria no existe")
    void testGetEmprendimientosPorFamiliaCategoria_NoExiste() {
        try {
            System.out.println("\n========================================");
            System.out.println("TEST 10: getEmprendimientosPorFamiliaCategoria() - No Existe");
            System.out.println("========================================");

            // Given
            System.out.println("GIVEN: Preparando mock para ID inexistente...");
            when(familiaCategoriaRepository.findById(999L)).thenReturn(Optional.empty());
            System.out.println("   ✓ Mock configurado para retornar vacío");

            // When & Then
            System.out.println("\nWHEN/THEN: Ejecutando y esperando excepción...");
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                familiaCategoriaService.getEmprendimientosPorFamiliaCategoria(999L, null);
            });

            System.out.println("   → Excepción lanzada: " + exception.getClass().getSimpleName());
            System.out.println("   → Mensaje: " + exception.getMessage());
            assertEquals("FamiliaCategoria no encontrada", exception.getMessage());

            verify(familiaCategoriaRepository, times(1)).findById(999L);
            System.out.println("   ✓ Verificaciones completadas");
            System.out.println("   ✓ PRUEBA EXITOSA\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR en testGetEmprendimientosPorFamiliaCategoria_NoExiste:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}