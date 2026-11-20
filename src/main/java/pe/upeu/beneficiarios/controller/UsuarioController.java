package pe.upeu.beneficiarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.dto.auth.UsuarioRequestDTO;
import pe.upeu.beneficiarios.dto.auth.UsuarioResponseDTO;
import pe.upeu.beneficiarios.service.services.AuthService;

@Tag(name = "Usuarios", description = "Gestion de usuarios y asignacion de roles/sedes")
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final AuthService authService;

    public UsuarioController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Perfil del usuario actual")
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> me(Authentication authentication) {
        return ResponseEntity.ok(authService.usuarioActual(authentication.getName()));
    }

    @Operation(summary = "Listar usuarios paginados")
    @GetMapping
    public ResponseEntity<PaginaResponseDTO<UsuarioResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanio) {
        return ResponseEntity.ok(authService.listarUsuarios(pagina, tamanio));
    }

    @Operation(summary = "Crear nuevo usuario")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.crearUsuario(request));
    }

    @Operation(summary = "Actualizar usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(authService.actualizarUsuario(id, request));
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        authService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
