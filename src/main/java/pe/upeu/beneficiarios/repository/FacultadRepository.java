package pe.upeu.beneficiarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upeu.beneficiarios.entity.Facultad;

@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
