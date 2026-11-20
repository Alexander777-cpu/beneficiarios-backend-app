package pe.upeu.beneficiarios.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.dto.auth.LoginRequestDTO;
import pe.upeu.beneficiarios.dto.auth.LoginResponseDTO;
import pe.upeu.beneficiarios.dto.auth.UsuarioRequestDTO;
import pe.upeu.beneficiarios.dto.auth.UsuarioResponseDTO;
import pe.upeu.beneficiarios.entity.Facultad;
import pe.upeu.beneficiarios.entity.Proyecto;
import pe.upeu.beneficiarios.entity.security.Rol;
import pe.upeu.beneficiarios.entity.security.Usuario;
import pe.upeu.beneficiarios.exception.RecursoNoEncontradoException;
import pe.upeu.beneficiarios.exception.ReglaNegocioException;
import pe.upeu.beneficiarios.exception.ValidacionException;
import pe.upeu.beneficiarios.mapper.UsuarioMapper;
import pe.upeu.beneficiarios.repository.FacultadRepository;
import pe.upeu.beneficiarios.repository.ProyectoRepository;
import pe.upeu.beneficiarios.repository.RolRepository;
import pe.upeu.beneficiarios.repository.UsuarioRepository;
import pe.upeu.beneficiarios.security.JwtService;
import pe.upeu.beneficiarios.service.services.AuditoriaService;
import pe.upeu.beneficiarios.service.services.AuthService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final FacultadRepository facultadRepository;
    private final ProyectoRepository proyectoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final AuditoriaService auditoriaService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           UsuarioRepository usuarioRepository,
                           RolRepository rolRepository,
                           FacultadRepository facultadRepository,
                           ProyectoRepository proyectoRepository,
                           PasswordEncoder passwordEncoder,
                           UsuarioMapper usuarioMapper,
                           AuditoriaService auditoriaService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.facultadRepository = facultadRepository;
        this.proyectoRepository = proyectoRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {
        String usernameLimpio = request.getUsername() != null ? request.getUsername().trim() : "";
        log.info("Intento de login para usuario={}", usernameLimpio);

        Authentication autenticacion = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameLimpio, request.getPassword())
        );

        UserDetails principal = (UserDetails) autenticacion.getPrincipal();
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(principal.getUsername())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + usernameLimpio));

        String token = jwtService.generarToken(principal, usuario);
        List<String> roles = usuarioMapper.nombresRoles(usuario.getRoles());
        List<String> privilegios = usuarioMapper.nombresPrivilegios(usuario.getRoles());

        log.info("Login exitoso: usuario={} roles={}", usernameLimpio, roles);

        return LoginResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .expiraEnSegundos(jwtService.getExpiracionSegundos())
                .username(usuario.getUsername())
                .nombreCompleto(usuario.getNombreCompleto())
                .facultadId(usuario.getFacultad() != null ? usuario.getFacultad().getId() : null)
                .proyectoId(usuario.getProyecto() != null ? usuario.getProyecto().getId() : null)
                .roles(roles)
                .privilegios(privilegios)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO usuarioActual(String username) {
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + username));
        return usuarioMapper.toUsuarioResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<UsuarioResponseDTO> listarUsuarios(int pagina, int tamanio) {
        Page<UsuarioResponseDTO> page = usuarioRepository
                .findAll(PageRequest.of(pagina, tamanio, Sort.by("id").ascending()))
                .map(usuarioMapper::toUsuarioResponse);
        return PaginaResponseDTO.de(page);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {
        String username = request.getUsername().trim();
        if (usuarioRepository.existsByUsernameIgnoreCase(username)) {
            throw new ValidacionException("username", "El nombre de usuario ya existe");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ValidacionException("password", "La contrasena es obligatoria al crear un usuario");
        }

        Set<Rol> roles = resolverRoles(request.getRolIds());
        validarJerarquiaProgramador(roles);

        Facultad facultad = request.getFacultadId() != null
                ? facultadRepository.findById(request.getFacultadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + request.getFacultadId()))
                : null;

        Proyecto proyecto = request.getProyectoId() != null
                ? proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado con id: " + request.getProyectoId()))
                : null;

        Usuario usuario = Usuario.builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .nombreCompleto(request.getNombreCompleto() != null ? request.getNombreCompleto().trim() : username)
                .estado(request.getEstado() != null ? request.getEstado() : true)
                .facultad(facultad)
                .proyecto(proyecto)
                .roles(roles)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        auditoriaService.registrar("CREO", "USUARIO", "Se registro el usuario: " + guardado.getUsername());
        return usuarioMapper.toUsuarioResponse(guardado);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        Set<Rol> roles = resolverRoles(request.getRolIds());
        validarJerarquiaProgramador(roles);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        }
        if (request.getNombreCompleto() != null) {
            usuario.setNombreCompleto(request.getNombreCompleto().trim());
        }
        if (request.getEstado() != null) {
            usuario.setEstado(request.getEstado());
        }

        usuario.setFacultad(request.getFacultadId() != null
                ? facultadRepository.findById(request.getFacultadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada"))
                : null);

        usuario.setProyecto(request.getProyectoId() != null
                ? proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado"))
                : null);

        usuario.setRoles(roles);
        Usuario actualizado = usuarioRepository.save(usuario);
        auditoriaService.registrar("EDITO", "USUARIO", "Se actualizo el usuario: " + actualizado.getUsername());
        return usuarioMapper.toUsuarioResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        boolean esSuperAdmin = usuario.getRoles().stream()
                .anyMatch(r -> "SUPER_ADMIN".equalsIgnoreCase(r.getNombre()));
        if (esSuperAdmin) {
            throw new ReglaNegocioException("No se puede eliminar una cuenta con rol SUPER_ADMIN");
        }

        usuarioRepository.delete(usuario);
        auditoriaService.registrar("ELIMINO", "USUARIO", "Se elimino el usuario: " + usuario.getUsername());
    }

    private Set<Rol> resolverRoles(Set<Long> rolIds) {
        Set<Rol> roles = new HashSet<>();
        for (Long rolId : rolIds) {
            Rol rol = rolRepository.findById(rolId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con id: " + rolId));
            roles.add(rol);
        }
        return roles;
    }

    private void validarJerarquiaProgramador(Set<Rol> roles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean esProgramador = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_PROGRAMADOR".equals(a.getAuthority()));
        boolean incluyeAdmin = roles.stream()
                .anyMatch(r -> "SUPER_ADMIN".equalsIgnoreCase(r.getNombre()));
        if (esProgramador && incluyeAdmin) {
            throw new ReglaNegocioException("Un PROGRAMADOR no tiene permisos para asignar el rol SUPER_ADMIN");
        }
    }
}
