package pe.upeu.beneficiarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@Tag(name = "Health", description = "Verificacion de disponibilidad del servicio")
@RestController
@RequestMapping("/api")
public class HealthController {

    @Operation(summary = "Estado del servicio")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "beneficiarios-api",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
