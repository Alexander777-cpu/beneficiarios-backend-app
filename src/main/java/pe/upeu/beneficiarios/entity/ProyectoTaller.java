package pe.upeu.beneficiarios.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "PROYECTO_TALLERES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoTaller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROYECTO_ID", nullable = false)
    private Proyecto proyecto;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TALLER_ID", nullable = false)
    private Taller taller;
}
