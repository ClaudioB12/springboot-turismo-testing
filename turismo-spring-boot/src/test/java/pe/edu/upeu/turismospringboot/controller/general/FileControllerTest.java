package pe.edu.upeu.turismospringboot.controller.general;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - FileController")
class FileControllerTest {

    @InjectMocks
    private FileController fileController;

    @TempDir
    Path tempDir;

    private Path uploadDir;
    private MockedStatic<Paths> pathsMock;
    private MockedStatic<Files> filesMock;

    @BeforeEach
    void setUp() throws IOException {
        // Crear directorio temporal para las pruebas
        uploadDir = tempDir.resolve("upload/imagenes");
        Files.createDirectories(uploadDir);

        // Configurar el UPLOAD_DIR del controller
        ReflectionTestUtils.setField(fileController, "UPLOAD_DIR", uploadDir.toString());
    }

    @AfterEach
    void tearDown() {
        if (pathsMock != null) {
            pathsMock.close();
        }
        if (filesMock != null) {
            filesMock.close();
        }
    }

    @Test
    @DisplayName("Descargar archivo existente - Debe retornar archivo con status 200")
    void downloadFile_ArchivoExistente_DebeRetornarArchivo() throws IOException {
        // Arrange
        String fileName = "test-image.jpg";
        Path testFile = uploadDir.resolve(fileName);
        Files.write(testFile, "test content".getBytes());

        pathsMock = mockStatic(Paths.class);
        pathsMock.when(() -> Paths.get(uploadDir.toString(), fileName))
                .thenReturn(testFile);

        filesMock = mockStatic(Files.class);
        filesMock.when(() -> Files.probeContentType(testFile))
                .thenReturn("image/jpeg");

        // Act
        ResponseEntity<Resource> response = fileController.downloadFile(fileName);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(MediaType.parseMediaType("image/jpeg"), response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION)
                .contains("attachment"));
        assertTrue(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION)
                .contains(fileName));
    }

    @Test
    @DisplayName("Descargar archivo - Tipo de contenido desconocido usa octet-stream")
    void downloadFile_TipoContenidoDesconocido_DebeUsarOctetStream() throws IOException {
        // Arrange
        String fileName = "test-file.unknown";
        Path testFile = uploadDir.resolve(fileName);
        Files.write(testFile, "test content".getBytes());

        pathsMock = mockStatic(Paths.class);
        pathsMock.when(() -> Paths.get(uploadDir.toString(), fileName))
                .thenReturn(testFile);

        filesMock = mockStatic(Files.class);
        filesMock.when(() -> Files.probeContentType(testFile))
                .thenReturn(null);

        // Act
        ResponseEntity<Resource> response = fileController.downloadFile(fileName);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.parseMediaType("application/octet-stream"),
                response.getHeaders().getContentType());
    }

    @Test
    @DisplayName("Descargar archivo no existente - Debe retornar 404")
    void downloadFile_ArchivoNoExistente_DebeRetornar404() {
        // Arrange
        String fileName = "non-existent-file.jpg";
        Path testFile = uploadDir.resolve(fileName);

        pathsMock = mockStatic(Paths.class);
        pathsMock.when(() -> Paths.get(uploadDir.toString(), fileName))
                .thenReturn(testFile);

        // Act
        ResponseEntity<Resource> response = fileController.downloadFile(fileName);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Descargar archivo con PNG - Debe retornar tipo image/png")
    void downloadFile_ArchivoPNG_DebeRetornarTipoImagenPNG() throws IOException {
        // Arrange
        String fileName = "test-image.png";
        Path testFile = uploadDir.resolve(fileName);
        Files.write(testFile, "PNG content".getBytes());

        pathsMock = mockStatic(Paths.class);
        pathsMock.when(() -> Paths.get(uploadDir.toString(), fileName))
                .thenReturn(testFile);

        filesMock = mockStatic(Files.class);
        filesMock.when(() -> Files.probeContentType(testFile))
                .thenReturn("image/png");

        // Act
        ResponseEntity<Resource> response = fileController.downloadFile(fileName);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.parseMediaType("image/png"),
                response.getHeaders().getContentType());
    }

    @Test
    @DisplayName("Descargar archivo con PDF - Debe retornar tipo application/pdf")
    void downloadFile_ArchivoPDF_DebeRetornarTipoPDF() throws IOException {
        // Arrange
        String fileName = "document.pdf";
        Path testFile = uploadDir.resolve(fileName);
        Files.write(testFile, "PDF content".getBytes());

        pathsMock = mockStatic(Paths.class);
        pathsMock.when(() -> Paths.get(uploadDir.toString(), fileName))
                .thenReturn(testFile);

        filesMock = mockStatic(Files.class);
        filesMock.when(() -> Files.probeContentType(testFile))
                .thenReturn("application/pdf");

        // Act
        ResponseEntity<Resource> response = fileController.downloadFile(fileName);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.parseMediaType("application/pdf"),
                response.getHeaders().getContentType());
    }

    @Test
    @DisplayName("Descargar archivo con nombre especial - Debe manejar correctamente")
    void downloadFile_NombreEspecial_DebeManejarcorrectamente() throws IOException {
        // Arrange
        String fileName = "test file with spaces.jpg";
        Path testFile = uploadDir.resolve(fileName);
        Files.write(testFile, "content".getBytes());

        pathsMock = mockStatic(Paths.class);
        pathsMock.when(() -> Paths.get(uploadDir.toString(), fileName))
                .thenReturn(testFile);

        filesMock = mockStatic(Files.class);
        filesMock.when(() -> Files.probeContentType(testFile))
                .thenReturn("image/jpeg");

        // Act
        ResponseEntity<Resource> response = fileController.downloadFile(fileName);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION)
                .contains(fileName));
    }


    @Test
    @DisplayName("Descargar archivo - Verificar header Content-Disposition correcto")
    void downloadFile_VerificarHeaderContentDisposition() throws IOException {
        // Arrange
        String fileName = "my-document.pdf";
        Path testFile = uploadDir.resolve(fileName);
        Files.write(testFile, "test".getBytes());

        pathsMock = mockStatic(Paths.class);
        pathsMock.when(() -> Paths.get(uploadDir.toString(), fileName))
                .thenReturn(testFile);

        filesMock = mockStatic(Files.class);
        filesMock.when(() -> Files.probeContentType(testFile))
                .thenReturn("application/pdf");

        // Act
        ResponseEntity<Resource> response = fileController.downloadFile(fileName);

        // Assert
        String contentDisposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        assertNotNull(contentDisposition);
        assertTrue(contentDisposition.startsWith("attachment;"));
        assertTrue(contentDisposition.contains("filename=\"" + fileName + "\""));
    }
}