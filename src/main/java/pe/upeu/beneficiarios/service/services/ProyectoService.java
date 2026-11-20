package pe.upeu.beneficiarios.service.services;

import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.dto.ProyectoRequestDTO;
import pe.upeu.beneficiarios.dto.ProyectoResponseDTO;

public interface ProyectoService {
    PaginaResponseDTO<ProyectoResponseDTO> listar(Long facultadId, int pagina, int tamanio, String ordenarPor, String direccion);
    ProyectoResponseDTO buscar(Long id);
    ProyectoResponseDTO crear(ProyectoRequestDTO request);
    ProyectoResponseDTO actualizar(Long id, ProyectoRequestDTO request);
    void eliminar(Long id);
}
