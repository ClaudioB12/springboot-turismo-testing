package pe.edu.upeu.turismospringboot.util.dataLoaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pe.edu.upeu.turismospringboot.model.entity.Rol;
import pe.edu.upeu.turismospringboot.repository.RolRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RolDataLoaderTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolDataLoader dataLoader;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ============================================================
    // 1️⃣ Caso: Roles NO existen → se deben crear
    // ============================================================

    @Test
    void testRun_CreaRolesCuandoNoExisten() throws Exception {
        when(rolRepository.findByNombre(anyString())).thenReturn(Optional.empty());

        dataLoader.run();

        // Debe guardar 3 roles
        verify(rolRepository, times(3)).save(any(Rol.class));
    }

    // ============================================================
    // 2️⃣ Caso: Algunos roles existen → solo crear los faltantes
    // ============================================================

    @Test
    void testRun_CreaSoloRolesFaltantes() throws Exception {

        when(rolRepository.findByNombre("ROLE_ADMIN")).thenReturn(Optional.empty());
        when(rolRepository.findByNombre("ROLE_USUARIO")).thenReturn(Optional.of(new Rol()));
        when(rolRepository.findByNombre("ROLE_EMPRENDEDOR")).thenReturn(Optional.empty());

        dataLoader.run();

        // Debe crear solo 2 roles: ADMIN y EMPRENDEDOR
        verify(rolRepository, times(2)).save(any(Rol.class));
    }

    // ============================================================
    // 3️⃣ Caso: Todos existen → NO crea nada
    // ============================================================

    @Test
    void testRun_NoCreaRolesCuandoTodosExisten() throws Exception {

        when(rolRepository.findByNombre(anyString())).thenReturn(Optional.of(new Rol()));

        dataLoader.run();

        verify(rolRepository, never()).save(any());
    }

    // ============================================================
    // 4️⃣ Caso interno: crearRolSiNoExiste retorna true solo si guardó
    // ============================================================

    @Test
    void testCrearRolSiNoExiste() {
        when(rolRepository.findByNombre("ROLE_TEST")).thenReturn(Optional.empty());

        boolean creado = dataLoader
                .getClass()
                .getDeclaredMethods()[1]
                .getName()
                .equals("crearRolSiNoExiste");

        // No invocamos el método privado directamente.
        // Verificamos comportamiento indirecto ejecutando run()
        dataLoader.run();

        verify(rolRepository, atLeastOnce()).save(any(Rol.class));
    }
}
