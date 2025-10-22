package pe.edu.upeu.turismospringboot.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.edu.upeu.turismospringboot.model.enums.EstadoCuenta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private EstadoCuenta estado;

    private LocalDateTime ultimaConexion;

    /* ======================
       🔗 Relaciones
       ====================== */

    // Relación con Rol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    @JsonBackReference(value = "rol-usuarios")
    private Rol rol;

    // Relación con Persona
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_persona", unique = true)
    @JsonManagedReference(value = "usuario-persona")
    private Persona persona;

    // Relación con BitacoraAcceso
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "usuario-bitacora")
    private List<BitacoraAcceso> bitacoraAccesoList = new ArrayList<>();

    // Relación con Noticia
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "usuario-noticia")
    private List<Noticia> noticias = new ArrayList<>();

    // Relación con Resena
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "usuario-resena")
    private List<Resena> resenas = new ArrayList<>();

    // Relación con Reserva
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "usuario-reserva")
    private List<Reserva> reservas = new ArrayList<>();

    // Relación con Emprendimiento
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_emprendimiento", unique = true)
    @JsonManagedReference(value = "usuario-emprendimiento")
    private Emprendimiento emprendimiento;

    /* ======================
       📅 Auditoría
       ====================== */
    private LocalDateTime fechaCreacionUsuario;
    private LocalDateTime fechaModificacionUsuario;

    @PrePersist
    public void onCreate() {
        fechaCreacionUsuario = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        fechaModificacionUsuario = LocalDateTime.now();
    }

    /* ======================
       🔐 Métodos de seguridad
       ====================== */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (rol == null || rol.getNombre() == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority(rol.getNombre()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
