package pe.edu.upeu.turismospringboot.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LugarTest {

    @Test
    void debeAsignarYLeerAtributosBasicos() {
        Lugar lugar = new Lugar();

        lugar.setIdLugar(1L);
        lugar.setNombre("Capachica");
        lugar.setDescripcion("Hermoso lugar turístico");
        lugar.setDireccion("Av. Principal 123");
        lugar.setCiudad("Puno");
        lugar.setProvincia("Puno");
        lugar.setPais("Perú");
        lugar.setLatitud(-15.123);
        lugar.setLongitud(-70.123);
        lugar.setImagenUrl("imagen.jpg");

        assertEquals(1L, lugar.getIdLugar());
        assertEquals("Capachica", lugar.getNombre());
        assertEquals("Hermoso lugar turístico", lugar.getDescripcion());
        assertEquals("Av. Principal 123", lugar.getDireccion());
        assertEquals("Puno", lugar.getCiudad());
        assertEquals("Puno", lugar.getProvincia());
        assertEquals("Perú", lugar.getPais());
        assertEquals(-15.123, lugar.getLatitud());
        assertEquals(-70.123, lugar.getLongitud());
        assertEquals("imagen.jpg", lugar.getImagenUrl());
    }

    @Test
    void debeAsignarListaDeFamilias() {
        Lugar lugar = new Lugar();

        Familia fam = new Familia();
        fam.setIdFamilia(10L);

        ArrayList<Familia> familias = new ArrayList<>();
        familias.add(fam);

        lugar.setFamilias(familias);

        assertNotNull(lugar.getFamilias());
        assertEquals(1, lugar.getFamilias().size());
        assertEquals(10L, lugar.getFamilias().get(0).getIdFamilia());
    }

    @Test
    void debeEjecutarPrePersist() {
        Lugar lugar = new Lugar();
        lugar.onCreate();

        assertNotNull(lugar.getFechaCreacionLugar());
        assertTrue(lugar.getFechaCreacionLugar().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void debeEjecutarPreUpdate() {
        Lugar lugar = new Lugar();
        lugar.onUpdate();

        assertNotNull(lugar.getFechaModificacionLugar());
        assertTrue(lugar.getFechaModificacionLugar().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void debeSerializarCorrectamenteSinProblemasDeReferencias() throws Exception {
        Lugar lugar = new Lugar();
        lugar.setIdLugar(1L);
        lugar.setNombre("Lugar Test");

        // Familia relacionada
        Familia f = new Familia();
        f.setIdFamilia(50L);

        ArrayList<Familia> familias = new ArrayList<>();
        familias.add(f);
        lugar.setFamilias(familias);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(lugar);

        // Validaciones
        assertTrue(json.contains("Lugar Test")); // nombre serializado
        assertTrue(json.contains("\"familias\"")); // lista incluida
        assertFalse(json.contains("lugar-familia")); // referencia back no serializa
    }
}
