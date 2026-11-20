package pe.upeu.beneficiarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upeu.beneficiarios.dto.BeneficiarioCertificadoDTO;
import pe.upeu.beneficiarios.service.services.CertificadoService;

import java.util.List;

@Tag(name = "Certificados", description = "Evaluacion de elegibilidad (>=5 asistencias) y emision")
@RestController
@RequestMapping("/api/v1/certificados")
public class CertificadoController {

    private final CertificadoService certificadoService;

    public CertificadoController(CertificadoService certificadoService) {
        this.certificadoService = certificadoService;
    }

    @Operation(summary = "Listar beneficiarios con conteo de asistencias y estado apto")
    @GetMapping("/beneficiarios")
    public ResponseEntity<List<BeneficiarioCertificadoDTO>> listarBeneficiarios() {
        return ResponseEntity.ok(certificadoService.listarBeneficiariosConAsistencias());
    }

    @Operation(summary = "Validar y emitir constancia de certificado para beneficiario apto")
    @PostMapping("/{beneficiarioId}/emitir")
    public ResponseEntity<BeneficiarioCertificadoDTO> emitir(@PathVariable Long beneficiarioId) {
        return ResponseEntity.ok(certificadoService.validarAptoCertificado(beneficiarioId));
    }
}
