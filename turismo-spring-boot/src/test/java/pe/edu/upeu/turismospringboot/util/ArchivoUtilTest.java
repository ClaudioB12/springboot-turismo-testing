package pe.edu.upeu.turismospringboot.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArchivoUtilTest {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/upload/imagenes/";

    @Mock
    private MockMultipartFile mockMultipartFile;

    @BeforeEach
    void setUp() {
        // Inicialización de MockMultipartFile
        mockMultipartFile = new MockMultipartFile("file", "testImage.jpg", "image/jpeg", "test content".getBytes());
    }

    @Test
    void testSaveFile() {
        // Preparar el entorno para el test
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Llamar al método saveFile
        String fileName = ArchivoUtil.saveFile(mockMultipartFile);

        // Verificar que el archivo se haya guardado
        File savedFile = new File(directory, fileName);
        assertTrue(savedFile.exists());

        // Limpiar el archivo creado después de la prueba
        if (savedFile.exists()) {
            savedFile.delete();
        }

        // Limpiar el directorio
        directory.delete();
    }

}
