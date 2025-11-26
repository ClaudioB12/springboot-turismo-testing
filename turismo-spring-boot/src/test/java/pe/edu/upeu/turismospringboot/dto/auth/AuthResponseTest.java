package pe.edu.upeu.turismospringboot.dto.auth;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.auth.AuthResponse;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void testConstructorVacio() {
        AuthResponse auth = new AuthResponse();
        assertNull(auth.getToken());
    }

    @Test
    void testConstructorConArgumentos() {
        AuthResponse auth = new AuthResponse("ABC123");
        assertEquals("ABC123", auth.getToken());
    }

    @Test
    void testBuilder() {
        AuthResponse auth = AuthResponse.builder()
                .token("TOKEN123")
                .build();

        assertEquals("TOKEN123", auth.getToken());
    }

    @Test
    void testSettersYGetters() {
        AuthResponse auth = new AuthResponse();
        auth.setToken("XYZ987");

        assertEquals("XYZ987", auth.getToken());
    }

    @Test
    void testEqualsYHashCode() {
        AuthResponse a1 = new AuthResponse("TOKEN");
        AuthResponse a2 = new AuthResponse("TOKEN");

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testToString() {
        AuthResponse auth = new AuthResponse("TEST123");

        String texto = auth.toString();

        assertTrue(texto.contains("token=TEST123"));
        assertTrue(texto.contains("AuthResponse"));
    }
}
