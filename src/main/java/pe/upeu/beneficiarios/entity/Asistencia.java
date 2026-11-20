package pe.upeu.beneficiarios.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ASISTENCIAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 10)
    @Column(name = "FECHA", nullable = false, length = 10)
    private String fecha;

    @NotNull
    @Column(name = "ASISTIO", nullable = false)
    @Builder.Default
    private Boolean asistio = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BENEFICIARIO_ID", nullable = false)
    private Beneficiario beneficiario;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROYECTO_TALLER_ID", nullable = false)
    private ProyectoTaller proyectoTaller;

    @Column(name = "FECHA_REGISTRO", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
    }
}
