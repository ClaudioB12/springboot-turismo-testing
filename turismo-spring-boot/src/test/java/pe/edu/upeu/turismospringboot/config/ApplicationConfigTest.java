package pe.edu.upeu.turismospringboot.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upeu.turismospringboot.service.auth.UsuarioDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationConfigTest {

    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------
    // TEST 1: PasswordEncoder bean
    // -------------------------------
    @Test
    void testPasswordEncoder() {
        PasswordEncoder encoder = applicationConfig.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);

        // Verificar funcionamiento
        String raw = "123456";
        String encoded = encoder.encode(raw);

        assertNotEquals(raw, encoded);
        assertTrue(encoder.matches(raw, encoded));
    }

    // ---------------------------------------
    // TEST 2: AuthenticationProvider bean
    // ---------------------------------------
    @Test
    void testAuthenticationProvider() {
        // Usamos SPY para verificar invocaciones internas
        DaoAuthenticationProvider spyProvider = spy(new DaoAuthenticationProvider());

        // Reemplazamos el provider real usando un SPY manual
        ApplicationConfig config = new ApplicationConfig(usuarioDetailsService) {
            @Override
            public AuthenticationProvider authenticationProvider() {
                spyProvider.setUserDetailsService(usuarioDetailsService);
                spyProvider.setPasswordEncoder(passwordEncoder());
                return spyProvider;
            }
        };

        AuthenticationProvider provider = config.authenticationProvider();

        assertNotNull(provider);
        assertTrue(provider instanceof DaoAuthenticationProvider);

        // Validar que internamente llamó al setter del userDetailsService
        verify(spyProvider).setUserDetailsService(usuarioDetailsService);

        // Validar que llamó al setter del passwordEncoder
        verify(spyProvider).setPasswordEncoder(any(PasswordEncoder.class));
    }


    // ---------------------------------------
    // TEST 3: AuthenticationManager bean
    // ---------------------------------------
    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationManager mockManager = mock(AuthenticationManager.class);

        when(authenticationConfiguration.getAuthenticationManager())
                .thenReturn(mockManager);

        AuthenticationManager result = applicationConfig.authenticationManager(authenticationConfiguration);

        assertNotNull(result);
        assertEquals(mockManager, result);

        verify(authenticationConfiguration).getAuthenticationManager();
    }
}
