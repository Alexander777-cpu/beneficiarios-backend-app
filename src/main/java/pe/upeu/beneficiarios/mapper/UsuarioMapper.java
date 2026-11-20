package pe.upeu.beneficiarios.mapper;

import org.springframework.stereotype.Component;
import pe.upeu.beneficiarios.dto.auth.UsuarioResponseDTO;
import pe.upeu.beneficiarios.entity.security.Privilegio;
import pe.upeu.beneficiarios.entity.security.Rol;
import pe.upeu.beneficiarios.entity.security.Usuario;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

@Component
public class UsuarioMapper {

    public UsuarioResponseDTO toUsuarioResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(usuario.getNombreCompleto())
                .estado(usuario.getEstado())
                .facultadId(usuario.getFacultad() != null ? usuario.getFacultad().getId() : null)
                .facultadNombre(usuario.getFacultad() != null ? usuario.getFacultad().getNombre() : null)
                .proyectoId(usuario.getProyecto() != null ? usuario.getProyecto().getId() : null)
                .proyectoNombre(usuario.getProyecto() != null ? usuario.getProyecto().getNombre() : null)
                .fechaCreacion(usuario.getFechaCreacion())
                .roles(nombresRoles(usuario.getRoles()))
                .privilegios(nombresPrivilegios(usuario.getRoles()))
                .build();
    }

    public List<String> nombresRoles(Collection<Rol> roles) {
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .map(Rol::getNombre)
                .sorted()
                .toList();
    }

    public List<String> nombresPrivilegios(Collection<Rol> roles) {
        if (roles == null) {
            return List.of();
        }
        TreeSet<String> privilegios = new TreeSet<>();
        for (Rol rol : roles) {
            if (rol.getPrivilegios() != null) {
                for (Privilegio priv : rol.getPrivilegios()) {
                    privilegios.add(priv.getNombre());
                }
            }
        }
        return privilegios.stream().toList();
    }
}
