package pe.upeu.beneficiarios.dto.auth;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String username;
    private String nombreCompleto;
    private Boolean estado;
    private Long facultadId;
    private String facultadNombre;
    private Long proyectoId;
    private String proyectoNombre;
    private LocalDateTime fechaCreacion;
    private List<String> roles;
    private List<String> privilegios;
}
