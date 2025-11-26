package pe.edu.upeu.turismospringboot.dto.auth;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.auth.LoginRequest;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testConstructorVacio() {
        LoginRequest req = new LoginRequest();
        assertNull(req.getUsername());
        assertNull(req.getPassword());
    }

    @Test
    void testConstructorConArgumentos() {
        LoginRequest req = new LoginRequest("user123", "secret");
        assertEquals("user123", req.getUsername());
        assertEquals("secret", req.getPassword());
    }

    @Test
    void testBuilder() {
        LoginRequest req = LoginRequest.builder()
                .username("admin")
                .password("12345")
                .build();

        assertEquals("admin", req.getUsername());
        assertEquals("12345", req.getPassword());
    }

    @Test
    void testSettersYGetters() {
        LoginRequest req = new LoginRequest();
        req.setUsername("testUser");
        req.setPassword("pwd123");

        assertEquals("testUser", req.getUsername());
        assertEquals("pwd123", req.getPassword());
    }

    @Test
    void testEqualsYHashCode() {
        LoginRequest a = new LoginRequest("user", "pass");
        LoginRequest b = new LoginRequest("user", "pass");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToString() {
        LoginRequest req = new LoginRequest("userX", "pwd");

        String text = req.toString();

        assertTrue(text.contains("userX"));
        assertTrue(text.contains("pwd"));
        assertTrue(text.contains("LoginRequest"));
    }
}
