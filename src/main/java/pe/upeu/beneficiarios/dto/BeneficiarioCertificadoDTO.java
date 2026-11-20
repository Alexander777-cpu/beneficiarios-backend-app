package pe.upeu.beneficiarios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiarioCertificadoDTO {

    private Long id;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tallerNombre;
    private String proyectoNombre;
    private String facultadNombre;
    private long totalAsistencias;
    private boolean aptoCertificado;

    public BeneficiarioCertificadoDTO(
            Long id,
            String nombres,
            String apellidoPaterno,
            String apellidoMaterno,
            String tallerNombre,
            String proyectoNombre,
            String facultadNombre,
            long totalAsistencias) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.tallerNombre = tallerNombre;
        this.proyectoNombre = proyectoNombre;
        this.facultadNombre = facultadNombre;
        this.totalAsistencias = totalAsistencias;
        this.aptoCertificado = totalAsistencias >= 5;
    }
}
