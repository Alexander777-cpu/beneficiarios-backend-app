package pe.upeu.beneficiarios.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaResponseDTO {
    private Long id;
    private String usuarioAccion;
    private String accion;
    private String modulo;
    private String descripcion;
    private LocalDateTime fecha;
}
