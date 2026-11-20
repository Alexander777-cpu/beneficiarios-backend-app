package pe.upeu.beneficiarios.service.services;

import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.dto.TallerRequestDTO;
import pe.upeu.beneficiarios.dto.TallerResponseDTO;

public interface TallerService {
    PaginaResponseDTO<TallerResponseDTO> listar(Long proyectoId, Long facultadId, int pagina, int tamanio, String ordenarPor, String direccion);
    TallerResponseDTO buscar(Long id);
    TallerResponseDTO crear(TallerRequestDTO request);
    TallerResponseDTO actualizar(Long id, TallerRequestDTO request);
    void eliminar(Long id);
}
