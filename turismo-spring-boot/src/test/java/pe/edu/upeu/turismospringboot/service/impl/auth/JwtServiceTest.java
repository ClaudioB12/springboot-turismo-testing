package pe.edu.upeu.turismospringboot.service.impl.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import pe.edu.upeu.turismospringboot.service.auth.JwtService;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testGenerateAndValidateToken() {
        // Arrange
        UserDetails user = User.builder()
                .username("testuser")
                .password("password")
                .authorities(Collections.singletonList(() -> "ROLE_USER"))
                .build();

        // Act
        String token = jwtService.getToken(user);

        // Assert
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void testGetKeyWithReflection() throws Exception {
        // Act
        Method getKeyMethod = JwtService.class.getDeclaredMethod("getKey");
        getKeyMethod.setAccessible(true);
        Object key = getKeyMethod.invoke(jwtService);

        // Assert
        assertNotNull(key);
    }

    @Test
    void testIsTokenExpiredWithReflection() throws Exception {
        // Arrange
        String token = jwtService.getToken(User.builder().username("testuser").password("password").authorities(Collections.singletonList(() -> "ROLE_USER")).build());

        // Act
        Method isTokenExpiredMethod = JwtService.class.getDeclaredMethod("isTokenExpired", String.class);
        isTokenExpiredMethod.setAccessible(true);
        boolean isExpired = (boolean) isTokenExpiredMethod.invoke(jwtService, token);

        // Assert
        assertFalse(isExpired);  // Token recién generado no debe estar expirado
    }
}
