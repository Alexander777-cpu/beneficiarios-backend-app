package pe.upeu.beneficiarios.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.dto.TallerRequestDTO;
import pe.upeu.beneficiarios.dto.TallerResponseDTO;
import pe.upeu.beneficiarios.entity.Proyecto;
import pe.upeu.beneficiarios.entity.ProyectoTaller;
import pe.upeu.beneficiarios.entity.Taller;
import pe.upeu.beneficiarios.exception.RecursoNoEncontradoException;
import pe.upeu.beneficiarios.exception.ReglaNegocioException;
import pe.upeu.beneficiarios.exception.ValidacionException;
import pe.upeu.beneficiarios.mapper.BeneficiarioMapper;
import pe.upeu.beneficiarios.repository.BeneficiarioRepository;
import pe.upeu.beneficiarios.repository.ProyectoRepository;
import pe.upeu.beneficiarios.repository.ProyectoTallerRepository;
import pe.upeu.beneficiarios.repository.TallerRepository;
import pe.upeu.beneficiarios.service.services.AuditoriaService;
import pe.upeu.beneficiarios.service.services.TallerService;
import pe.upeu.beneficiarios.util.PaginacionUtil;

import java.util.Set;

@Service
public class TallerServiceImpl implements TallerService {

    private static final Set<String> CAMPOS_ORDENABLES = Set.of("id");

    private final TallerRepository tallerRepository;
    private final ProyectoRepository proyectoRepository;
    private final ProyectoTallerRepository proyectoTallerRepository;
    private final BeneficiarioRepository beneficiarioRepository;
    private final BeneficiarioMapper mapper;
    private final AuditoriaService auditoriaService;

    public TallerServiceImpl(TallerRepository tallerRepository,
                             ProyectoRepository proyectoRepository,
                             ProyectoTallerRepository proyectoTallerRepository,
                             BeneficiarioRepository beneficiarioRepository,
                             BeneficiarioMapper mapper,
                             AuditoriaService auditoriaService) {
        this.tallerRepository = tallerRepository;
        this.proyectoRepository = proyectoRepository;
        this.proyectoTallerRepository = proyectoTallerRepository;
        this.beneficiarioRepository = beneficiarioRepository;
        this.mapper = mapper;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<TallerResponseDTO> listar(Long proyectoId, Long facultadId, int pagina, int tamanio, String ordenarPor, String direccion) {
        Pageable pageable = PaginacionUtil.construir(pagina, tamanio, ordenarPor, direccion, CAMPOS_ORDENABLES, "id");
        Page<TallerResponseDTO> page = proyectoTallerRepository.buscar(proyectoId, facultadId, pageable).map(mapper::toTallerResponse);
        return PaginaResponseDTO.de(page);
    }

    @Override
    @Transactional(readOnly = true)
    public TallerResponseDTO buscar(Long id) {
        ProyectoTaller pt = proyectoTallerRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignacion de taller no encontrada con id: " + id));
        return mapper.toTallerResponse(pt);
    }

    @Override
    @Transactional
    public TallerResponseDTO crear(TallerRequestDTO request) {
        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado con id: " + request.getProyectoId()));

        String nombre = request.getNombre().trim();
        if (proyectoTallerRepository.existsByProyectoIdAndTaller_NombreIgnoreCase(proyecto.getId(), nombre)) {
            throw new ValidacionException("nombre", "El taller '" + nombre + "' ya existe en el proyecto seleccionado");
        }

        Taller taller = tallerRepository.findByNombreIgnoreCase(nombre)
                .orElseGet(() -> tallerRepository.save(Taller.builder().nombre(nombre).build()));

        ProyectoTaller guardado = proyectoTallerRepository.save(
                ProyectoTaller.builder().proyecto(proyecto).taller(taller).build()
        );
        auditoriaService.registrar("CREO", "TALLER", "Se asigno el taller '" + nombre + "' al proyecto '" + proyecto.getNombre() + "'");
        return mapper.toTallerResponse(guardado);
    }

    @Override
    @Transactional
    public TallerResponseDTO actualizar(Long id, TallerRequestDTO request) {
        ProyectoTaller pt = proyectoTallerRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignacion de taller no encontrada con id: " + id));

        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado con id: " + request.getProyectoId()));

        String nombre = request.getNombre().trim();
        if (proyectoTallerRepository.existsByProyectoIdAndTallerNombreIgnoreCaseExcludingId(proyecto.getId(), nombre, id)) {
            throw new ValidacionException("nombre", "El taller '" + nombre + "' ya existe en el proyecto seleccionado");
        }

        Taller taller = tallerRepository.findByNombreIgnoreCase(nombre)
                .orElseGet(() -> tallerRepository.save(Taller.builder().nombre(nombre).build()));

        pt.setProyecto(proyecto);
        pt.setTaller(taller);
        ProyectoTaller actualizado = proyectoTallerRepository.save(pt);
        auditoriaService.registrar("EDITO", "TALLER", "Se actualizo el taller a '" + nombre + "'");
        return mapper.toTallerResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        ProyectoTaller pt = proyectoTallerRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignacion de taller no encontrada con id: " + id));

        long beneficiarios = beneficiarioRepository.countByProyectoTallerId(id);
        if (beneficiarios > 0) {
            throw new ReglaNegocioException("No se puede eliminar el taller porque tiene " + beneficiarios + " beneficiario(s) inscritos");
        }

        proyectoTallerRepository.delete(pt);
        auditoriaService.registrar("ELIMINO", "TALLER", "Se elimino la asignacion de taller id=" + id);
    }
}
