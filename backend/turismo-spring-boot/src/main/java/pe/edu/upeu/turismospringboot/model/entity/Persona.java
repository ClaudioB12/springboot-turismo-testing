package pe.edu.upeu.turismospringboot.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersona;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(length = 100)
    private String apellidos;

    @Column(length = 20)
    private String tipoDocumento;

    @Column(length = 20, unique = true)
    private String numeroDocumento;

    @Column(length = 15)
    private String telefono;

    @Column(length = 200)
    private String direccion;

    @Column(length = 100)
    private String correoElectronico;

    private String fotoPerfil;

    private LocalDate fechaNacimiento;

    @OneToOne(mappedBy = "persona")
    @JsonBackReference(value = "usuario-persona") // 👈 clave para romper ciclo Jackson
    private Usuario usuario;

    private LocalDateTime fechaCreacionPersona;
    private LocalDateTime fechaModificacionPersona;

    @PrePersist
    public void onCreate() {
        fechaCreacionPersona = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        fechaModificacionPersona = LocalDateTime.now();
    }
}
