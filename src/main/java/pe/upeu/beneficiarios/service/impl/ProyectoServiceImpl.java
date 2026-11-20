package pe.upeu.beneficiarios.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.dto.ProyectoRequestDTO;
import pe.upeu.beneficiarios.dto.ProyectoResponseDTO;
import pe.upeu.beneficiarios.entity.Facultad;
import pe.upeu.beneficiarios.entity.Proyecto;
import pe.upeu.beneficiarios.exception.RecursoNoEncontradoException;
import pe.upeu.beneficiarios.exception.ReglaNegocioException;
import pe.upeu.beneficiarios.exception.ValidacionException;
import pe.upeu.beneficiarios.mapper.BeneficiarioMapper;
import pe.upeu.beneficiarios.repository.FacultadRepository;
import pe.upeu.beneficiarios.repository.ProyectoRepository;
import pe.upeu.beneficiarios.repository.ProyectoTallerRepository;
import pe.upeu.beneficiarios.repository.UsuarioRepository;
import pe.upeu.beneficiarios.service.services.AuditoriaService;
import pe.upeu.beneficiarios.service.services.ProyectoService;
import pe.upeu.beneficiarios.util.PaginacionUtil;

import java.util.Set;

@Service
public class ProyectoServiceImpl implements ProyectoService {

    private static final Set<String> CAMPOS_ORDENABLES = Set.of("id", "nombre");

    private final ProyectoRepository proyectoRepository;
    private final FacultadRepository facultadRepository;
    private final ProyectoTallerRepository proyectoTallerRepository;
    private final UsuarioRepository usuarioRepository;
    private final BeneficiarioMapper mapper;
    private final AuditoriaService auditoriaService;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository,
                               FacultadRepository facultadRepository,
                               ProyectoTallerRepository proyectoTallerRepository,
                               UsuarioRepository usuarioRepository,
                               BeneficiarioMapper mapper,
                               AuditoriaService auditoriaService) {
        this.proyectoRepository = proyectoRepository;
        this.facultadRepository = facultadRepository;
        this.proyectoTallerRepository = proyectoTallerRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<ProyectoResponseDTO> listar(Long facultadId, int pagina, int tamanio, String ordenarPor, String direccion) {
        Pageable pageable = PaginacionUtil.construir(pagina, tamanio, ordenarPor, direccion, CAMPOS_ORDENABLES, "id");
        Page<ProyectoResponseDTO> page = proyectoRepository.buscar(facultadId, pageable).map(mapper::toProyectoResponse);
        return PaginaResponseDTO.de(page);
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoResponseDTO buscar(Long id) {
        Proyecto p = proyectoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado con id: " + id));
        return mapper.toProyectoResponse(p);
    }

    @Override
    @Transactional
    public ProyectoResponseDTO crear(ProyectoRequestDTO request) {
        String nombre = request.getNombre().trim();
        if (proyectoRepository.existsByNombreIgnoreCaseAndFacultadId(nombre, request.getFacultadId())) {
            throw new ValidacionException("nombre", "Ya existe un proyecto con ese nombre en la facultad seleccionada");
        }

        Facultad facultad = facultadRepository.findById(request.getFacultadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + request.getFacultadId()));

        Proyecto guardado = proyectoRepository.save(Proyecto.builder().nombre(nombre).facultad(facultad).build());
        auditoriaService.registrar("CREO", "PROYECTO", "Se creo el proyecto: " + guardado.getNombre());
        return mapper.toProyectoResponse(guardado);
    }

    @Override
    @Transactional
    public ProyectoResponseDTO actualizar(Long id, ProyectoRequestDTO request) {
        Proyecto p = proyectoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado con id: " + id));

        String nombre = request.getNombre().trim();
        if (proyectoRepository.existsByNombreIgnoreCaseAndFacultadIdAndIdNot(nombre, request.getFacultadId(), id)) {
            throw new ValidacionException("nombre", "Ya existe otro proyecto con ese nombre en la facultad seleccionada");
        }

        Facultad facultad = facultadRepository.findById(request.getFacultadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + request.getFacultadId()));

        p.setNombre(nombre);
        p.setFacultad(facultad);
        Proyecto actualizado = proyectoRepository.save(p);
        auditoriaService.registrar("EDITO", "PROYECTO", "Se actualizo el proyecto: " + actualizado.getNombre());
        return mapper.toProyectoResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Proyecto p = proyectoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado con id: " + id));

        long talleres = proyectoTallerRepository.countByProyectoId(id);
        long usuarios = usuarioRepository.countByProyectoId(id);
        if (talleres > 0 || usuarios > 0) {
            throw new ReglaNegocioException("No se puede eliminar el proyecto porque tiene " + talleres + " taller(es) y " + usuarios + " lider(es) asociados");
        }

        proyectoRepository.delete(p);
        auditoriaService.registrar("ELIMINO", "PROYECTO", "Se elimino el proyecto: " + p.getNombre());
    }
}
