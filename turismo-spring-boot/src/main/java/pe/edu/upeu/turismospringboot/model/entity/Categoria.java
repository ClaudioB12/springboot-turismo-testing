package pe.edu.upeu.turismospringboot.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@DynamicUpdate  // 🔥 fuerza a Hibernate a disparar @PreUpdate correctamente
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String imagenUrl;

    @JsonManagedReference(value = "categoria-familiaCategoria")
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FamiliaCategoria> familiaCategorias;

    private LocalDateTime fechaCreacionCategoria;
    private LocalDateTime fechaModificacionCategoria;

    @PrePersist
    public void onCreate() {
        fechaCreacionCategoria = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        fechaModificacionCategoria = LocalDateTime.now();
    }
}
