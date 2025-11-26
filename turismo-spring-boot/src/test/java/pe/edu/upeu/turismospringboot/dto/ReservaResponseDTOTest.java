package pe.edu.upeu.turismospringboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.turismospringboot.model.dto.ReservaResponseDTO;
import pe.edu.upeu.turismospringboot.model.entity.*;
import pe.edu.upeu.turismospringboot.model.enums.EstadoReserva;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservaResponseDTOTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // 🔥 Registrar módulo para LocalDateTime
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void debeCrearDTOCorrectamenteDesdeEntidad() {
        // ======= Preparar objetos relacionados =======
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(5L);
        usuario.setUsername("testUser");

        Persona persona = new Persona();
        persona.setNombres("Juan");
        persona.setApellidos("Perez");
        usuario.setPersona(persona);

        Rol rol = new Rol();
        rol.setNombre("USUARIO");
        usuario.setRol(rol);

        Emprendimiento emp = new Emprendimiento();
        emp.setIdEmprendimiento(10L);

        // ======= Crear Reserva =======
        Reserva reserva = new Reserva();
        reserva.setIdReserva(1L);
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        reserva.setUsuario(usuario);
        reserva.setEmprendimiento(emp);
        reserva.setFechaHoraInicio(LocalDateTime.now());
        reserva.setFechaHoraFin(LocalDateTime.now().plusHours(2));
        reserva.setFechaHoraReserva(LocalDateTime.now());

        // ======= Crear DTO =======
        ReservaResponseDTO dto = new ReservaResponseDTO(reserva);

        // ======= Validaciones =======
        assertEquals(1L, dto.getIdReserva());
        assertEquals("CONFIRMADA", dto.getEstado());
        assertEquals(5L, dto.getUsuario().getIdUsuario());
        assertEquals("testUser", dto.getUsuario().getUsername());
        assertEquals("Juan Perez", dto.getUsuario().getNombrePersona());
        assertEquals("USUARIO", dto.getUsuario().getRolNombre());
        assertEquals(10L, dto.getIdEmprendimiento());
    }


    @Test
    void debeSerializarAJsonCorrectamente() throws Exception {
        // ======= Preparar objetos =======
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(5L);
        usuario.setUsername("userx");

        Persona persona = new Persona();
        persona.setNombres("Ana");
        persona.setApellidos("Lopez");
        usuario.setPersona(persona);

        Rol rol = new Rol();
        rol.setNombre("USUARIO");
        usuario.setRol(rol);

        Emprendimiento emp = new Emprendimiento();
        emp.setIdEmprendimiento(22L);

        Reserva reserva = new Reserva();
        reserva.setIdReserva(99L);
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setUsuario(usuario);
        reserva.setEmprendimiento(emp);
        reserva.setFechaHoraInicio(LocalDateTime.now());
        reserva.setFechaHoraFin(LocalDateTime.now().plusHours(1));
        reserva.setFechaHoraReserva(LocalDateTime.now());

        ReservaResponseDTO dto = new ReservaResponseDTO(reserva);

        // ======= Serializar =======
        String json = objectMapper.writeValueAsString(dto);

        // ======= Verificar =======
        assertTrue(json.contains("\"idReserva\":99"));
        assertTrue(json.contains("\"estado\":\"PENDIENTE\""));
        assertTrue(json.contains("\"idEmprendimiento\":22"));
        assertTrue(json.contains("\"username\":\"userx\""));
        assertTrue(json.contains("\"nombrePersona\":\"Ana Lopez\""));
    }
}
