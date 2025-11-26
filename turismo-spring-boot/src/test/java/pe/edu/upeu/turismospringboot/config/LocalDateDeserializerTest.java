package pe.edu.upeu.turismospringboot.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocalDateDeserializerTest {

    private LocalDateDeserializer deserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deserializer = new LocalDateDeserializer();
    }

    @Test
    void testDeserializeValidDate() throws Exception {
        // Entrada simulada: fecha válida en formato "yyyy-MM-dd"
        when(jsonParser.getText()).thenReturn("2025-03-15");

        LocalDate result = deserializer.deserialize(jsonParser, context);

        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 3, 15), result);
    }

    @Test
    void testDeserializeInvalidDateFormat() {
        try {
            when(jsonParser.getText()).thenReturn("15/03/2025"); // formato incorrecto

            deserializer.deserialize(jsonParser, context);
            fail("Se esperaba una excepción por formato incorrecto");

        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException ||
                    e instanceof java.time.format.DateTimeParseException);
        }
    }

    @Test
    void testDeserializeNullValue() throws Exception {
        when(jsonParser.getText()).thenReturn(null);

        assertThrows(NullPointerException.class, () ->
                deserializer.deserialize(jsonParser, context)
        );
    }
}
