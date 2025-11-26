package pe.edu.upeu.turismospringboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.CrearReservaDetalleRequest;
import pe.edu.upeu.turismospringboot.model.dto.CrearReservaRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrearReservaRequestTest {


    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @Test
    void debeCrearReservaRequestConGettersYSetters() {
        CrearReservaRequest request = new CrearReservaRequest();
        request.setIdEmprendimiento(10L);
        request.setFechaHoraInicio(LocalDateTime.of(2025, 1, 10, 12, 0));
        request.setFechaHoraFin(LocalDateTime.of(2025, 1, 10, 14, 0));
        request.setTotalGeneral(150.50);

        CrearReservaDetalleRequest detalle = new CrearReservaDetalleRequest();
        detalle.setIdServicioTuristico(5L);
        detalle.setCantidad(3);
        detalle.setObservaciones("Test detalle");

        request.setDetalles(List.of(detalle));

        assertEquals(10L, request.getIdEmprendimiento());
        assertEquals(150.50, request.getTotalGeneral());
        assertEquals(1, request.getDetalles().size());
        assertEquals(5L, request.getDetalles().get(0).getIdServicioTuristico());
    }

    @Test
    void debeConvertirJsonAReservaRequest() throws Exception {
        String json = """
                {
                  "idEmprendimiento": 25,
                  "fechaHoraInicio": "2025-04-10T09:00:00",
                  "fechaHoraFin": "2025-04-10T11:00:00",
                  "totalGeneral": 300.0,
                  "detalles": [
                    {
                      "idServicioTuristico": 2,
                      "cantidad": 1,
                      "observaciones": "Nota"
                    }
                  ]
                }
                """;

        CrearReservaRequest request = objectMapper.readValue(json, CrearReservaRequest.class);

        assertNotNull(request);
        assertEquals(25L, request.getIdEmprendimiento());
        assertEquals(300.0, request.getTotalGeneral());
        assertEquals(1, request.getDetalles().size());
        assertEquals(2L, request.getDetalles().get(0).getIdServicioTuristico());
    }

    @Test
    void debeConvertirReservaRequestAJson() throws Exception {
        CrearReservaDetalleRequest detalle = new CrearReservaDetalleRequest();
        detalle.setIdServicioTuristico(9L);
        detalle.setCantidad(2);
        detalle.setObservaciones("Sin observaciones");

        CrearReservaRequest request = new CrearReservaRequest();
        request.setIdEmprendimiento(88L);
        request.setFechaHoraInicio(LocalDateTime.of(2025, 6, 1, 10, 0));
        request.setFechaHoraFin(LocalDateTime.of(2025, 6, 1, 12, 0));
        request.setTotalGeneral(500.0);
        request.setDetalles(List.of(detalle));

        String json = objectMapper.writeValueAsString(request);

        assertTrue(json.contains("\"idEmprendimiento\":88"));
        assertTrue(json.contains("\"totalGeneral\":500.0"));
        assertTrue(json.contains("\"idServicioTuristico\":9"));
    }

    @Test
    void debePermitirListaVaciaDeDetalles() {
        CrearReservaRequest request = new CrearReservaRequest();
        request.setDetalles(List.of());

        assertNotNull(request.getDetalles());
        assertTrue(request.getDetalles().isEmpty());
    }

    @Test
    void debeValidarMultiplesDetalles() {
        CrearReservaDetalleRequest d1 = new CrearReservaDetalleRequest();
        d1.setIdServicioTuristico(1L);
        d1.setCantidad(2);

        CrearReservaDetalleRequest d2 = new CrearReservaDetalleRequest();
        d2.setIdServicioTuristico(2L);
        d2.setCantidad(1);

        CrearReservaRequest request = new CrearReservaRequest();
        request.setDetalles(List.of(d1, d2));

        assertEquals(2, request.getDetalles().size());
        assertEquals(1L, request.getDetalles().get(0).getIdServicioTuristico());
        assertEquals(2L, request.getDetalles().get(1).getIdServicioTuristico());
    }
}
