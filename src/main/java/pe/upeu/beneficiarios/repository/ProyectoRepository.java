package pe.upeu.beneficiarios.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upeu.beneficiarios.entity.Proyecto;

import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    @EntityGraph(attributePaths = {"facultad"})
    @Query(value = "SELECT p FROM Proyecto p WHERE (:facultadId IS NULL OR p.facultad.id = :facultadId)",
           countQuery = "SELECT COUNT(p) FROM Proyecto p WHERE (:facultadId IS NULL OR p.facultad.id = :facultadId)")
    Page<Proyecto> buscar(@Param("facultadId") Long facultadId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"facultad"})
    Optional<Proyecto> findById(Long id);

    long countByFacultadId(Long facultadId);
    boolean existsByNombreIgnoreCaseAndFacultadId(String nombre, Long facultadId);
    boolean existsByNombreIgnoreCaseAndFacultadIdAndIdNot(String nombre, Long facultadId, Long id);
}
