package pe.upeu.beneficiarios.dto.auth;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "El usuario solo admite letras, numeros, punto, guion y guion bajo"
    )
    private String username;

    @Size(min = 8, max = 72, message = "La contrasena debe tener entre 8 y 72 caracteres")
    private String password;

    @Size(max = 120, message = "El nombre completo no puede exceder 120 caracteres")
    private String nombreCompleto;

    private Boolean estado;
    private Long facultadId;
    private Long proyectoId;

    @NotEmpty(message = "Debe asignar al menos un rol")
    private Set<@NotNull(message = "El id de rol no puede ser nulo") Long> rolIds;
}
