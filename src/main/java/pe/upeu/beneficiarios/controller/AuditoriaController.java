package pe.upeu.beneficiarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.upeu.beneficiarios.dto.AuditoriaResponseDTO;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.service.services.AuditoriaService;

@Tag(name = "Auditoria", description = "Bitacora inmutable de operaciones del sistema")
@RestController
@RequestMapping("/api/v1/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @Operation(summary = "Listar eventos de auditoria paginados")
    @GetMapping
    public ResponseEntity<PaginaResponseDTO<AuditoriaResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanio) {
        return ResponseEntity.ok(auditoriaService.listar(pagina, tamanio));
    }
}
