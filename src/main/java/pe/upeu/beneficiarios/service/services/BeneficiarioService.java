package pe.upeu.beneficiarios.service.services;

import pe.upeu.beneficiarios.dto.BeneficiarioRequestDTO;
import pe.upeu.beneficiarios.dto.BeneficiarioResponseDTO;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;

public interface BeneficiarioService {
    PaginaResponseDTO<BeneficiarioResponseDTO> listar(
            Long proyectoTallerId,
            Long proyectoId,
            Long facultadId,
            String buscar,
            int pagina,
            int tamanio,
            String ordenarPor,
            String direccion);

    BeneficiarioResponseDTO buscar(Long id);
    BeneficiarioResponseDTO crear(BeneficiarioRequestDTO request);
    BeneficiarioResponseDTO actualizar(Long id, BeneficiarioRequestDTO request);
    void eliminar(Long id);
}
