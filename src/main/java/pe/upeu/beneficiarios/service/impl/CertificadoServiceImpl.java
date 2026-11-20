package pe.upeu.beneficiarios.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upeu.beneficiarios.dto.BeneficiarioCertificadoDTO;
import pe.upeu.beneficiarios.entity.Beneficiario;
import pe.upeu.beneficiarios.exception.RecursoNoEncontradoException;
import pe.upeu.beneficiarios.exception.ReglaNegocioException;
import pe.upeu.beneficiarios.repository.BeneficiarioRepository;
import pe.upeu.beneficiarios.service.services.AuditoriaService;
import pe.upeu.beneficiarios.service.services.CertificadoService;

import java.util.List;

@Service
public class CertificadoServiceImpl implements CertificadoService {

    private static final long MINIMO_ASISTENCIAS = 5L;

    private final BeneficiarioRepository beneficiarioRepository;
    private final AuditoriaService auditoriaService;

    public CertificadoServiceImpl(BeneficiarioRepository beneficiarioRepository, AuditoriaService auditoriaService) {
        this.beneficiarioRepository = beneficiarioRepository;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeneficiarioCertificadoDTO> listarBeneficiariosConAsistencias() {
        return beneficiarioRepository.listarParaCertificados();
    }

    @Override
    @Transactional
    public BeneficiarioCertificadoDTO validarAptoCertificado(Long beneficiarioId) {
        Beneficiario b = beneficiarioRepository.findById(beneficiarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Beneficiario no encontrado con id: " + beneficiarioId));

        long asistencias = beneficiarioRepository.contarAsistenciasValidas(beneficiarioId);
        if (asistencias < MINIMO_ASISTENCIAS) {
            throw new ReglaNegocioException("El beneficiario tiene " + asistencias + " asistencia(s). Se requieren minimo " + MINIMO_ASISTENCIAS + " para emitir el certificado");
        }

        auditoriaService.registrar("CERTIFICADO", "CERTIFICADOS", "Certificado emitido para beneficiario: " + b.getNombres() + " " + b.getApellidoPaterno());

        return new BeneficiarioCertificadoDTO(
                b.getId(),
                b.getNombres(),
                b.getApellidoPaterno(),
                b.getApellidoMaterno(),
                b.getProyectoTaller().getTaller().getNombre(),
                b.getProyectoTaller().getProyecto().getNombre(),
                b.getProyectoTaller().getProyecto().getFacultad().getNombre(),
                asistencias
        );
    }
}
