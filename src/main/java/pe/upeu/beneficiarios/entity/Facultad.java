package pe.upeu.beneficiarios.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "FACULTADES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facultad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "NOMBRE", nullable = false, unique = true, length = 100)
    private String nombre;
}
