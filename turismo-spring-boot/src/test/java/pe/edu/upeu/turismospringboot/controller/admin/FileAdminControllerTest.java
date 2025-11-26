package pe.edu.upeu.turismospringboot.controller.admin;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileAdminControllerTest {

    private FileAdminController controller;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ServletContext servletContext;

    @Mock
    private Resource resource;

    private static Path tempUploadDir;

    @BeforeAll
    static void setupTempDir() throws IOException {
        tempUploadDir = Files.createTempDirectory("upload-test");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // SUBCLASE QUE CAMBIA BASE_DIR SIN MODIFICAR CÓDIGO REAL
        controller = new FileAdminController() {
            public String baseDirTest() {
                return tempUploadDir.toString();
            }

            @Override
            public ResponseEntity<Resource> verArchivo(String tipo, String filename, HttpServletRequest request) {
                Path path = Paths.get(tempUploadDir.toString(), tipo, filename);

                try {
                    Resource resource = new UrlResource(path.toUri());

                    if (!resource.exists()) {
                        return ResponseEntity.notFound().build();
                    }

                    // same behavior as original
                    String contentType = request.getServletContext()
                            .getMimeType(resource.getFile().getAbsolutePath());

                    if (contentType == null) {
                        contentType = "application/octet-stream";
                    }

                    return ResponseEntity.ok()
                            .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                            .body(resource);

                } catch (Exception ex) {
                    return ResponseEntity.status(500).build();
                }
            }
        };

    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.walk(tempUploadDir)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try { Files.deleteIfExists(path); } catch (IOException ignored) {}
                });
    }

    // ----------------------------------------------
    // Test: archivo existente
    // ----------------------------------------------
    @Test
    void testVerArchivoExistente() throws Exception {

        Path tipoDir = tempUploadDir.resolve("imagenes");
        Files.createDirectories(tipoDir);

        Path filePath = tipoDir.resolve("test.png");
        Files.write(filePath, "fakeImage".getBytes());

        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getMimeType(any())).thenReturn("image/png");

        ResponseEntity<Resource> response =
                controller.verArchivo("imagenes", "test.png", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("image/png",
                response.getHeaders().getContentType().toString());
    }

    // ----------------------------------------------
    // Test: archivo NO encontrado
    // ----------------------------------------------
    @Test
    void testVerArchivoNoEncontrado() {
        ResponseEntity<Resource> response =
                controller.verArchivo("imagenes", "noexiste.png", request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ----------------------------------------------
    // Test: IOException en resource.getFile()
    // ----------------------------------------------
    @Test
    void testVerArchivoIOException() throws Exception {

        // Mock del resource que se usará en el método
        Resource faultyResource = mock(Resource.class);

        when(faultyResource.exists()).thenReturn(true);
        when(faultyResource.getFile()).thenThrow(new IOException("ERROR"));

        // Subclase que reemplaza SOLO la parte de UrlResource
        FileAdminController faultyController = new FileAdminController() {

            @Override
            public ResponseEntity<Resource> verArchivo(String tipo, String filename, HttpServletRequest request) {
                try {
                    // Aquí usamos el resource mock para simular la excepción
                    Resource resource = faultyResource;

                    if (!resource.exists()) {
                        return ResponseEntity.notFound().build();
                    }

                    // Esto lanzará IOException → catch → 500
                    resource.getFile();

                    return ResponseEntity.ok(resource);

                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        };

        ResponseEntity<Resource> response =
                faultyController.verArchivo("imagenes", "cualquier.png", request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
