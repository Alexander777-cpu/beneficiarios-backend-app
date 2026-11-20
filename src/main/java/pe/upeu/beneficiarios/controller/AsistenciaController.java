package pe.upeu.beneficiarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upeu.beneficiarios.dto.AsistenciaItemDTO;
import pe.upeu.beneficiarios.dto.GuardarAsistenciaRequestDTO;
import pe.upeu.beneficiarios.service.services.AsistenciaService;

import java.util.List;

@Tag(name = "Asistencias", description = "Planilla diaria y reporte consolidado de asistencias")
@RestController
@RequestMapping("/api/v1/asistencias")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @Operation(summary = "Obtener planilla diaria por asignacion Proyecto-Taller")
    @GetMapping("/planilla/{proyectoTallerId}")
    public ResponseEntity<List<AsistenciaItemDTO>> obtenerPlanilla(
            @PathVariable Long proyectoTallerId,
            @RequestParam(required = false) String fecha) {
        return ResponseEntity.ok(asistenciaService.obtenerPlanilla(proyectoTallerId, fecha));
    }

    @Operation(summary = "Reporte consolidado de asistencias acumuladas")
    @GetMapping("/reporte-general")
    public ResponseEntity<List<AsistenciaItemDTO>> obtenerReporteGeneral(
            @RequestParam(required = false) Long facultadId,
            @RequestParam(required = false) Long proyectoId) {
        return ResponseEntity.ok(asistenciaService.obtenerReporteGeneral(facultadId, proyectoId));
    }

    @Operation(summary = "Guardar o actualizar planilla de asistencias en lote")
    @PostMapping("/guardar")
    public ResponseEntity<Void> guardar(@Valid @RequestBody GuardarAsistenciaRequestDTO request) {
        asistenciaService.guardar(request);
        return ResponseEntity.ok().build();
    }
}
