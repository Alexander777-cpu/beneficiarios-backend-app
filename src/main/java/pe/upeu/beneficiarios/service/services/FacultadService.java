package pe.upeu.beneficiarios.service.services;

import pe.upeu.beneficiarios.dto.FacultadRequestDTO;
import pe.upeu.beneficiarios.dto.FacultadResponseDTO;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;

public interface FacultadService {
    PaginaResponseDTO<FacultadResponseDTO> listar(int pagina, int tamanio, String ordenarPor, String direccion);
    FacultadResponseDTO buscar(Long id);
    FacultadResponseDTO crear(FacultadRequestDTO request);
    FacultadResponseDTO actualizar(Long id, FacultadRequestDTO request);
    void eliminar(Long id);
}
