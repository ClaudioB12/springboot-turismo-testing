package pe.edu.upeu.turismospringboot.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomOpenAPIConfigTest {

    private CustomOpenAPIConfig config;

    @BeforeEach
    void setUp() {
        config = new CustomOpenAPIConfig();
    }

    @Test
    void testCustomOpenAPIConfiguration() {
        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI, "OpenAPI no debe ser nulo");

        // --- INFO ---
        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("OPEN API SISTEMA DE Turismo", info.getTitle());
        assertEquals("0.0.1", info.getVersion());
        assertEquals("Servicios web de turismo", info.getDescription());
        assertEquals("http://swagger.io/terms/", info.getTermsOfService());
        assertNotNull(info.getLicense());
        assertEquals("Apache 2.0", info.getLicense().getName());
        assertEquals("http://springdoc.org", info.getLicense().getUrl());

        // --- SECURITY REQUIREMENT ---
        assertNotNull(openAPI.getSecurity());
        assertFalse(openAPI.getSecurity().isEmpty());

        SecurityRequirement securityRequirement = openAPI.getSecurity().get(0);
        assertTrue(securityRequirement.containsKey("bearerAuth"));

        // --- COMPONENTS & SECURITY SCHEME ---
        Components components = openAPI.getComponents();
        assertNotNull(components);

        SecurityScheme scheme = components.getSecuritySchemes().get("bearerAuth");
        assertNotNull(scheme, "El esquema de seguridad no debe ser nulo");

        assertEquals("Authorization", scheme.getName());
        assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
        assertEquals("bearer", scheme.getScheme());
        assertEquals("JWT", scheme.getBearerFormat());
    }
}
