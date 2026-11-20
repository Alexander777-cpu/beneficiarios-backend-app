package pe.upeu.beneficiarios.dto.auth;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String token;
    @Builder.Default
    private String tipo = "Bearer";
    private long expiraEnSegundos;
    private String username;
    private String nombreCompleto;
    private Long facultadId;
    private Long proyectoId;
    private List<String> roles;
    private List<String> privilegios;
}
