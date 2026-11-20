package pe.upeu.beneficiarios.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upeu.beneficiarios.dto.AuditoriaResponseDTO;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.entity.Auditoria;
import pe.upeu.beneficiarios.exception.ReglaNegocioException;
import pe.upeu.beneficiarios.mapper.BeneficiarioMapper;
import pe.upeu.beneficiarios.repository.AuditoriaRepository;
import pe.upeu.beneficiarios.service.services.AuditoriaService;
import pe.upeu.beneficiarios.util.PaginacionUtil;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final BeneficiarioMapper mapper;

    public AuditoriaServiceImpl(AuditoriaRepository auditoriaRepository, BeneficiarioMapper mapper) {
        this.auditoriaRepository = auditoriaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void registrar(String accion, String modulo, String descripcion) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usuarioActual = (auth != null && auth.isAuthenticated()) ? auth.getName() : "SISTEMA";

        Auditoria aud = Auditoria.builder()
                .usuarioAccion(usuarioActual)
                .accion(accion)
                .modulo(modulo)
                .descripcion(descripcion)
                .build();
        auditoriaRepository.save(aud);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<AuditoriaResponseDTO> listar(int pagina, int tamanio) {
        if (pagina < 0 || tamanio < 1 || tamanio > PaginacionUtil.TAMANIO_MAXIMO) {
            throw new ReglaNegocioException("Parametros de paginacion invalidos");
        }
        Page<AuditoriaResponseDTO> page = auditoriaRepository
                .findAllByOrderByIdDesc(PageRequest.of(pagina, tamanio))
                .map(mapper::toAuditoriaResponse);
        return PaginaResponseDTO.de(page);
    }
}
