package pe.upeu.beneficiarios.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upeu.beneficiarios.entity.ProyectoTaller;

import java.util.Optional;

@Repository
public interface ProyectoTallerRepository extends JpaRepository<ProyectoTaller, Long> {

    @EntityGraph(attributePaths = {"proyecto", "proyecto.facultad", "taller"})
    @Query(value = """
        SELECT pt FROM ProyectoTaller pt
        WHERE (:proyectoId IS NULL OR pt.proyecto.id = :proyectoId)
          AND (:facultadId IS NULL OR pt.proyecto.facultad.id = :facultadId)
        """,
        countQuery = """
        SELECT COUNT(pt) FROM ProyectoTaller pt
        WHERE (:proyectoId IS NULL OR pt.proyecto.id = :proyectoId)
          AND (:facultadId IS NULL OR pt.proyecto.facultad.id = :facultadId)
        """)
    Page<ProyectoTaller> buscar(@Param("proyectoId") Long proyectoId, @Param("facultadId") Long facultadId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"proyecto", "proyecto.facultad", "taller"})
    Optional<ProyectoTaller> findById(Long id);

    long countByProyectoId(Long proyectoId);
    boolean existsByProyectoIdAndTaller_NombreIgnoreCase(Long proyectoId, String nombreTaller);

    @Query("SELECT COUNT(pt) > 0 FROM ProyectoTaller pt WHERE pt.proyecto.id = :proyectoId AND LOWER(TRIM(pt.taller.nombre)) = LOWER(TRIM(:nombre)) AND pt.id <> :excludeId")
    boolean existsByProyectoIdAndTallerNombreIgnoreCaseExcludingId(
            @Param("proyectoId") Long proyectoId,
            @Param("nombre") String nombre,
            @Param("excludeId") Long excludeId);
}
