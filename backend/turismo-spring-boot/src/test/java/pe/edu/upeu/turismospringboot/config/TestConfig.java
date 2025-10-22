package pe.edu.upeu.turismospringboot.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pe.edu.upeu.turismospringboot.service.CategoriaService;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public CategoriaService categoriaService() {
        return mock(CategoriaService.class);
    }

    // Si luego tienes más servicios, los agregas aquí
}
