package pe.upeu.beneficiarios.service.services;

import pe.upeu.beneficiarios.dto.AuditoriaResponseDTO;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;

public interface AuditoriaService {
    void registrar(String accion, String modulo, String descripcion);
    PaginaResponseDTO<AuditoriaResponseDTO> listar(int pagina, int tamanio);
}
