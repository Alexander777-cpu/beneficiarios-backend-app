package pe.upeu.beneficiarios.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upeu.beneficiarios.entity.security.Privilegio;
import pe.upeu.beneficiarios.entity.security.Rol;
import pe.upeu.beneficiarios.entity.security.Usuario;
import pe.upeu.beneficiarios.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository
                .findByUsernameIgnoreCase(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (usuario.getRoles() != null) {
            for (Rol rol : usuario.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
                if (rol.getPrivilegios() != null) {
                    for (Privilegio priv : rol.getPrivilegios()) {
                        authorities.add(new SimpleGrantedAuthority(priv.getNombre()));
                    }
                }
            }
        }

        String passwordLimpia = usuario.getPassword() != null ? usuario.getPassword().trim() : "";

        return User.withUsername(usuario.getUsername())
                .password(passwordLimpia)
                .authorities(authorities)
                .disabled(!Boolean.TRUE.equals(usuario.getEstado()))
                .build();
    }
}
