package pe.upeu.beneficiarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upeu.beneficiarios.dto.BeneficiarioRequestDTO;
import pe.upeu.beneficiarios.dto.BeneficiarioResponseDTO;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.service.services.BeneficiarioService;

@Tag(name = "Beneficiarios", description = "CRUD completo y busqueda paginada de beneficiarios")
@RestController
@RequestMapping("/api/v1/beneficiarios")
public class BeneficiarioController {

    private final BeneficiarioService beneficiarioService;

    public BeneficiarioController(BeneficiarioService beneficiarioService) {
        this.beneficiarioService = beneficiarioService;
    }

    @Operation(summary = "Listar beneficiarios paginados con filtros")
    @GetMapping
    public ResponseEntity<PaginaResponseDTO<BeneficiarioResponseDTO>> listar(
            @RequestParam(required = false) Long proyectoTallerId,
            @RequestParam(required = false) Long proyectoId,
            @RequestParam(required = false) Long facultadId,
            @RequestParam(required = false) String buscar,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanio,
            @RequestParam(defaultValue = "id") String ordenarPor,
            @RequestParam(defaultValue = "desc") String direccion) {

        return ResponseEntity.ok(beneficiarioService.listar(
                proyectoTallerId, proyectoId, facultadId, buscar, pagina, tamanio, ordenarPor, direccion));
    }

    @Operation(summary = "Buscar beneficiario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<BeneficiarioResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiarioService.buscar(id));
    }

    @Operation(summary = "Registrar nuevo beneficiario")
    @PostMapping
    public ResponseEntity<BeneficiarioResponseDTO> crear(@Valid @RequestBody BeneficiarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiarioService.crear(request));
    }

    @Operation(summary = "Actualizar beneficiario")
    @PutMapping("/{id}")
    public ResponseEntity<BeneficiarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody BeneficiarioRequestDTO request) {
        return ResponseEntity.ok(beneficiarioService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar beneficiario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        beneficiarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
