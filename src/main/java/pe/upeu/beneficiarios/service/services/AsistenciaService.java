package pe.upeu.beneficiarios.service.services;

import pe.upeu.beneficiarios.dto.AsistenciaItemDTO;
import pe.upeu.beneficiarios.dto.GuardarAsistenciaRequestDTO;

import java.util.List;

public interface AsistenciaService {
    List<AsistenciaItemDTO> obtenerPlanilla(Long proyectoTallerId, String fecha);
    List<AsistenciaItemDTO> obtenerReporteGeneral(Long facultadId, Long proyectoId);
    void guardar(GuardarAsistenciaRequestDTO request);
}
