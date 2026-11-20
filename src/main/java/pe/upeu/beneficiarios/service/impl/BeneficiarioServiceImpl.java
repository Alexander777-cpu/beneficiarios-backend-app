package pe.upeu.beneficiarios.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upeu.beneficiarios.dto.BeneficiarioRequestDTO;
import pe.upeu.beneficiarios.dto.BeneficiarioResponseDTO;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.entity.Beneficiario;
import pe.upeu.beneficiarios.entity.ProyectoTaller;
import pe.upeu.beneficiarios.exception.RecursoNoEncontradoException;
import pe.upeu.beneficiarios.exception.ReglaNegocioException;
import pe.upeu.beneficiarios.exception.ValidacionException;
import pe.upeu.beneficiarios.mapper.BeneficiarioMapper;
import pe.upeu.beneficiarios.repository.AsistenciaRepository;
import pe.upeu.beneficiarios.repository.BeneficiarioRepository;
import pe.upeu.beneficiarios.repository.ProyectoTallerRepository;
import pe.upeu.beneficiarios.service.services.AuditoriaService;
import pe.upeu.beneficiarios.service.services.BeneficiarioService;
import pe.upeu.beneficiarios.util.PaginacionUtil;

import java.util.Set;

@Service
public class BeneficiarioServiceImpl implements BeneficiarioService {

    private static final Logger log = LoggerFactory.getLogger(BeneficiarioServiceImpl.class);
    private static final Set<String> CAMPOS_ORDENABLES = Set.of("id", "nombres", "apellidoPaterno", "edad", "fechaRegistro");

    private final BeneficiarioRepository beneficiarioRepository;
    private final ProyectoTallerRepository proyectoTallerRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final BeneficiarioMapper mapper;
    private final AuditoriaService auditoriaService;

    public BeneficiarioServiceImpl(BeneficiarioRepository beneficiarioRepository,
                                   ProyectoTallerRepository proyectoTallerRepository,
                                   AsistenciaRepository asistenciaRepository,
                                   BeneficiarioMapper mapper,
                                   AuditoriaService auditoriaService) {
        this.beneficiarioRepository = beneficiarioRepository;
        this.proyectoTallerRepository = proyectoTallerRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.mapper = mapper;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<BeneficiarioResponseDTO> listar(
            Long proyectoTallerId,
            Long proyectoId,
            Long facultadId,
            String buscar,
            int pagina,
            int tamanio,
            String ordenarPor,
            String direccion) {

        Pageable pageable = PaginacionUtil.construir(pagina, tamanio, ordenarPor, direccion, CAMPOS_ORDENABLES, "id");
        String filtroTexto = (buscar != null && !buscar.isBlank()) ? buscar.trim() : null;

        Page<BeneficiarioResponseDTO> page = beneficiarioRepository
                .buscar(proyectoTallerId, proyectoId, facultadId, filtroTexto, pageable)
                .map(mapper::toBeneficiarioResponse);

        return PaginaResponseDTO.de(page);
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiarioResponseDTO buscar(Long id) {
        Beneficiario b = beneficiarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Beneficiario no encontrado con id: " + id));
        return mapper.toBeneficiarioResponse(b);
    }

    @Override
    @Transactional
    public BeneficiarioResponseDTO crear(BeneficiarioRequestDTO request) {
        String nombresLimpios = request.getNombres().trim();
        if (beneficiarioRepository.existsByNombresIgnoreCase(nombresLimpios)) {
            throw new ValidacionException("nombres", "Ya existe un beneficiario registrado con los nombres: " + nombresLimpios);
        }

        ProyectoTaller pt = proyectoTallerRepository.findById(request.getProyectoTallerId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignacion Proyecto-Taller no encontrada con id: " + request.getProyectoTallerId()));

        Beneficiario entidad = Beneficiario.builder()
                .tipoBeneficiario(request.getTipoBeneficiario().trim().toUpperCase())
                .nombres(nombresLimpios)
                .apellidoPaterno(request.getApellidoPaterno().trim())
                .apellidoMaterno(request.getApellidoMaterno().trim())
                .telefono(limpiarOpcional(request.getTelefono()))
                .edad(request.getEdad())
                .gradoEstudios(limpiarOpcional(request.getGradoEstudios()))
                .estadoCivil(limpiarOpcional(request.getEstadoCivil()))
                .tutorNombre(limpiarOpcional(request.getTutorNombre()))
                .tallaPolo(limpiarOpcional(request.getTallaPolo()))
                .tallaPantalon(limpiarOpcional(request.getTallaPantalon()))
                .tallaCalzado(request.getTallaCalzado())
                .proyectoTaller(pt)
                .build();

        Beneficiario guardado = beneficiarioRepository.save(entidad);
        log.info("Beneficiario registrado id={} nombres={}", guardado.getId(), guardado.getNombres());
        auditoriaService.registrar("CREO", "BENEFICIARIO", "Se registro el beneficiario: " + guardado.getNombres() + " " + guardado.getApellidoPaterno());
        return mapper.toBeneficiarioResponse(guardado);
    }

    @Override
    @Transactional
    public BeneficiarioResponseDTO actualizar(Long id, BeneficiarioRequestDTO request) {
        Beneficiario b = beneficiarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Beneficiario no encontrado con id: " + id));

        String nombresLimpios = request.getNombres().trim();
        if (beneficiarioRepository.existsByNombresIgnoreCaseAndIdNot(nombresLimpios, id)) {
            throw new ValidacionException("nombres", "Ya existe otro beneficiario registrado con los nombres: " + nombresLimpios);
        }

        ProyectoTaller pt = proyectoTallerRepository.findById(request.getProyectoTallerId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignacion Proyecto-Taller no encontrada con id: " + request.getProyectoTallerId()));

        b.setTipoBeneficiario(request.getTipoBeneficiario().trim().toUpperCase());
        b.setNombres(nombresLimpios);
        b.setApellidoPaterno(request.getApellidoPaterno().trim());
        b.setApellidoMaterno(request.getApellidoMaterno().trim());
        b.setTelefono(limpiarOpcional(request.getTelefono()));
        b.setEdad(request.getEdad());
        b.setGradoEstudios(limpiarOpcional(request.getGradoEstudios()));
        b.setEstadoCivil(limpiarOpcional(request.getEstadoCivil()));
        b.setTutorNombre(limpiarOpcional(request.getTutorNombre()));
        b.setTallaPolo(limpiarOpcional(request.getTallaPolo()));
        b.setTallaPantalon(limpiarOpcional(request.getTallaPantalon()));
        b.setTallaCalzado(request.getTallaCalzado());
        b.setProyectoTaller(pt);

        Beneficiario actualizado = beneficiarioRepository.save(b);
        auditoriaService.registrar("EDITO", "BENEFICIARIO", "Se actualizo el beneficiario: " + actualizado.getNombres() + " " + actualizado.getApellidoPaterno());
        return mapper.toBeneficiarioResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Beneficiario b = beneficiarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Beneficiario no encontrado con id: " + id));

        long asistencias = asistenciaRepository.countByBeneficiarioId(id);
        if (asistencias > 0) {
            throw new ReglaNegocioException("No se puede eliminar el beneficiario porque cuenta con " + asistencias + " registro(s) de asistencia asociados");
        }

        beneficiarioRepository.delete(b);
        auditoriaService.registrar("ELIMINO", "BENEFICIARIO", "Se elimino el beneficiario: " + b.getNombres() + " " + b.getApellidoPaterno());
    }

    private String limpiarOpcional(String valor) {
        return (valor != null && !valor.isBlank()) ? valor.trim() : null;
    }
}
