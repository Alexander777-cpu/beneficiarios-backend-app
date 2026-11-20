package pe.upeu.beneficiarios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TallerResponseDTO {
    private Long id;
    private Long proyectoTallerId;
    private String nombre;
    private Long proyectoId;
    private String proyectoNombre;
    private Long facultadId;
    private String facultadNombre;
}
