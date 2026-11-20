package pe.upeu.beneficiarios.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiarioRequestDTO {

    @NotBlank(message = "El tipo de beneficiario es obligatorio (NINO, ADOLESCENTE, ADULTO)")
    @Size(max = 50, message = "El tipo de beneficiario no debe exceder 50 caracteres")
    private String tipoBeneficiario;

    @NotBlank(message = "El campo nombres es obligatorio")
    @Size(min = 2, max = 100, message = "Los nombres deben tener entre 2 y 100 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido paterno debe tener entre 2 y 100 caracteres")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido materno debe tener entre 2 y 100 caracteres")
    private String apellidoMaterno;

    @Pattern(regexp = "^$|^[0-9]{9}$", message = "El telefono debe contener exactamente 9 digitos numericos")
    private String telefono;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad no puede superar 120 anos")
    private Integer edad;

    @Size(max = 100, message = "El grado de estudios no debe exceder 100 caracteres")
    private String gradoEstudios;

    @Size(max = 50, message = "El estado civil no debe exceder 50 caracteres")
    private String estadoCivil;

    @Size(max = 150, message = "El nombre del tutor no debe exceder 150 caracteres")
    private String tutorNombre;

    @Size(max = 10, message = "La talla de polo no debe exceder 10 caracteres")
    private String tallaPolo;

    @Size(max = 10, message = "La talla de pantalon no debe exceder 10 caracteres")
    private String tallaPantalon;

    @Min(value = 15, message = "La talla de calzado minima es 15")
    @Max(value = 55, message = "La talla de calzado maxima es 55")
    private Integer tallaCalzado;

    @NotNull(message = "El id de la asignacion Proyecto-Taller es obligatorio")
    private Long proyectoTallerId;
}
