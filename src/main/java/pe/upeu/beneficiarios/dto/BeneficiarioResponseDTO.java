package pe.upeu.beneficiarios.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiarioResponseDTO {

    private Long id;
    private String tipoBeneficiario;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono;
    private Integer edad;
    private String gradoEstudios;
    private String estadoCivil;
    private String tutorNombre;
    private String tallaPolo;
    private String tallaPantalon;
    private Integer tallaCalzado;
    private Long proyectoTallerId;
    private String tallerNombre;
    private Long proyectoId;
    private String proyectoNombre;
    private Long facultadId;
    private String facultadNombre;
    private LocalDateTime fechaRegistro;
}
