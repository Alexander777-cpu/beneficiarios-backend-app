package pe.upeu.beneficiarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.dto.ProyectoRequestDTO;
import pe.upeu.beneficiarios.dto.ProyectoResponseDTO;
import pe.upeu.beneficiarios.service.services.ProyectoService;

@Tag(name = "Proyectos", description = "Gestion de proyectos sociales por facultad")
@RestController
@RequestMapping("/api/v1/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @Operation(summary = "Listar proyectos paginados con filtro opcional por facultad")
    @GetMapping
    public ResponseEntity<PaginaResponseDTO<ProyectoResponseDTO>> listar(
            @RequestParam(required = false) Long facultadId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanio,
            @RequestParam(defaultValue = "id") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direccion) {
        return ResponseEntity.ok(proyectoService.listar(facultadId, pagina, tamanio, ordenarPor, direccion));
    }

    @Operation(summary = "Buscar proyecto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.buscar(id));
    }

    @Operation(summary = "Crear proyecto")
    @PostMapping
    public ResponseEntity<ProyectoResponseDTO> crear(@Valid @RequestBody ProyectoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoService.crear(request));
    }

    @Operation(summary = "Actualizar proyecto")
    @PutMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProyectoRequestDTO request) {
        return ResponseEntity.ok(proyectoService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar proyecto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
