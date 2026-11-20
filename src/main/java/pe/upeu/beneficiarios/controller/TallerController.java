package pe.upeu.beneficiarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.dto.TallerRequestDTO;
import pe.upeu.beneficiarios.dto.TallerResponseDTO;
import pe.upeu.beneficiarios.service.services.TallerService;

@Tag(name = "Talleres", description = "Asignacion de talleres por proyecto")
@RestController
@RequestMapping("/api/v1/talleres")
public class TallerController {

    private final TallerService tallerService;

    public TallerController(TallerService tallerService) {
        this.tallerService = tallerService;
    }

    @Operation(summary = "Listar talleres paginados con filtros opcionales")
    @GetMapping
    public ResponseEntity<PaginaResponseDTO<TallerResponseDTO>> listar(
            @RequestParam(required = false) Long proyectoId,
            @RequestParam(required = false) Long facultadId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanio,
            @RequestParam(defaultValue = "id") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direccion) {
        return ResponseEntity.ok(tallerService.listar(proyectoId, facultadId, pagina, tamanio, ordenarPor, direccion));
    }

    @Operation(summary = "Buscar taller asignado por ID")
    @GetMapping("/{id}")
    public ResponseEntity<TallerResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(tallerService.buscar(id));
    }

    @Operation(summary = "Crear y asignar taller a un proyecto")
    @PostMapping
    public ResponseEntity<TallerResponseDTO> crear(@Valid @RequestBody TallerRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tallerService.crear(request));
    }

    @Operation(summary = "Actualizar taller asignado")
    @PutMapping("/{id}")
    public ResponseEntity<TallerResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TallerRequestDTO request) {
        return ResponseEntity.ok(tallerService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar asignacion de taller")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tallerService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
