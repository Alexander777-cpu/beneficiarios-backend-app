package pe.upeu.beneficiarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upeu.beneficiarios.entity.Taller;

import java.util.Optional;

@Repository
public interface TallerRepository extends JpaRepository<Taller, Long> {
    Optional<Taller> findByNombreIgnoreCase(String nombre);
}
