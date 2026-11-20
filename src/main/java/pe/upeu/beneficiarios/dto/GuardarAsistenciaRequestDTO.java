package pe.upeu.beneficiarios.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuardarAsistenciaRequestDTO {

    @NotNull(message = "El id de Proyecto-Taller es obligatorio")
    private Long proyectoTallerId;

    @NotBlank(message = "La fecha es obligatoria")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "La fecha debe tener formato YYYY-MM-DD")
    private String fecha;

    @NotEmpty(message = "Debe enviar al menos un detalle de asistencia")
    @Valid
    private List<DetalleAsistenciaDTO> detalles;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetalleAsistenciaDTO {
        @NotNull(message = "El id del beneficiario es obligatorio")
        private Long beneficiarioId;

        @NotNull(message = "El estado de asistencia es obligatorio")
        private Boolean asistio;
    }
}
