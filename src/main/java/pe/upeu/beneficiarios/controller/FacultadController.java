package pe.upeu.beneficiarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upeu.beneficiarios.dto.FacultadRequestDTO;
import pe.upeu.beneficiarios.dto.FacultadResponseDTO;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.service.services.FacultadService;

@Tag(name = "Facultades", description = "Catalogo de facultades institucionales")
@RestController
@RequestMapping("/api/v1/facultades")
public class FacultadController {

    private final FacultadService facultadService;

    public FacultadController(FacultadService facultadService) {
        this.facultadService = facultadService;
    }

    @Operation(summary = "Listar facultades paginadas")
    @GetMapping
    public ResponseEntity<PaginaResponseDTO<FacultadResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanio,
            @RequestParam(defaultValue = "id") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direccion) {
        return ResponseEntity.ok(facultadService.listar(pagina, tamanio, ordenarPor, direccion));
    }

    @Operation(summary = "Buscar facultad por ID")
    @GetMapping("/{id}")
    public ResponseEntity<FacultadResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(facultadService.buscar(id));
    }

    @Operation(summary = "Registrar facultad")
    @PostMapping
    public ResponseEntity<FacultadResponseDTO> crear(@Valid @RequestBody FacultadRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facultadService.crear(request));
    }

    @Operation(summary = "Actualizar facultad")
    @PutMapping("/{id}")
    public ResponseEntity<FacultadResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FacultadRequestDTO request) {
        return ResponseEntity.ok(facultadService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar facultad")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facultadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
