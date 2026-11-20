package pe.upeu.beneficiarios.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "BENEFICIARIOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "TIPO_BENEFICIARIO", nullable = false, length = 50)
    private String tipoBeneficiario;

    @NotBlank
    @Size(max = 100)
    @Column(name = "NOMBRES", nullable = false, unique = true, length = 100)
    private String nombres;

    @NotBlank
    @Size(max = 100)
    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 100)
    private String apellidoPaterno;

    @NotBlank
    @Size(max = 100)
    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 100)
    private String apellidoMaterno;

    @Size(max = 15)
    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    @Column(name = "EDAD")
    private Integer edad;

    @Size(max = 100)
    @Column(name = "GRADO_ESTUDIOS", length = 100)
    private String gradoEstudios;

    @Size(max = 50)
    @Column(name = "ESTADO_CIVIL", length = 50)
    private String estadoCivil;

    @Size(max = 150)
    @Column(name = "TUTOR_NOMBRE", length = 150)
    private String tutorNombre;

    @Size(max = 10)
    @Column(name = "TALLA_POLO", length = 10)
    private String tallaPolo;

    @Size(max = 10)
    @Column(name = "TALLA_PANTALON", length = 10)
    private String tallaPantalon;

    @Column(name = "TALLA_CALZADO")
    private Integer tallaCalzado;

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
