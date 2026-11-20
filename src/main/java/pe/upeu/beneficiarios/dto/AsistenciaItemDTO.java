package pe.upeu.beneficiarios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaItemDTO {
    private Long beneficiarioId;
    private String nombres;
    private String apellidos;
    private String tipoBeneficiario;
    private Boolean asistio;
    private Long totalAsistenciasHistoricas;
    private String tallerNombre;
    private String proyectoNombre;
    private String facultadNombre;
}
