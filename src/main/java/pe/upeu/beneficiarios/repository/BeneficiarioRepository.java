package pe.upeu.beneficiarios.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upeu.beneficiarios.dto.BeneficiarioCertificadoDTO;
import pe.upeu.beneficiarios.entity.Beneficiario;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiarioRepository extends JpaRepository<Beneficiario, Long> {

    @EntityGraph(attributePaths = {"proyectoTaller", "proyectoTaller.taller", "proyectoTaller.proyecto", "proyectoTaller.proyecto.facultad"})
    @Query(value = """
        SELECT b FROM Beneficiario b
        WHERE (:proyectoTallerId IS NULL OR b.proyectoTaller.id = :proyectoTallerId)
          AND (:proyectoId IS NULL OR b.proyectoTaller.proyecto.id = :proyectoId)
          AND (:facultadId IS NULL OR b.proyectoTaller.proyecto.facultad.id = :facultadId)
          AND (:buscar IS NULL OR LOWER(b.nombres) LIKE LOWER(CONCAT('%', :buscar, '%'))
               OR LOWER(b.apellidoPaterno) LIKE LOWER(CONCAT('%', :buscar, '%'))
               OR LOWER(b.apellidoMaterno) LIKE LOWER(CONCAT('%', :buscar, '%')))
        """,
        countQuery = """
        SELECT COUNT(b) FROM Beneficiario b
        WHERE (:proyectoTallerId IS NULL OR b.proyectoTaller.id = :proyectoTallerId)
          AND (:proyectoId IS NULL OR b.proyectoTaller.proyecto.id = :proyectoId)
          AND (:facultadId IS NULL OR b.proyectoTaller.proyecto.facultad.id = :facultadId)
          AND (:buscar IS NULL OR LOWER(b.nombres) LIKE LOWER(CONCAT('%', :buscar, '%'))
               OR LOWER(b.apellidoPaterno) LIKE LOWER(CONCAT('%', :buscar, '%'))
               OR LOWER(b.apellidoMaterno) LIKE LOWER(CONCAT('%', :buscar, '%')))
        """)
    Page<Beneficiario> buscar(
            @Param("proyectoTallerId") Long proyectoTallerId,
            @Param("proyectoId") Long proyectoId,
            @Param("facultadId") Long facultadId,
            @Param("buscar") String buscar,
            Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"proyectoTaller", "proyectoTaller.taller", "proyectoTaller.proyecto", "proyectoTaller.proyecto.facultad"})
    Optional<Beneficiario> findById(Long id);

    @EntityGraph(attributePaths = {"proyectoTaller", "proyectoTaller.taller", "proyectoTaller.proyecto", "proyectoTaller.proyecto.facultad"})
    List<Beneficiario> findByProyectoTallerIdOrderByIdAsc(Long proyectoTallerId);

    @EntityGraph(attributePaths = {"proyectoTaller", "proyectoTaller.taller", "proyectoTaller.proyecto", "proyectoTaller.proyecto.facultad"})
    @Query("SELECT b FROM Beneficiario b WHERE (:proyectoId IS NULL OR b.proyectoTaller.proyecto.id = :proyectoId) AND (:facultadId IS NULL OR b.proyectoTaller.proyecto.facultad.id = :facultadId) ORDER BY b.id ASC")
    List<Beneficiario> findAllFiltered(@Param("facultadId") Long facultadId, @Param("proyectoId") Long proyectoId);

    long countByProyectoTallerId(Long proyectoTallerId);
    boolean existsByNombresIgnoreCase(String nombres);
    boolean existsByNombresIgnoreCaseAndIdNot(String nombres, Long id);

    @Query("""
        SELECT new pe.upeu.beneficiarios.dto.BeneficiarioCertificadoDTO(
            b.id,
            b.nombres,
            b.apellidoPaterno,
            b.apellidoMaterno,
            b.proyectoTaller.taller.nombre,
            b.proyectoTaller.proyecto.nombre,
            b.proyectoTaller.proyecto.facultad.nombre,
            (SELECT COUNT(a.id) FROM Asistencia a WHERE a.beneficiario.id = b.id AND a.asistio = true)
        )
        FROM Beneficiario b
        ORDER BY b.proyectoTaller.proyecto.facultad.nombre ASC,
                 b.proyectoTaller.proyecto.nombre ASC,
                 b.apellidoPaterno ASC,
                 b.nombres ASC
    """)
    List<BeneficiarioCertificadoDTO> listarParaCertificados();

    @Query("SELECT COUNT(a.id) FROM Asistencia a WHERE a.beneficiario.id = :beneficiarioId AND a.asistio = true")
    long contarAsistenciasValidas(@Param("beneficiarioId") Long beneficiarioId);
}
