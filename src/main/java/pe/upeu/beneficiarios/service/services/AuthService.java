package pe.upeu.beneficiarios.service.services;

import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.dto.auth.LoginRequestDTO;
import pe.upeu.beneficiarios.dto.auth.LoginResponseDTO;
import pe.upeu.beneficiarios.dto.auth.UsuarioRequestDTO;
import pe.upeu.beneficiarios.dto.auth.UsuarioResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);
    UsuarioResponseDTO usuarioActual(String username);
    PaginaResponseDTO<UsuarioResponseDTO> listarUsuarios(int pagina, int tamanio);
    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request);
    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request);
    void eliminarUsuario(Long id);
}
