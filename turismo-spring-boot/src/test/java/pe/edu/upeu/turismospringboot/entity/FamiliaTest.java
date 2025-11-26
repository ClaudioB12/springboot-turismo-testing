package pe.edu.upeu.turismospringboot.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.entity.Familia;
import pe.edu.upeu.turismospringboot.model.entity.FamiliaCategoria;
import pe.edu.upeu.turismospringboot.model.entity.Lugar;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FamiliaTest {

    @Test
    void debeAsignarYLeerAtributosBasicos() {
        Familia familia = new Familia();

        familia.setIdFamilia(1L);
        familia.setNombre("Turismo Vivencial");
        familia.setDescripcion("Actividades tradicionales de Capachica");
        familia.setImagenUrl("imagen.jpg");

        assertEquals(1L, familia.getIdFamilia());
        assertEquals("Turismo Vivencial", familia.getNombre());
        assertEquals("Actividades tradicionales de Capachica", familia.getDescripcion());
        assertEquals("imagen.jpg", familia.getImagenUrl());
    }

    @Test
    void debeAsignarLugarCorrectamente() {
        Familia familia = new Familia();
        Lugar lugar = new Lugar();
        lugar.setIdLugar(10L);
        lugar.setNombre("Capachica");

        familia.setLugar(lugar);

        assertNotNull(familia.getLugar());
        assertEquals(10L, familia.getLugar().getIdLugar());
        assertEquals("Capachica", familia.getLugar().getNombre());
    }

    @Test
    void debeAsignarListaFamiliaCategorias() {
        Familia familia = new Familia();
        FamiliaCategoria categoria = new FamiliaCategoria();
        categoria.setIdFamiliaCategoria(5L);

        ArrayList<FamiliaCategoria> lista = new ArrayList<>();
        lista.add(categoria);

        familia.setFamiliaCategorias(lista);

        assertNotNull(familia.getFamiliaCategorias());
        assertEquals(1, familia.getFamiliaCategorias().size());
        assertEquals(5L, familia.getFamiliaCategorias().get(0).getIdFamiliaCategoria());
    }

    @Test
    void debeEjecutarPrePersist() {
        Familia familia = new Familia();
        familia.onCreate();

        assertNotNull(familia.getFechaCreacionFamilia());
        assertTrue(familia.getFechaCreacionFamilia().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void debeEjecutarPreUpdate() {
        Familia familia = new Familia();
        familia.onUpdate();

        assertNotNull(familia.getFechaModificacionFamilia());
        assertTrue(familia.getFechaModificacionFamilia().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void debeSerializarIgnorandoReferenciasCirculares() throws Exception {
        Familia familia = new Familia();
        familia.setIdFamilia(1L);
        familia.setNombre("Familia Test");

        Lugar lugar = new Lugar();
        lugar.setIdLugar(99L);
        lugar.setNombre("Lugar Test");
        familia.setLugar(lugar);

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(familia);

        assertTrue(json.contains("Familia Test"));
        assertFalse(json.contains("lugar-familia")); // no debe serializar referencia back
    }
}
