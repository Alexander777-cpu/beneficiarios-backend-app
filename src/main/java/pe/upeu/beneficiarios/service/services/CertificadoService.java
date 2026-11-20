package pe.upeu.beneficiarios.service.services;

import pe.upeu.beneficiarios.dto.BeneficiarioCertificadoDTO;

import java.util.List;

public interface CertificadoService {
    List<BeneficiarioCertificadoDTO> listarBeneficiariosConAsistencias();
    BeneficiarioCertificadoDTO validarAptoCertificado(Long beneficiarioId);
}
