package pe.edu.upeu.turismospringboot.controller.emprendedor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FileEmprendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_DIR = "upload";

    @BeforeEach
    void setUp() throws Exception {
        // Crear directorio base si no existe
        Files.createDirectories(Paths.get(BASE_DIR));
    }

    @AfterEach
    void tearDown() throws Exception {
        // Limpiar archivos de prueba después de cada test
        Path uploadPath = Paths.get(BASE_DIR);
        if (Files.exists(uploadPath)) {
            Files.walk(uploadPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    @WithMockUser(username = "emprendedor", roles = {"EMPRENDEDOR"})
    void debeSubirImagenCorrectamente() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido de prueba".getBytes()
        );

        // Act & Assert
        MvcResult result = mockMvc.perform(multipart("/emprendedor/file/upload")
                        .file(file)
                        .param("tipo", "IMAGEN"))
                .andExpect(status().isOk())
                .andReturn();

        String nombreArchivo = result.getResponse().getContentAsString();
        assertThat(nombreArchivo).isNotEmpty();
        assertThat(nombreArchivo).contains("test-image.jpg");

        // Verificar que el archivo se guardó físicamente
        Path archivoGuardado = Paths.get(BASE_DIR, "imagenes", nombreArchivo);
        assertThat(Files.exists(archivoGuardado)).isTrue();
    }

    @Test
    @WithMockUser(username = "emprendedor", roles = {"EMPRENDEDOR"})
    void debeSubirAudioCorrectamente() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-audio.mp3",
                "audio/mpeg",
                "audio de prueba".getBytes()
        );

        MvcResult result = mockMvc.perform(multipart("/emprendedor/file/upload")
                        .file(file)
                        .param("tipo", "AUDIO"))
                .andExpect(status().isOk())
                .andReturn();

        String nombreArchivo = result.getResponse().getContentAsString();
        Path archivoGuardado = Paths.get(BASE_DIR, "audios", nombreArchivo);
        assertThat(Files.exists(archivoGuardado)).isTrue();
    }

    @Test
    @WithMockUser(username = "emprendedor", roles = {"EMPRENDEDOR"})
    void debeSubirDocumentoCorrectamente() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-doc.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "documento de prueba".getBytes()
        );

        MvcResult result = mockMvc.perform(multipart("/emprendedor/file/upload")
                        .file(file)
                        .param("tipo", "DOCUMENTO"))
                .andExpect(status().isOk())
                .andReturn();

        String nombreArchivo = result.getResponse().getContentAsString();
        Path archivoGuardado = Paths.get(BASE_DIR, "documentos", nombreArchivo);
        assertThat(Files.exists(archivoGuardado)).isTrue();
    }

    @Test
    @WithMockUser(username = "emprendedor", roles = {"EMPRENDEDOR"})
    void debeSubirVideoCorrectamente() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-video.mp4",
                "video/mp4",
                "video de prueba".getBytes()
        );

        MvcResult result = mockMvc.perform(multipart("/emprendedor/file/upload")
                        .file(file)
                        .param("tipo", "VIDEO"))
                .andExpect(status().isOk())
                .andReturn();

        String nombreArchivo = result.getResponse().getContentAsString();
        Path archivoGuardado = Paths.get(BASE_DIR, "videos", nombreArchivo);
        assertThat(Files.exists(archivoGuardado)).isTrue();
    }

    @Test
    @WithMockUser(username = "emprendedor", roles = {"EMPRENDEDOR"})
    void debeRetornarBadRequestConTipoInvalido() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "contenido".getBytes()
        );

        mockMvc.perform(multipart("/emprendedor/file/upload")
                        .file(file)
                        .param("tipo", "INVALIDO"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Tipo inválido"));
    }

    @Test
    @WithMockUser(username = "emprendedor", roles = {"EMPRENDEDOR"})
    void debeDescargarArchivoExistente() throws Exception {
        // Arrange: Primero subir un archivo
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-download.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido para descargar".getBytes()
        );

        MvcResult uploadResult = mockMvc.perform(multipart("/emprendedor/file/upload")
                        .file(file)
                        .param("tipo", "IMAGEN"))
                .andExpect(status().isOk())
                .andReturn();

        String nombreArchivo = uploadResult.getResponse().getContentAsString();

        // Act & Assert: Descargar el archivo
        mockMvc.perform(get("/emprendedor/file/imagenes/{filename}", nombreArchivo))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes("contenido para descargar".getBytes()));
    }

    @Test
    @WithMockUser(username = "emprendedor", roles = {"EMPRENDEDOR"})
    void debeRetornar404CuandoArchivoNoExiste() throws Exception {
        mockMvc.perform(get("/emprendedor/file/imagenes/archivo-inexistente.jpg"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "emprendedor", roles = {"EMPRENDEDOR"})
    void debeGenerarNombreUnicoParaCadaArchivo() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "mismo-nombre.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido 1".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "mismo-nombre.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido 2".getBytes()
        );

        // Subir primer archivo
        MvcResult result1 = mockMvc.perform(multipart("/emprendedor/file/upload")
                        .file(file1)
                        .param("tipo", "IMAGEN"))
                .andExpect(status().isOk())
                .andReturn();

        // Subir segundo archivo con mismo nombre
        MvcResult result2 = mockMvc.perform(multipart("/emprendedor/file/upload")
                        .file(file2)
                        .param("tipo", "IMAGEN"))
                .andExpect(status().isOk())
                .andReturn();

        String nombre1 = result1.getResponse().getContentAsString();
        String nombre2 = result2.getResponse().getContentAsString();

        // Los nombres deben ser diferentes (UUID diferente)
        assertThat(nombre1).isNotEqualTo(nombre2);
        assertThat(nombre1).contains("mismo-nombre.jpg");
        assertThat(nombre2).contains("mismo-nombre.jpg");
    }

    @Test
    @WithMockUser(username = "emprendedor", roles = {"EMPRENDEDOR"})
    void debeCrearDirectoriosSiNoExisten() throws Exception {
        // Eliminar directorio de audios si existe
        Path audiosPath = Paths.get(BASE_DIR, "audios");
        if (Files.exists(audiosPath)) {
            Files.walk(audiosPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "nuevo-audio.mp3",
                "audio/mpeg",
                "audio".getBytes()
        );

        mockMvc.perform(multipart("/emprendedor/file/upload")
                        .file(file)
                        .param("tipo", "AUDIO"))
                .andExpect(status().isOk());

        // Verificar que el directorio se creó
        assertThat(Files.exists(audiosPath)).isTrue();
    }

    @Test
    void debeDenegarAccesoSinAutenticacion() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido".getBytes()
        );

        mockMvc.perform(multipart("/emprendedor/file/upload")
                        .file(file)
                        .param("tipo", "IMAGEN"))
                .andExpect(status().isForbidden()); // ✅ Cambiado de isUnauthorized() a isForbidden()
    }
}