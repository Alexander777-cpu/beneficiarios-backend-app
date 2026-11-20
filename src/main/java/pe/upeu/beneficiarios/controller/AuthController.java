package pe.upeu.beneficiarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.upeu.beneficiarios.dto.auth.LoginRequestDTO;
import pe.upeu.beneficiarios.dto.auth.LoginResponseDTO;
import pe.upeu.beneficiarios.dto.auth.UsuarioResponseDTO;
import pe.upeu.beneficiarios.service.services.AuthService;

@Tag(name = "Autenticacion", description = "Inicio de sesion y perfil autenticado JWT")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Iniciar sesion y obtener token JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Consultar datos del usuario autenticado")
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> me(Authentication authentication) {
        return ResponseEntity.ok(authService.usuarioActual(authentication.getName()));
    }
}
