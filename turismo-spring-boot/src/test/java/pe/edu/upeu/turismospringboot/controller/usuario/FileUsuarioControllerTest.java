package pe.edu.upeu.turismospringboot.controller.usuario;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - FileUsuarioController")
class FileUsuarioControllerTest {

    @InjectMocks
    private FileUsuarioController fileUsuarioController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ServletContext servletContext;

    private static final String BASE_DIR = "upload";
    private Path uploadDir;

    @BeforeEach
    void setUp() throws IOException {
        uploadDir = Paths.get(BASE_DIR);
        Files.createDirectories(uploadDir);
        when(request.getServletContext()).thenReturn(servletContext);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Limpiar directorios de prueba
        if (Files.exists(uploadDir)) {
            Files.walk(uploadDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            // Ignorar errores de limpieza
                        }
                    });
        }
    }


    @Test
    @DisplayName("Ver archivo existente - Debe retornar recurso con status 200")
    void verArchivo_ArchivoExistente_DebeRetornarRecurso() throws IOException {
        // Arrange
        String tipo = "imagenes";
        String filename = "test-view.jpg";
        Path testFile = Paths.get(BASE_DIR, tipo, filename);
        Files.createDirectories(testFile.getParent());
        Files.write(testFile, "test content".getBytes());

        when(servletContext.getMimeType(anyString())).thenReturn("image/jpeg");

        // Act
        ResponseEntity<Resource> response = fileUsuarioController.verArchivo(tipo, filename, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().exists());
        assertEquals(MediaType.parseMediaType("image/jpeg"), response.getHeaders().getContentType());
    }


    @Test
    @DisplayName("Ver archivo - Tipo MIME desconocido debe usar octet-stream")
    void verArchivo_TipoMimeDesconocido_DebeUsarOctetStream() throws IOException {
        // Arrange
        String tipo = "documentos";
        String filename = "unknown.xyz";
        Path testFile = Paths.get(BASE_DIR, tipo, filename);
        Files.createDirectories(testFile.getParent());
        Files.write(testFile, "unknown content".getBytes());

        when(servletContext.getMimeType(anyString())).thenReturn(null);

        // Act
        ResponseEntity<Resource> response = fileUsuarioController.verArchivo(tipo, filename, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.parseMediaType("application/octet-stream"),
                response.getHeaders().getContentType());
    }

    @Test
    @DisplayName("Ver archivo PDF - Debe retornar tipo application/pdf")
    void verArchivo_ArchivoPDF_DebeRetornarTipoPDF() throws IOException {
        // Arrange
        String tipo = "documentos";
        String filename = "document.pdf";
        Path testFile = Paths.get(BASE_DIR, tipo, filename);
        Files.createDirectories(testFile.getParent());
        Files.write(testFile, "PDF content".getBytes());

        when(servletContext.getMimeType(anyString())).thenReturn("application/pdf");

        // Act
        ResponseEntity<Resource> response = fileUsuarioController.verArchivo(tipo, filename, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.parseMediaType("application/pdf"),
                response.getHeaders().getContentType());
    }

    @Test
    @DisplayName("Ver archivo de audio - Debe retornar tipo audio/mpeg")
    void verArchivo_ArchivoAudio_DebeRetornarTipoAudio() throws IOException {
        // Arrange
        String tipo = "audios";
        String filename = "song.mp3";
        Path testFile = Paths.get(BASE_DIR, tipo, filename);
        Files.createDirectories(testFile.getParent());
        Files.write(testFile, "audio content".getBytes());

        when(servletContext.getMimeType(anyString())).thenReturn("audio/mpeg");

        // Act
        ResponseEntity<Resource> response = fileUsuarioController.verArchivo(tipo, filename, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.parseMediaType("audio/mpeg"),
                response.getHeaders().getContentType());
    }

    @Test
    @DisplayName("Ver archivo de video - Debe retornar tipo video/mp4")
    void verArchivo_ArchivoVideo_DebeRetornarTipoVideo() throws IOException {
        // Arrange
        String tipo = "videos";
        String filename = "movie.mp4";
        Path testFile = Paths.get(BASE_DIR, tipo, filename);
        Files.createDirectories(testFile.getParent());
        Files.write(testFile, "video content".getBytes());

        when(servletContext.getMimeType(anyString())).thenReturn("video/mp4");

        // Act
        ResponseEntity<Resource> response = fileUsuarioController.verArchivo(tipo, filename, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.parseMediaType("video/mp4"),
                response.getHeaders().getContentType());
    }

    @Test
    @DisplayName("Ver archivo con nombre que contiene punto - Debe funcionar correctamente")
    void verArchivo_NombreConPuntos_DebeFuncionar() throws IOException {
        // Arrange
        String tipo = "imagenes";
        String filename = "test.image.final.jpg";
        Path testFile = Paths.get(BASE_DIR, tipo, filename);
        Files.createDirectories(testFile.getParent());
        Files.write(testFile, "content".getBytes());

        when(servletContext.getMimeType(anyString())).thenReturn("image/jpeg");

        // Act
        ResponseEntity<Resource> response = fileUsuarioController.verArchivo(tipo, filename, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Flujo completo - Subir y luego ver archivo")
    void flujoCompleto_SubirYVerArchivo_DebeFuncionar() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "integration-test.jpg",
                "image/jpeg",
                "integration test content".getBytes()
        );

        // Act - Subir
        ResponseEntity<String> uploadResponse = fileUsuarioController.uploadFile(file, "IMAGEN");
        assertEquals(HttpStatus.OK, uploadResponse.getStatusCode());
        String filename = uploadResponse.getBody();

        // Configurar mock para ver archivo
        when(servletContext.getMimeType(anyString())).thenReturn("image/jpeg");

        // Act - Ver
        ResponseEntity<Resource> viewResponse = fileUsuarioController.verArchivo("imagenes", filename, request);

        // Assert
        assertEquals(HttpStatus.OK, viewResponse.getStatusCode());
        assertNotNull(viewResponse.getBody());
        assertTrue(viewResponse.getBody().exists());
    }



}