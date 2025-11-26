package pe.edu.upeu.turismospringboot.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.ServicioTuristicoDto;
import pe.edu.upeu.turismospringboot.model.entity.ServicioTuristico;
import pe.edu.upeu.turismospringboot.service.ServicioTuristicoService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioTuristicoControllerTest {

    @InjectMocks
    private ServicioTuristicoController controller;

    @Mock
    private ServicioTuristicoService servicioTuristicoService;

    @Mock
    private MultipartFile multipartFile;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    // ------------------------------------------------------
    // GET: listar servicios turísticos
    // ------------------------------------------------------
    @Test
    void testObtenerServiciosTuristicos() {
        List<ServicioTuristico> lista = List.of(new ServicioTuristico());
        when(servicioTuristicoService.getServicioTuristicos()).thenReturn(lista);

        ResponseEntity<List<ServicioTuristico>> response = controller.obtenerServiciosTuristicos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
    }

    // ------------------------------------------------------
    // GET: obtener por id
    // ------------------------------------------------------
    @Test
    void testObtenerServicioPorId() {
        ServicioTuristico st = new ServicioTuristico();
        st.setIdServicio(1L);

        when(servicioTuristicoService.getServicioTuristicoById(1L)).thenReturn(st);

        ResponseEntity<ServicioTuristico> response =
                controller.obtenerServicioTuristicoById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(st, response.getBody());
    }

    // ------------------------------------------------------
    // POST: crear servicio turístico
    // ------------------------------------------------------
    @Test
    void testCrearServicioTuristico() throws Exception {
        ServicioTuristicoDto dto = new ServicioTuristicoDto();
        dto.setNombre("Tour Mirador");

        ServicioTuristico creado = new ServicioTuristico();
        creado.setIdServicio(10L);
        creado.setNombre("Tour Mirador");

        String json = objectMapper.writeValueAsString(dto);

        when(servicioTuristicoService.postServicioTuristico(any(), any()))
                .thenReturn(creado);

        ResponseEntity<ServicioTuristico> response =
                controller.crearServicioTuristico(json, multipartFile);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(creado, response.getBody());
    }

    // ------------------------------------------------------
    // POST: error inesperado
    // ------------------------------------------------------
    @Test
    void testCrearServicioTuristico_Error() throws Exception {
        String json = "{\"nombre\":\"Cascada\"}";

        when(servicioTuristicoService.postServicioTuristico(any(), any()))
                .thenThrow(new RuntimeException("error"));

        ResponseEntity<ServicioTuristico> response =
                controller.crearServicioTuristico(json, multipartFile);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ------------------------------------------------------
    // PUT: actualizar servicio turístico
    // ------------------------------------------------------
    @Test
    void testActualizarServicioTuristico() throws Exception {
        ServicioTuristicoDto dto = new ServicioTuristicoDto();
        dto.setNombre("Nuevo Tour");

        ServicioTuristico actualizado = new ServicioTuristico();
        actualizado.setIdServicio(1L);
        actualizado.setNombre("Nuevo Tour");

        when(servicioTuristicoService.putServicioTuristico(eq(1L), any(), any()))
                .thenReturn(actualizado);

        String json = objectMapper.writeValueAsString(dto);

        ResponseEntity<ServicioTuristico> response =
                controller.actualizarServicioTuristico(1L, json, multipartFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(actualizado, response.getBody());
    }

    // ------------------------------------------------------
    // PUT: error inesperado
    // ------------------------------------------------------
    @Test
    void testActualizarServicioTuristico_Error() throws Exception {
        String json = "{\"nombre\":\"Falla\"}";

        when(servicioTuristicoService.putServicioTuristico(anyLong(), any(), any()))
                .thenThrow(new RuntimeException("error"));

        ResponseEntity<ServicioTuristico> response =
                controller.actualizarServicioTuristico(1L, json, multipartFile);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ------------------------------------------------------
    // DELETE: eliminar servicio turístico OK
    // ------------------------------------------------------
    @Test
    void testEliminarServicioTuristico() {
        ResponseEntity<String> response =
                controller.eliminarServicioTuristico(1L);

        verify(servicioTuristicoService).deleteServicioTuristico(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("ServicioTuristico eliminado", response.getBody());
    }

    // ------------------------------------------------------
    // DELETE: eliminar servicio turístico ERROR
    // ------------------------------------------------------
    @Test
    void testEliminarServicioTuristico_Error() {
        doThrow(new RuntimeException("error"))
                .when(servicioTuristicoService).deleteServicioTuristico(1L);

        ResponseEntity<String> response =
                controller.eliminarServicioTuristico(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al eliminar el servicioTuristico", response.getBody());
    }

    // ------------------------------------------------------
    // GET/buscar?nombre=
    // ------------------------------------------------------
    @Test
    void testBuscarPorNombre() {
        List<ServicioTuristico> lista = List.of(new ServicioTuristico());

        when(servicioTuristicoService.buscarServicioTuristicoPorNombre("tour"))
                .thenReturn(lista);

        ResponseEntity<List<ServicioTuristico>> response =
                controller.buscarPorNombre("tour");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
    }
}
