package pe.edu.upeu.turismospringboot.enums;

import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.enums.EstadoCuenta;

import static org.junit.jupiter.api.Assertions.*;

class EstadoCuentaTest {

    @Test
    void testEnumContainsAllValues() {
        EstadoCuenta[] values = EstadoCuenta.values();

        assertEquals(3, values.length);
        assertNotNull(values);
    }

    @Test
    void testEnumValuesCorrectOrder() {
        assertEquals(EstadoCuenta.ACTIVO, EstadoCuenta.values()[0]);
        assertEquals(EstadoCuenta.INACTIVO, EstadoCuenta.values()[1]);
        assertEquals(EstadoCuenta.BLOQUEADO, EstadoCuenta.values()[2]);
    }

    @Test
    void testValueOfValid() {
        assertEquals(EstadoCuenta.ACTIVO, EstadoCuenta.valueOf("ACTIVO"));
        assertEquals(EstadoCuenta.INACTIVO, EstadoCuenta.valueOf("INACTIVO"));
        assertEquals(EstadoCuenta.BLOQUEADO, EstadoCuenta.valueOf("BLOQUEADO"));
    }

    @Test
    void testValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            EstadoCuenta.valueOf("NO_EXISTE");
        });
    }

    @Test
    void testEnumToString() {
        assertEquals("ACTIVO", EstadoCuenta.ACTIVO.toString());
        assertEquals("INACTIVO", EstadoCuenta.INACTIVO.toString());
        assertEquals("BLOQUEADO", EstadoCuenta.BLOQUEADO.toString());
    }
}
