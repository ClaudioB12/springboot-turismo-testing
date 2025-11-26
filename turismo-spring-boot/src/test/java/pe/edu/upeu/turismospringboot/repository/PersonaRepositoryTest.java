package pe.edu.upeu.turismospringboot.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.turismospringboot.model.entity.Persona;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PersonaRepositoryTest {

    @Autowired
    private PersonaRepository personaRepository;

    private Persona persona;

    @BeforeEach
    void setUp() {
        persona = new Persona();
        persona.setNombres("Juan");
        persona.setApellidos("Pérez");
        persona.setCorreoElectronico("juan@example.com");
        persona.setTipoDocumento("DNI");
        persona.setNumeroDocumento("DOC-" + System.nanoTime());  // 🔥 SIEMPRE ÚNICO
        persona.setTelefono("999999999");
        persona.setDireccion("Av. Lima 123");
        persona.setFechaNacimiento(LocalDate.of(2000, 1, 1));
    }

    @Test
    void testGuardarPersona() {
        Persona guardada = personaRepository.save(persona);
        assertThat(guardada.getIdPersona()).isNotNull();
    }

    @Test
    void testFindById() {
        Persona guardada = personaRepository.save(persona);
        Persona encontrada = personaRepository.findById(guardada.getIdPersona()).orElse(null);
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getNombres()).isEqualTo("Juan");
    }

    @Test
    void testActualizarPersona() {
        Persona guardada = personaRepository.save(persona);
        guardada.setNombres("Carlos");

        Persona actualizada = personaRepository.save(guardada);

        assertThat(actualizada.getNombres()).isEqualTo("Carlos");
    }

    @Test
    void testEliminarPersona() {
        Persona guardada = personaRepository.save(persona);

        personaRepository.deleteById(guardada.getIdPersona());

        assertThat(personaRepository.findById(guardada.getIdPersona())).isEmpty();
    }
}
