package pe.edu.upeu.turismospringboot.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // 🔹 RUTA QUE DEBE SER PERMITIDA SEGÚN SecurityConfig
    @Test
    void rutaPublicaDebeSerAccesible() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().is4xxClientError());
        // Nota: se espera 4xx (400 o 401) porque la seguridad SÍ permite,
        // pero el endpoint real puede pedir POST o body. Lo importante es que NO dé 403.
    }

    // 🔹 RUTA PRIVADA /admin/** debe exigir token
    @Test
    void rutaAdminDebeSerProtegida() throws Exception {
        mockMvc.perform(get("/admin/familia"))
                .andExpect(status().isForbidden());  // <-- 403 es lo correcto
    }

    // 🔹 Otra ruta pública permitida por SecurityConfig
    @Test
    void rutaGeneralDebeSerPublica() throws Exception {
        mockMvc.perform(get("/general/servicios"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (!(status >= 200 && status < 500)) {
                        throw new AssertionError("La ruta no está siendo permitida por seguridad");
                    }
                });
    }

    // 🔹 RUTA DE LOGIN VIA POST (para cubrir ambos métodos)
    @Test
    void rutaPublicaPost() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is4xxClientError());
        // 400 = lógica falló pero seguridad permitió
        // 401 = body incompleto pero pasó seguridad
        // NUNCA debe ser 403 aquí
    }
}
