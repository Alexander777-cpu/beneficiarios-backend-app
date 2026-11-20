package pe.upeu.beneficiarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TallerRequestDTO {

    @NotBlank(message = "El nombre del taller es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre del taller debe tener entre 3 y 150 caracteres")
    private String nombre;

    @NotNull(message = "El id del proyecto es obligatorio")
    private Long proyectoId;
}
