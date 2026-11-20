package pe.upeu.beneficiarios.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "AUDITORIAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "USUARIO_ACCION", nullable = false, length = 50)
    private String usuarioAccion;

    @NotBlank
    @Size(max = 50)
    @Column(name = "ACCION", nullable = false, length = 50)
    private String accion;

    @NotBlank
    @Size(max = 50)
    @Column(name = "MODULO", nullable = false, length = 50)
    private String modulo;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "DESCRIPCION", nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "FECHA", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }
}
