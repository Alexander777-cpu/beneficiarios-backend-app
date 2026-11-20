package pe.upeu.beneficiarios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoResponseDTO {
    private Long id;
    private String nombre;
    private Long facultadId;
    private String facultadNombre;
}
