package pe.upeu.beneficiarios.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upeu.beneficiarios.dto.FacultadRequestDTO;
import pe.upeu.beneficiarios.dto.FacultadResponseDTO;
import pe.upeu.beneficiarios.dto.PaginaResponseDTO;
import pe.upeu.beneficiarios.entity.Facultad;
import pe.upeu.beneficiarios.exception.RecursoNoEncontradoException;
import pe.upeu.beneficiarios.exception.ReglaNegocioException;
import pe.upeu.beneficiarios.exception.ValidacionException;
import pe.upeu.beneficiarios.mapper.BeneficiarioMapper;
import pe.upeu.beneficiarios.repository.FacultadRepository;
import pe.upeu.beneficiarios.repository.ProyectoRepository;
import pe.upeu.beneficiarios.repository.UsuarioRepository;
import pe.upeu.beneficiarios.service.services.AuditoriaService;
import pe.upeu.beneficiarios.service.services.FacultadService;
import pe.upeu.beneficiarios.util.PaginacionUtil;

import java.util.Set;

@Service
public class FacultadServiceImpl implements FacultadService {

    private static final Set<String> CAMPOS_ORDENABLES = Set.of("id", "nombre");

    private final FacultadRepository facultadRepository;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BeneficiarioMapper mapper;
    private final AuditoriaService auditoriaService;

    public FacultadServiceImpl(FacultadRepository facultadRepository,
                               ProyectoRepository proyectoRepository,
                               UsuarioRepository usuarioRepository,
                               BeneficiarioMapper mapper,
                               AuditoriaService auditoriaService) {
        this.facultadRepository = facultadRepository;
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<FacultadResponseDTO> listar(int pagina, int tamanio, String ordenarPor, String direccion) {
        Pageable pageable = PaginacionUtil.construir(pagina, tamanio, ordenarPor, direccion, CAMPOS_ORDENABLES, "id");
        Page<FacultadResponseDTO> page = facultadRepository.findAll(pageable).map(mapper::toFacultadResponse);
        return PaginaResponseDTO.de(page);
    }

    @Override
    @Transactional(readOnly = true)
    public FacultadResponseDTO buscar(Long id) {
        Facultad f = facultadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + id));
        return mapper.toFacultadResponse(f);
    }

    @Override
    @Transactional
    public FacultadResponseDTO crear(FacultadRequestDTO request) {
        String nombre = request.getNombre().trim();
        if (facultadRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ValidacionException("nombre", "Ya existe una facultad con el nombre '" + nombre + "'");
        }

        Facultad guardada = facultadRepository.save(Facultad.builder().nombre(nombre).build());
        auditoriaService.registrar("CREO", "FACULTAD", "Se creo la facultad: " + guardada.getNombre());
        return mapper.toFacultadResponse(guardada);
    }

    @Override
    @Transactional
    public FacultadResponseDTO actualizar(Long id, FacultadRequestDTO request) {
        Facultad f = facultadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + id));

        String nombre = request.getNombre().trim();
        if (facultadRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new ValidacionException("nombre", "Ya existe otra facultad con el nombre '" + nombre + "'");
        }

        f.setNombre(nombre);
        Facultad actualizada = facultadRepository.save(f);
        auditoriaService.registrar("EDITO", "FACULTAD", "Se actualizo la facultad a: " + actualizada.getNombre());
        return mapper.toFacultadResponse(actualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Facultad f = facultadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + id));

        long proyectos = proyectoRepository.countByFacultadId(id);
        long usuarios = usuarioRepository.countByFacultadId(id);
        if (proyectos > 0 || usuarios > 0) {
            throw new ReglaNegocioException("No se puede eliminar la facultad porque tiene " + proyectos + " proyecto(s) y " + usuarios + " usuario(s) asociados");
        }

        facultadRepository.delete(f);
        auditoriaService.registrar("ELIMINO", "FACULTAD", "Se elimino la facultad: " + f.getNombre());
    }
}
