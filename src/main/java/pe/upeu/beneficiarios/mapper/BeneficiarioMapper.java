package pe.upeu.beneficiarios.mapper;

import org.springframework.stereotype.Component;
import pe.upeu.beneficiarios.dto.*;
import pe.upeu.beneficiarios.entity.*;

@Component
public class BeneficiarioMapper {

    public FacultadResponseDTO toFacultadResponse(Facultad f) {
        if (f == null) return null;
        return FacultadResponseDTO.builder()
                .id(f.getId())
                .nombre(f.getNombre())
                .build();
    }

    public ProyectoResponseDTO toProyectoResponse(Proyecto p) {
        if (p == null) return null;
        return ProyectoResponseDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .facultadId(p.getFacultad() != null ? p.getFacultad().getId() : null)
                .facultadNombre(p.getFacultad() != null ? p.getFacultad().getNombre() : null)
                .build();
    }

    public TallerResponseDTO toTallerResponse(ProyectoTaller pt) {
        if (pt == null) return null;
        Proyecto p = pt.getProyecto();
        Facultad f = p != null ? p.getFacultad() : null;
        return TallerResponseDTO.builder()
                .id(pt.getId())
                .proyectoTallerId(pt.getId())
                .nombre(pt.getTaller() != null ? pt.getTaller().getNombre() : null)
                .proyectoId(p != null ? p.getId() : null)
                .proyectoNombre(p != null ? p.getNombre() : null)
                .facultadId(f != null ? f.getId() : null)
                .facultadNombre(f != null ? f.getNombre() : null)
                .build();
    }

    public BeneficiarioResponseDTO toBeneficiarioResponse(Beneficiario b) {
        if (b == null) return null;
        ProyectoTaller pt = b.getProyectoTaller();
        Proyecto p = pt != null ? pt.getProyecto() : null;
        Facultad f = p != null ? p.getFacultad() : null;
        return BeneficiarioResponseDTO.builder()
                .id(b.getId())
                .tipoBeneficiario(b.getTipoBeneficiario())
                .nombres(b.getNombres())
                .apellidoPaterno(b.getApellidoPaterno())
                .apellidoMaterno(b.getApellidoMaterno())
                .telefono(b.getTelefono())
                .edad(b.getEdad())
                .gradoEstudios(b.getGradoEstudios())
                .estadoCivil(b.getEstadoCivil())
                .tutorNombre(b.getTutorNombre())
                .tallaPolo(b.getTallaPolo())
                .tallaPantalon(b.getTallaPantalon())
                .tallaCalzado(b.getTallaCalzado())
                .proyectoTallerId(pt != null ? pt.getId() : null)
                .tallerNombre(pt != null && pt.getTaller() != null ? pt.getTaller().getNombre() : null)
                .proyectoId(p != null ? p.getId() : null)
                .proyectoNombre(p != null ? p.getNombre() : null)
                .facultadId(f != null ? f.getId() : null)
                .facultadNombre(f != null ? f.getNombre() : null)
                .fechaRegistro(b.getFechaRegistro())
                .build();
    }

    public AsistenciaItemDTO toAsistenciaItem(Beneficiario b, boolean asistio, long totalAsistencias) {
        ProyectoTaller pt = b.getProyectoTaller();
        Proyecto p = pt != null ? pt.getProyecto() : null;
        Facultad f = p != null ? p.getFacultad() : null;
        return AsistenciaItemDTO.builder()
                .beneficiarioId(b.getId())
                .nombres(b.getNombres())
                .apellidos((b.getApellidoPaterno() + " " + b.getApellidoMaterno()).trim())
                .tipoBeneficiario(b.getTipoBeneficiario())
                .asistio(asistio)
                .totalAsistenciasHistoricas(totalAsistencias)
                .tallerNombre(pt != null && pt.getTaller() != null ? pt.getTaller().getNombre() : null)
                .proyectoNombre(p != null ? p.getNombre() : null)
                .facultadNombre(f != null ? f.getNombre() : null)
                .build();
    }

    public AuditoriaResponseDTO toAuditoriaResponse(Auditoria a) {
        if (a == null) return null;
        return AuditoriaResponseDTO.builder()
                .id(a.getId())
                .usuarioAccion(a.getUsuarioAccion())
                .accion(a.getAccion())
                .modulo(a.getModulo())
                .descripcion(a.getDescripcion())
                .fecha(a.getFecha())
                .build();
    }
}
