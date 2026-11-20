package pe.upeu.beneficiarios.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upeu.beneficiarios.dto.AsistenciaItemDTO;
import pe.upeu.beneficiarios.dto.GuardarAsistenciaRequestDTO;
import pe.upeu.beneficiarios.entity.Asistencia;
import pe.upeu.beneficiarios.entity.Beneficiario;
import pe.upeu.beneficiarios.entity.ProyectoTaller;
import pe.upeu.beneficiarios.exception.RecursoNoEncontradoException;
import pe.upeu.beneficiarios.mapper.BeneficiarioMapper;
import pe.upeu.beneficiarios.repository.AsistenciaRepository;
import pe.upeu.beneficiarios.repository.BeneficiarioRepository;
import pe.upeu.beneficiarios.repository.ProyectoTallerRepository;
import pe.upeu.beneficiarios.service.services.AsistenciaService;
import pe.upeu.beneficiarios.service.services.AuditoriaService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final BeneficiarioRepository beneficiarioRepository;
    private final ProyectoTallerRepository proyectoTallerRepository;
    private final BeneficiarioMapper mapper;
    private final AuditoriaService auditoriaService;

    public AsistenciaServiceImpl(AsistenciaRepository asistenciaRepository,
                                 BeneficiarioRepository beneficiarioRepository,
                                 ProyectoTallerRepository proyectoTallerRepository,
                                 BeneficiarioMapper mapper,
                                 AuditoriaService auditoriaService) {
        this.asistenciaRepository = asistenciaRepository;
        this.beneficiarioRepository = beneficiarioRepository;
        this.proyectoTallerRepository = proyectoTallerRepository;
        this.mapper = mapper;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaItemDTO> obtenerPlanilla(Long proyectoTallerId, String fecha) {
        if (!proyectoTallerRepository.existsById(proyectoTallerId)) {
            throw new RecursoNoEncontradoException("Asignacion Proyecto-Taller no encontrada con id: " + proyectoTallerId);
        }

        String fechaConsulta = (fecha != null && !fecha.isBlank()) ? fecha.trim() : LocalDate.now().toString();
        List<Beneficiario> beneficiarios = beneficiarioRepository.findByProyectoTallerIdOrderByIdAsc(proyectoTallerId);
        if (beneficiarios.isEmpty()) {
            return Collections.emptyList();
        }

        List<Asistencia> asistenciasDia = asistenciaRepository.findByProyectoTallerIdAndFecha(proyectoTallerId, fechaConsulta);
        Map<Long, Boolean> asistioMap = asistenciasDia.stream()
                .collect(Collectors.toMap(
                        a -> a.getBeneficiario().getId(),
                        a -> Boolean.TRUE.equals(a.getAsistio()),
                        (v1, v2) -> v1
                ));

        Map<Long, Long> conteosMap = obtenerConteoAsistencias(beneficiarios.stream().map(Beneficiario::getId).toList());

        return beneficiarios.stream()
                .map(b -> mapper.toAsistenciaItem(
                        b,
                        asistioMap.getOrDefault(b.getId(), false),
                        conteosMap.getOrDefault(b.getId(), 0L)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaItemDTO> obtenerReporteGeneral(Long facultadId, Long proyectoId) {
        List<Beneficiario> beneficiarios = beneficiarioRepository.findAllFiltered(facultadId, proyectoId);
        if (beneficiarios.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Long> conteosMap = obtenerConteoAsistencias(beneficiarios.stream().map(Beneficiario::getId).toList());

        return beneficiarios.stream()
                .map(b -> mapper.toAsistenciaItem(b, false, conteosMap.getOrDefault(b.getId(), 0L)))
                .toList();
    }

    @Override
    @Transactional
    public void guardar(GuardarAsistenciaRequestDTO request) {
        ProyectoTaller pt = proyectoTallerRepository.findById(request.getProyectoTallerId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignacion Proyecto-Taller no encontrada con id: " + request.getProyectoTallerId()));

        List<Asistencia> existentes = asistenciaRepository.findByProyectoTallerIdAndFecha(pt.getId(), request.getFecha());
        Map<Long, Asistencia> mapaExistentes = existentes.stream()
                .collect(Collectors.toMap(a -> a.getBeneficiario().getId(), a -> a, (a1, a2) -> a1));

        List<Long> ids = request.getDetalles().stream()
                .map(GuardarAsistenciaRequestDTO.DetalleAsistenciaDTO::getBeneficiarioId)
                .toList();
        Map<Long, Beneficiario> mapaBeneficiarios = beneficiarioRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Beneficiario::getId, b -> b));

        List<Asistencia> paraGuardar = new ArrayList<>();
        for (GuardarAsistenciaRequestDTO.DetalleAsistenciaDTO det : request.getDetalles()) {
            boolean asistio = Boolean.TRUE.equals(det.getAsistio());
            if (mapaExistentes.containsKey(det.getBeneficiarioId())) {
                Asistencia a = mapaExistentes.get(det.getBeneficiarioId());
                a.setAsistio(asistio);
                paraGuardar.add(a);
            } else {
                Beneficiario b = mapaBeneficiarios.get(det.getBeneficiarioId());
                if (b == null) {
                    throw new RecursoNoEncontradoException("Beneficiario no encontrado con id: " + det.getBeneficiarioId());
                }
                paraGuardar.add(Asistencia.builder()
                        .beneficiario(b)
                        .proyectoTaller(pt)
                        .fecha(request.getFecha())
                        .asistio(asistio)
                        .build());
            }
        }

        asistenciaRepository.saveAll(paraGuardar);
        auditoriaService.registrar("REGISTRO", "ASISTENCIA", "Asistencia guardada para fecha " + request.getFecha() + " en taller id=" + pt.getId());
    }

    private Map<Long, Long> obtenerConteoAsistencias(List<Long> ids) {
        if (ids.isEmpty()) return Collections.emptyMap();
        Map<Long, Long> map = new HashMap<>();
        for (Object[] row : asistenciaRepository.countTotalAsistenciasPorBeneficiarioIds(ids)) {
            map.put((Long) row[0], ((Number) row[1]).longValue());
        }
        return map;
    }
}
